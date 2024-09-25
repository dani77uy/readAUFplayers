package com.tipsuy.readaufplayers.dao;

import com.tipsuy.readaufplayers.domain.Match;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends MongoRepository<Match, Long> {

}
