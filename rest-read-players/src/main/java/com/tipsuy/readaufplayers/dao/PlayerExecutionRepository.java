package com.tipsuy.readaufplayers.dao;

import com.tipsuy.readaufplayers.domain.PlayerExecution;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerExecutionRepository extends MongoRepository<PlayerExecution, String> {

}
