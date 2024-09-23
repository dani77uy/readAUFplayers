package com.tipsuy.readaufplayers.dao;

import com.tipsuy.readaufplayers.domain.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {

  @Query(value = "{ 'uniquePropertyOfPlayer': ?0 }", exists = true)
  boolean existsByUniquePropertyOfPlayer(String uniquePropertyOfPlayer);
}
