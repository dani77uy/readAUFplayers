package com.tipsuy.readaufplayers.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tipsuy.readaufplayers.dao.MatchRepository;
import com.tipsuy.readaufplayers.dao.SeasonRepository;
import com.tipsuy.readaufplayers.dao.template.MatchDAO;
import com.tipsuy.readaufplayers.domain.Execution;
import com.tipsuy.readaufplayers.domain.Match;
import com.tipsuy.readaufplayers.domain.MatchPlayer;
import com.tipsuy.readaufplayers.domain.PlayerExecution;
import com.tipsuy.readaufplayers.domain.dto.MatchDTO;
import com.tipsuy.readaufplayers.util.DateUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MatchService {

   private final MatchRepository matchRepository;

   private final SeasonRepository seasonRepository;

   private final MatchDAO matchDAO;

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
   }

   public Match addGoalsToMatch(final long matchId, final byte homeGoals, final byte awayGoals) {
      return matchDAO.addMatchGoals(matchId, homeGoals, awayGoals);
   }

   public List<Match> addMatchesPlayers(final short seasonId, final byte matchDay) {
      final var matchesList = matchDAO.findMatchesBySeasonAndMatchDay(seasonId, matchDay);
      final var matchesUpdated = new ArrayList<Match>(matchesList.size());
      matchesList.forEach(match -> {
         final var matchDateTime = match.getMatchDateTime();
         final var homeTeam = match.getHomeTeam();
         final var awayTeam = match.getAwayTeam();
         final Match lastMatchOfHomeTeam = getTheLatestMatchOfTeam(homeTeam);
         final Match lastMatchOfAwayTeam = getTheLatestMatchOfTeam(awayTeam);
         final List<Execution> executionsHomeTeam = getAllExecutionsBetweenDates(matchDateTime, lastMatchOfHomeTeam.getMatchDateTime());
         final List<Execution> executionsAwayTeam = getAllExecutionsBetweenDates(matchDateTime, lastMatchOfAwayTeam.getMatchDateTime());
         final List<PlayerExecution> playersExecutionHomeTeam = getPlayerExecutions(executionsHomeTeam);
         final List<PlayerExecution> playersExecutionAwayTeam = getPlayerExecutions(executionsAwayTeam);
         match.addMatchPlayers(calculateMatchPlayers(playersExecutionHomeTeam, playersExecutionAwayTeam));
         matchesUpdated.add(matchDAO.addMatchPlayers(match.getMatchId(), match.getMatchPlayers()));
      });
      return matchesUpdated;
   }

   private Match getTheLatestMatchOfTeam(final Short teamId) {
      final Iterable<Match> latestMatches = matchRepository.findByMatchesFromTeam(teamId);
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
