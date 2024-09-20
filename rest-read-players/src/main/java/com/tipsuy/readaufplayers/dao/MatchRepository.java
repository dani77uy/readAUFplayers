package com.tipsuy.readaufplayers.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tipsuy.readaufplayers.domain.Match;

@Repository
public interface MatchRepository extends MongoRepository<Match, Long> {

}
