package com.tipsuy.readaufplayers.dao;

import java.time.OffsetDateTime;
import java.util.List;

import com.tipsuy.readaufplayers.domain.Execution;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ExecutionRepository extends MongoRepository<Execution, String> {

   @Query("{'executionDateTime':  {$gte: ?0, $lte:  ?1}}")
   List<Execution> getAllExecutionsBetweenDates(OffsetDateTime fromDateTime, OffsetDateTime toDateTime);
}
