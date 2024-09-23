package com.tipsuy.readaufplayers.service;

import com.tipsuy.readaufplayers.dao.ExecutionRepository;
import com.tipsuy.readaufplayers.dao.PlayerExecutionRepository;
import com.tipsuy.readaufplayers.dao.PlayerRepository;
import com.tipsuy.readaufplayers.dao.SeasonRepository;
import com.tipsuy.readaufplayers.dao.TeamLastOpponentRepository;
import com.tipsuy.readaufplayers.dao.TeamRepository;
import com.tipsuy.readaufplayers.domain.Execution;
import com.tipsuy.readaufplayers.domain.Player;
import com.tipsuy.readaufplayers.domain.PlayerExecution;
import com.tipsuy.readaufplayers.domain.dto.ReadPlayerDTO;
import com.tipsuy.readaufplayers.domain.pk.PlayerExecutionPK;
import com.tipsuy.readaufplayers.util.ReadAufPage;
import com.tipsuy.readaufplayers.util.TextUtil;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlayerService {

   private final PlayerRepository playerRepository;

   private final SeasonRepository seasonRepository;

   private final TeamRepository teamRepository;

   private final ExecutionRepository executionRepository;

   private final PlayerExecutionRepository playerExecutionRepository;

   private final TeamLastOpponentRepository teamLastOpponentRepository;

   private final Clock clock;

   @Transactional(propagation = Propagation.REQUIRES_NEW)
   public List<Player> saveAll(final Iterable<Player> collection) {
      final var playersToSave = new ArrayList<Player>();
      collection.forEach(p -> {
         if (playerRepository.existsByUniquePropertyOfPlayer(p.getUniquePropertyOfPlayer())) {
            Optional.ofNullable(p.getBirthDate()).ifPresent(b -> p.setBirthDate(b.plusDays(1)));
            p.setPlayerName(TextUtil.normalizeName(p.getPlayerName()));
            playersToSave.add(p);
         }
      });
      return playerRepository.saveAll(playersToSave);
   }

   @Transactional(propagation = Propagation.REQUIRES_NEW)
   public Player save(final Player player) {
      if (playerRepository.existsByUniquePropertyOfPlayer(player.getUniquePropertyOfPlayer())) {
         Optional.ofNullable(player.getBirthDate()).ifPresent(b -> player.setBirthDate(b.plusDays(1)));
         player.setPlayerName(TextUtil.normalizeName(player.getPlayerName()));
         final var p = playerRepository.save(player);
         log.info("Player with name {}, id {} and identification {} was registered", p.getPlayerName(), p.getId(), p.getUniquePropertyOfPlayer());
         return p;
      }
      log.info("Player already registered: {}", player);
      return player;
   }

   @Transactional(propagation = Propagation.REQUIRES_NEW)
   public Optional<Map<Player, PlayerExecution>> readPlayers(final Short seasonId, final Short teamId) {
      final var seasonOpt = seasonRepository.findById(seasonId);
      if (seasonOpt.isPresent()) {
         log.info("Tournament: {}", seasonOpt.get());
         final var teamIdOptional = seasonOpt.get().getTeams().stream().filter(teamId::equals).findFirst();
         if (teamIdOptional.isPresent()) {
            final var teamOptional = teamRepository.findById(teamIdOptional.get());
            if (teamOptional.isPresent()) {
               final var team = teamOptional.get();
               log.info("Team: {}", team);
               final var url = team.getUrl();
               try {
                  final var playersRead = ReadAufPage.read(url);
                  if (playersRead.isEmpty()) {
                     throw new IllegalArgumentException(STR."No players found for team \{teamId}");
                  }
                  log.info("Total players: {}", playersRead.size());
                  final var map = new LinkedHashMap<Player, PlayerExecution>();
                  final Execution execution = createExecution(teamId, seasonId);
                  playersRead.forEach(dto -> {
                     final var player = new Player(dto.playerUniqueIdentification(), dto.name());
                     Optional.ofNullable(dto.birthdate()).map(bd -> {
                        final var zoneId = clock.getZone();
                        return bd.atZoneSameInstant(zoneId).toOffsetDateTime();
                     }).ifPresent(player::setBirthDate);
                     final Player save = save(player);
                     map.put(save, createPlayersExecution(execution, seasonId, dto));
                  });
                  return Optional.of(map);
               } catch (IOException e) {
                  log.error(String.format("Could not get information from team with url: %s", url), e);
               }
            }
         }
      }
      return Optional.empty();
   }

   @Transactional(propagation = Propagation.REQUIRES_NEW)
   PlayerExecution createPlayersExecution(final Execution execution, final short seasonId, final ReadPlayerDTO dto) {
      final var executionPlayer = new PlayerExecutionPK(execution.getId(), seasonId, dto.playerUniqueIdentification());
      final var playerExecution = new PlayerExecution(executionPlayer);
      playerExecution.setTotalGoals(dto.totalGoals());
      playerExecution.setTotalMatches(dto.totalMatches());
      playerExecution.setTotalMinutes(dto.totalMinutes());
      return playerExecutionRepository.save(playerExecution);
   }

   @Transactional(propagation = Propagation.REQUIRES_NEW)
   Execution createExecution(final Short teamId, final short seasonId) {
      final var execution = new Execution(Instant.now().atZone(clock.getZone()).toOffsetDateTime(), teamId);
      teamLastOpponentRepository.findTeamLastOpponentByIdAAndSeasonId(seasonId, teamId).ifPresent(x -> execution.setTeamLastOpponentId(x.getOpponentId()));
      return executionRepository.save(execution);
   }

   @Transactional(propagation = Propagation.REQUIRES_NEW)
   public Optional<List<Map<Player, PlayerExecution>>> readPlayersInAllTeams(final short seasonId) {
      final var seasonOptional = seasonRepository.findById(seasonId);
      if (seasonOptional.isPresent()) {
         final var list = new LinkedList<Map<Player, PlayerExecution>>();
         seasonOptional.get().getTeams().forEach(teamId -> {
            readPlayers(seasonId, teamId).ifPresent(list::add);
         });
         return Optional.of(list);
      }
      log.warn("Season {} not found", seasonId);
      return Optional.empty();
   }
}
