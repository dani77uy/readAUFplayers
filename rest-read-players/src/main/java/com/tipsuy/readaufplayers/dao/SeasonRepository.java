package com.tipsuy.readaufplayers.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tipsuy.readaufplayers.domain.Season;

@Repository
public interface SeasonRepository extends MongoRepository<Season, String> {

   Optional<Season> findByCustomSeasonId(Short customSeasonId);

}
