package com.tipsuy.readaufplayers.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tipsuy.readaufplayers.domain.Player;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {

}
