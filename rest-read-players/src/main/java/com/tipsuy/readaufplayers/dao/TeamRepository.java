package com.tipsuy.readaufplayers.dao;

import com.tipsuy.readaufplayers.domain.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends MongoRepository<Team, Short> {

}
