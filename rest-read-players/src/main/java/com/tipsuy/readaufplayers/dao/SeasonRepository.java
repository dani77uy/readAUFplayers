package com.tipsuy.readaufplayers.dao;

import com.tipsuy.readaufplayers.domain.Season;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends MongoRepository<Season, Short> {

}
