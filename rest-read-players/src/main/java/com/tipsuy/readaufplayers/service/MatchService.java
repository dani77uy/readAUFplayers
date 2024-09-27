package com.tipsuy.readaufplayers.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.tipsuy.readaufplayers.dao.ExecutionRepository;
import com.tipsuy.readaufplayers.dao.MatchRepository;
import com.tipsuy.readaufplayers.dao.SeasonRepository;
import com.tipsuy.readaufplayers.dao.template.MatchDAO;
import com.tipsuy.readaufplayers.dao.template.PlayerExecutionDAO;
import com.tipsuy.readaufplayers.domain.Execution;
import com.tipsuy.readaufplayers.domain.Match;
import com.tipsuy.readaufplayers.domain.MatchPlayer;
import com.tipsuy.readaufplayers.domain.PlayerExecution;
import com.tipsuy.readaufplayers.domain.dto.MatchDTO;
import com.tipsuy.readaufplayers.domain.pk.MatchPlayerPK;
import com.tipsuy.readaufplayers.util.DateUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchService {

   private final MatchRepository matchRepository;

   private final SeasonRepository seasonRepository;

   private final MatchDAO matchDAO;

   private final ExecutionRepository executionRepository;

   private final PlayerExecutionDAO playerExecutionDAO;

   public Match createMatch(final MatchDTO matchDTO) {
      final var match = new Match();
      match.setMatchDay(matchDTO.matchDay());
      match.setHomeTeam(matchDTO.homeTeam());
      match.setAwayTeam(matchDTO.awayTeam());
      match.setMatchDateTime(DateUtil.toOffsetDateTime(matchDTO.date(), matchDTO.time()));
      match.setCity(matchDTO.city());
      match.setStadium(matchDTO.stadium());
      final var matchAdded = matchRepository.save(match);
      matchDAO.addMatchToSeason(matchAdded.getMatchId(), matchDTO.season());
      // TODO check this
      return matchAdded;
   }

   public Match addGoalsToMatch(final long matchId, final byte homeGoals, final byte awayGoals) {
      return matchDAO.addMatchGoals(matchId, homeGoals, awayGoals);
   }

   public List<Match> addMatchesPlayers(final short seasonId, final byte matchDay) {
      // leo los partidos de la jornada por temporada
      final var matchesList = matchDAO.findMatchesBySeasonAndMatchDay(seasonId, matchDay);
      // creo una lista vacía de partidos donde se adicionarán jugadores
      final var matchesUpdated = new ArrayList<Match>(matchesList.size());
      matchesList.forEach(match -> {
         // recorro los partidos de la jornada
         final var matchDateTime = match.getMatchDateTime();
         final var homeTeam = match.getHomeTeam();
         final var awayTeam = match.getAwayTeam();
         if (matchDay == NumberUtils.BYTE_ONE) {
            //TODO
         } else {
            final List<Match> lastMatchDayMatches = matchDAO.findMatchesBySeasonAndMatchDay(seasonId, (byte) (matchDay - NumberUtils.BYTE_ONE));
            // busco cuándo fue el último partido del local, anterior al partido actual en esta temporada
            final var lastMatchDateTimeOfHomeTeam = lastMatchDayMatches.stream().filter(x -> x.getHomeTeam().equals(homeTeam)).findFirst().map(Match::getMatchDateTime).orElseThrow();
            // busco cuándo fue el último partido del visitante, anterior al partido actual en esta temporada
            final var lastMatchDateTimeOfAwayTeam = lastMatchDayMatches.stream().filter(x -> x.getHomeTeam().equals(awayTeam)).findFirst().map(Match::getMatchDateTime).orElseThrow();
            // busco la lista de ejecuciones entre el partido actual y el anterior para el local
            final List<Execution> executionsHomeTeam = getAllExecutionsBetweenDates(matchDateTime, lastMatchDateTimeOfHomeTeam);
            // busco la lista de ejecuciones entre el partido actual y el anterior para el visitante
            final List<Execution> executionsAwayTeam = getAllExecutionsBetweenDates(matchDateTime, lastMatchDateTimeOfAwayTeam);
            // busco la info de jugadores en esas ejecuciones, para el local
            final List<PlayerExecution> playersExecutionHomeTeam = getPlayerExecutions(executionsHomeTeam);
            // busco la info de jugadores en esas ejecuciones, para el visitante
            final List<PlayerExecution> playersExecutionAwayTeam = getPlayerExecutions(executionsAwayTeam);
            // calculo la diferencia de partidos, goles y minutos a cada jugador que estuvo involucrado en las ejecuciones
            match.addMatchPlayers(calculateMatchPlayers(homeTeam, awayTeam, playersExecutionHomeTeam, playersExecutionAwayTeam));
            // actualizo la base de datos con los jugadores del partido
            matchesUpdated.add(matchDAO.addMatchPlayers(match.getMatchId(), match.getMatchPlayers()));
         }

      });
      return matchesUpdated;
   }

   private List<MatchPlayer> calculateMatchPlayers(final short homeTeamId, final short awayTeamId,
         final List<PlayerExecution> playerExecutionHomeTeamList, final List<PlayerExecution> playerExecutionAwayTeamList) {
      final var list = new ArrayList<MatchPlayer>();
      list.addAll(analyzePlayerStatics(homeTeamId, playerExecutionHomeTeamList));
      list.addAll(analyzePlayerStatics(awayTeamId, playerExecutionAwayTeamList));
      return list;
   }

   private List<MatchPlayer> analyzePlayerStatics(final short teamId, final List<PlayerExecution> playerExecutionList) {
      final var previousExecutionByPlayer = new HashMap<String, PlayerExecution>();
      final var list = new ArrayList<MatchPlayer>();
      for (var currentExecution : playerExecutionList) {
         final var playerId = currentExecution.getPlayerExecutionPk().playerUniqueIdentification();
         final var previousExecution = previousExecutionByPlayer.get(playerId);

         if (previousExecution != null) {
            final var matchesDiff = currentExecution.getTotalMatches() - previousExecution.getTotalMatches();
            final var minutesDiff = currentExecution.getTotalMinutes() - previousExecution.getTotalMinutes();
            final var goalsDiff = currentExecution.getTotalGoals() - previousExecution.getTotalGoals();

            if (matchesDiff > 0) {
               log.info("Player {} played in {} new match(es).", playerId, matchesDiff);
               log.info("Minutes played: {}", minutesDiff);
               log.info("Goals scored: {}", goalsDiff);
               final var matchPlayerPK = new MatchPlayerPK(teamId, playerId);
               final var matchPlayer = new MatchPlayer();
               matchPlayer.setMatchPlayerPK(matchPlayerPK);
               matchPlayer.setMinutesPlayed((byte) minutesDiff);
               matchPlayer.setGoals((byte) goalsDiff);
               list.add(matchPlayer);
            }
         }
         previousExecutionByPlayer.put(playerId, currentExecution);
      }
      return list;
   }


   private List<PlayerExecution> getPlayerExecutions(final List<Execution> executionList) {
      return playerExecutionDAO.getPlayerAssociatedExecutions(executionList);
   }

   private List<Execution> getAllExecutionsBetweenDates(@NonNull final OffsetDateTime fromDateTime, @NonNull final OffsetDateTime toDateTime) {
      return executionRepository.getAllExecutionsBetweenDates(fromDateTime, toDateTime);
   }

   private Match addMatchPlayers(final long matchId, final List<MatchPlayer> matchPlayers) {
      return matchDAO.addMatchPlayers(matchId, matchPlayers);
   }

   public List<Match> saveAll(final Iterable<Match> matchList) {
      return matchRepository.saveAll(matchList);
   }

   public Match addPlayersToMatch(final long matchId, final List<MatchPlayer> matchPlayerList) {
      final var matchOptional = matchRepository.findById(matchId);
      if (matchOptional.isPresent()) {
         final var match = matchOptional.get();
         match.addMatchPlayers(matchPlayerList);
         return matchRepository.save(match);
      }
      throw new IllegalArgumentException("Match not found");
   }
}
