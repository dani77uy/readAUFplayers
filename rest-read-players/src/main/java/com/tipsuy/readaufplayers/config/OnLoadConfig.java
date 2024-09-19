package com.tipsuy.readaufplayers.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tipsuy.readaufplayers.dao.SeasonRepository;
import com.tipsuy.readaufplayers.domain.Season;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.ResourceUtils;

@RequiredArgsConstructor
@Configuration
public class OnLoadConfig implements InitializingBean {

   private static final String dataPath = "./init-data/";

   private final ObjectMapper objectMapper;

   private final SeasonRepository seasonRepository;

   private final MongoTemplate mongoTemplate;

   @Override
   public void afterPropertiesSet() throws Exception {
      loadSeasons();
   }

   private void loadSequences() {
      mongoTemplate.createCollection("counters");
      final var matchDocumentoSeq = new Document("_id", "matches").append("seq", 7);
      final var playerDocumentoSeq = new Document("_id", "players").append("seq", 1);
      final var teamDocumentoSeq = new Document("_id", "teams").append("seq", 13);
      final var seasonDocumentoSeq = new Document("_id", "seasons").append("seq", 2);
      final var executionDocumentoSeq = new Document("_id", "executions").append("seq", 1);
      mongoTemplate.getCollection("counters").insertOne(matchDocumentoSeq);
      mongoTemplate.getCollection("counters").insertOne(playerDocumentoSeq);
      mongoTemplate.getCollection("counters").insertOne(teamDocumentoSeq);
      mongoTemplate.getCollection("counters").insertOne(seasonDocumentoSeq);
      mongoTemplate.getCollection("counters").insertOne(executionDocumentoSeq);
   }

   private void loadSeasons() throws IOException {
      final var jsonFile = ResourceUtils.getFile(dataPath + "seasons.json");
      final var seasons = objectMapper.readValue(jsonFile, new TypeReference<List<Season>>() {
      });
      seasonRepository.saveAll(seasons);
   }
}
