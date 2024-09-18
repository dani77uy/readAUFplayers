package com.tipsuy.readaufplayers.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tipsuy.readaufplayers.domain.Team;

@Repository
public interface TeamRepository extends MongoRepository<Team, String> {

}
