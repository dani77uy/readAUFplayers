package com.tipsuy.readaufplayers.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tipsuy.readaufplayers.dao.ExecutionRepository;
import com.tipsuy.readaufplayers.dao.PlayerExecutionRepository;
import com.tipsuy.readaufplayers.dao.PlayerRepository;
import com.tipsuy.readaufplayers.dao.SeasonRepository;
import com.tipsuy.readaufplayers.dao.SequenceRepository;
import com.tipsuy.readaufplayers.dao.TeamLastOpponentRepository;
import com.tipsuy.readaufplayers.dao.TeamRepository;
import com.tipsuy.readaufplayers.domain.Execution;
import com.tipsuy.readaufplayers.domain.Player;
import com.tipsuy.readaufplayers.domain.PlayerExecutionDifference;
import com.tipsuy.readaufplayers.domain.dto.ReadPlayerDTO;
import com.tipsuy.readaufplayers.domain.pk.ExecutionPlayer;
import com.tipsuy.readaufplayers.util.ReadAufPage;
import com.tipsuy.readaufplayers.util.TextUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

   private final SequenceRepository sequenceRepository;

   public List<Player> saveAll(final Iterable<Player> collection) {
      collection.forEach(p -> {
         final long playerId;
         if (p.getPlayerId() == null) {
            playerId = sequenceRepository.getNextSequenceValue("players");
         } else {
            playerId = p.getPlayerId();
         }
         p.setPlayerId(playerId);
         Optional.ofNullable(p.getBirthDate()).ifPresent(b -> p.setBirthDate(b.plusDays(1)));
         p.setPlayerName(TextUtil.normalizeName(p.getPlayerName()));
      });
      return playerRepository.saveAll(collection);
   }

   public Player save(final Player player) {
      final long playerId;
      if (player.getPlayerId() == null) {
         playerId = sequenceRepository.getNextSequenceValue("players");
      } else {
         playerId = player.getPlayerId();
      }
      player.setPlayerId(playerId);
      Optional.ofNullable(player.getBirthDate()).ifPresent(b -> player.setBirthDate(b.plusDays(1)));
      player.setPlayerName(TextUtil.normalizeName(player.getPlayerName()));
      return playerRepository.save(player);
   }

   public Optional<Map<Player, PlayerExecutionDifference>> readPlayers(final Short seasonId, final Short teamId) {
      final var seasonOpt = seasonRepository.findById(seasonId);
      if (seasonOpt.isPresent()) {
         final var teamIdOptional = seasonOpt.get().getTeams().stream().filter(teamId::equals).findFirst();
         if (teamIdOptional.isPresent()) {
            final var teamOptional = teamRepository.findById(teamIdOptional.get());
            if (teamOptional.isPresent()) {
               final var team = teamOptional.get();
               final var url = team.getUrl();
               try {
                  final var playersRead = ReadAufPage.read(url);
                  final var map = new LinkedHashMap<Player, PlayerExecutionDifference>();
                  playersRead.forEach(dto -> {
                     final var player = new Player(dto.name());
                     Optional.ofNullable(dto.birthdate()).ifPresent(player::setBirthDate);
                     final Player save = save(player);
                     map.put(save, addExecution(seasonId, team.getTeamId(), player.getPlayerId(), dto));
                  });
                  return Optional.of(map);
               } catch (IOException e) {
                  log.error(String.format("Could not get information from team with url: %s", url), e);
                  return Optional.empty();
               }
            }
         }
      }
      return Optional.empty();
   }

   private PlayerExecutionDifference addExecution(final short seasonId, final short teamId, final long playerId, final ReadPlayerDTO dto) {
      final var execution = new Execution(LocalDateTime.now(), teamId);
      final var teamLastOpponentOptional = teamLastOpponentRepository.findTeamLastOpponentByIdAAndSeasonId(seasonId, teamId);
      if (teamLastOpponentOptional.isPresent()) {
         final var teamLastOpponent = teamLastOpponentOptional.get();
         execution.setTeamLastOpponentId(teamLastOpponent.getOpponentId());
         final var executionSaved = executionRepository.save(execution);
         final var executionPlayer = new ExecutionPlayer(executionSaved.getId(), playerId);
         final var playerExecution = new PlayerExecutionDifference(executionPlayer);
         playerExecution.setTotalGoals(dto.totalGoals());
         playerExecution.setTotalMatches(dto.totalMatches());
         playerExecution.setTotalMinutes(dto.totalMinutes());
         return playerExecutionRepository.save(playerExecution);
      } else {
         final var executionSaved = executionRepository.save(execution);
         final var executionPlayer = new ExecutionPlayer(executionSaved.getId(), playerId);
         final var playerExecution = new PlayerExecutionDifference(executionPlayer);
         return playerExecutionRepository.save(playerExecution);
      }
   }
}
