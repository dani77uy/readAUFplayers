package com.tipsuy.readaufplayers.service;

import com.tipsuy.readaufplayers.domain.pk.ExecutionPlayer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tipsuy.readaufplayers.dao.ExecutionRepository;
import com.tipsuy.readaufplayers.dao.PlayerExecutionRepository;
import com.tipsuy.readaufplayers.dao.PlayerRepository;
import com.tipsuy.readaufplayers.dao.SeasonRepository;
import com.tipsuy.readaufplayers.domain.Execution;
import com.tipsuy.readaufplayers.domain.Player;
import com.tipsuy.readaufplayers.domain.PlayerExecutionDifference;
import com.tipsuy.readaufplayers.domain.Team;
import com.tipsuy.readaufplayers.domain.dto.ReadPlayerDTO;
import com.tipsuy.readaufplayers.util.ReadAufPage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlayerService {

   private final PlayerRepository playerRepository;

   private final SeasonRepository seasonRepository;

   private final ExecutionRepository executionRepository;

   private final PlayerExecutionRepository playerExecutionRepository;

   public Optional<Map<Player, PlayerExecutionDifference>> readPlayers(final Short seasonId, final Short customTeamId) {
      final var seasonOpt = seasonRepository.findById(seasonId);
      if (seasonOpt.isPresent()) {
         final var teamsOptional = seasonOpt.get().getTeams().stream().filter(x -> x.getTeamId().equals(customTeamId)).findFirst();
         if (teamsOptional.isPresent()) {
            final var team = teamsOptional.get();
            final var url = team.getUrl();
            try {
               final var playersRead = ReadAufPage.read(url);
               final var map = new LinkedHashMap<Player, PlayerExecutionDifference>();
               playersRead.forEach(dto -> {
                  final var player = new Player(dto.name());
                  Optional.ofNullable(dto.birthdate()).ifPresent(player::setBirthDate);
                  final Player save = playerRepository.save(player);
                  map.put(save, addExecution(team, player, dto));
               });
               return Optional.of(map);
            } catch (IOException e) {
               log.error(String.format("Could not get information from team with url: %s", url), e);
               return Optional.empty();
            }
         }
         return Optional.empty();
      }
      return Optional.empty();
   }

   private PlayerExecutionDifference addExecution(final Team team, final Player player, final ReadPlayerDTO dto) {
      final var execution = executionRepository.save(new Execution(LocalDateTime.now(), team));
      final var executionPlayer = new ExecutionPlayer(execution, player);
      final var playerExecution = new PlayerExecutionDifference(executionPlayer);
      playerExecution.setGoalsDifference(dto.totalGoals());
      playerExecution.setMatchesDifference(dto.totalMatches());
      playerExecution.setMinutesDifference(dto.totalMinutes());
      return playerExecutionRepository.save(playerExecution);
   }
}
