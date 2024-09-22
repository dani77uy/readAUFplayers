package com.tipsuy.readaufplayers.dao;

import com.tipsuy.readaufplayers.domain.Player;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {

  @Query("{'uniquePropertyOfPlayer' :  {$eq :  ?0}} ")
  Optional<Player> findByUniquePropertyOfPlayer(String uniqueProperty);

}
