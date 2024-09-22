package com.tipsuy.readaufplayers.dao;

import com.tipsuy.readaufplayers.domain.Execution;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExecutionRepository extends MongoRepository<Execution, Object> {

}
