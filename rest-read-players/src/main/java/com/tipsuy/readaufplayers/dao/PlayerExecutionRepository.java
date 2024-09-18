package com.tipsuy.readaufplayers.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tipsuy.readaufplayers.domain.PlayerExecutionDifference;

@Repository
public interface PlayerExecutionRepository extends MongoRepository<PlayerExecutionDifference, String> {

}
