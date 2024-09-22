package com.tipsuy.readaufplayers.dao;

import com.tipsuy.readaufplayers.domain.TeamLastOpponent;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamLastOpponentRepository extends MongoRepository<TeamLastOpponent, Short> {

   @Query("{'teamId' :  {$eq :  ?1}, 'seasonId': {$eq: ?0}}")
   Optional<TeamLastOpponent> findTeamLastOpponentByIdAAndSeasonId(short seasonId, short teamId);
}
