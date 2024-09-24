package com.tipsuy.readaufplayers.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tipsuy.readaufplayers.domain.Match;

@Repository
public interface MatchRepository extends MongoRepository<Match, Long> {

   @Query(value = "{ '$or': [ { 'homeTeam': ?0 }, { 'awayTeam': ?0 } ], {''} }", sort = "{'matchDateTime': 1}")
   Iterable<Match> findByMatchesFromTeam(Short teamId, Short seasonId);
}
