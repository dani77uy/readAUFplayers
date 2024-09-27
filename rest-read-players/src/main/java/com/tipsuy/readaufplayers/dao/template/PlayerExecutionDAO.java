package com.tipsuy.readaufplayers.dao.template;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.tipsuy.readaufplayers.domain.Execution;
import com.tipsuy.readaufplayers.domain.PlayerExecution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class PlayerExecutionDAO {

   private final MongoTemplate mongoTemplate;

   public List<PlayerExecution> getPlayerAssociatedExecutions(final List<Execution> executions) {
      final var lookupOperation = LookupOperation.newLookup()
            .from("player-execution")
            .localField("id.executionId")
            .foreignField("id")
            .as("playerExecutionInfo");
      final var criteria = Criteria.where("id.executionId").in(executions.stream().map(Execution::getId));
      final var aggregation = Aggregation.newAggregation(Aggregation.match(criteria), lookupOperation);
      return mongoTemplate.aggregate(aggregation, "playerExecution", PlayerExecution.class).getMappedResults();
   }
}
