package com.tipsuy.readaufplayers.dao.template;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.tipsuy.readaufplayers.domain.Match;
import com.tipsuy.readaufplayers.domain.MatchPlayer;
import com.tipsuy.readaufplayers.domain.Season;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class MatchDAO {

   private final MongoTemplate mongoTemplate;

   public Match addMatchGoals(final long matchId, final byte homeGoals, final byte awayGoals) {
      final var query = new Query(Criteria.where("matchId").is(matchId));
      final var update = new Update().set("homeGoals", homeGoals).set("awayGoals", awayGoals);
      return mongoTemplate.findAndModify(query, update, Match.class);
   }

   public Match addMatchPlayers(final long matchId, final List<MatchPlayer> matchPlayerList) {
      final var query = new Query(Criteria.where("matchId").is(matchId));
      final var update = new Update().set("matchPlayers", matchPlayerList);
      return mongoTemplate.findAndModify(query, update, Match.class);
   }

   public void addMatchToSeason(final long matchId, final short seasonId) {
      final var query = new Query(Criteria.where("id").is(seasonId));
      final var update = new Update().addToSet("matches", matchId);
      final var result = mongoTemplate.updateFirst(query, update, Season.class);
      log.info("Added count of {} matches to season: {}", result.getMatchedCount(), seasonId);
   }

   public List<Match> findMatchesBySeasonAndMatchDay(final short seasonId, final byte matchDay) {
      final var aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("id").is(seasonId)),
            Aggregation.unwind("matches"),
            Aggregation.lookup("match", "matches", "matchId", "matchDetails"),
            Aggregation.unwind("matchDetails"),
            Aggregation.match(Criteria.where("matchDetails.matchDay").is(matchDay)),
            Aggregation.replaceRoot("matchDetails")
      );
      final var aggregationResult = mongoTemplate.aggregate(aggregation, "matches", Match.class);
      return aggregationResult.getMappedResults();
   }

}
