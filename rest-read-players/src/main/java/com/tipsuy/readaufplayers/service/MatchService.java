package com.tipsuy.readaufplayers.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tipsuy.readaufplayers.dao.MatchRepository;
import com.tipsuy.readaufplayers.domain.Match;
import com.tipsuy.readaufplayers.domain.MatchPlayer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MatchService {

   private final MatchRepository matchRepository;

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
