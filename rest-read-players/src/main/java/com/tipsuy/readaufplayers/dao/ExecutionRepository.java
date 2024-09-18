package com.tipsuy.readaufplayers.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tipsuy.readaufplayers.domain.Execution;

public interface ExecutionRepository extends MongoRepository<Execution, Object> {

}
