package com.tipsuy.readaufplayers.config;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tipsuy.readaufplayers.domain.Match;
import com.tipsuy.readaufplayers.domain.Player;
import com.tipsuy.readaufplayers.service.MatchService;
import com.tipsuy.readaufplayers.service.PlayerService;

@Configuration
public class OnLoadConfig implements InitializingBean {

   private static final String dataPath = "classpath:./init-data/";

   private final ObjectMapper objectMapper;

   private final MongoTemplate mongoTemplate;

   private final PlayerService playerService;

   private final MatchService matchService;

   public OnLoadConfig(@Qualifier("custom-object-mapper") final ObjectMapper objectMapper, final MongoTemplate mongoTemplate, final PlayerService playerService,
         final MatchService matchService) {
      this.objectMapper = objectMapper;
      this.mongoTemplate = mongoTemplate;
      this.playerService = playerService;
      this.matchService = matchService;
   }

   @Override
   public void afterPropertiesSet() throws Exception {
//      loadSequences();
//      load("seasons.json", new TypeReference<List<Season>>() {});
//      load("teams.json", new TypeReference<List<Team>>() {});
  //    loadMatches();
//      load("teams-opponents.json", new TypeReference<List<TeamLastOpponent>>() {});
//      load("executions.json", new TypeReference<List<Execution>>() {});
      loadPlayers();
//      load("players-executions.json", new TypeReference<List<PlayerExecutionDifference>>() {});
   }

   private void loadSequences() {
      mongoTemplate.createCollection("counters");
      final var matchDocumentoSeq = new Document("_id", "matches").append("seq", 13);
      final var playerDocumentoSeq = new Document("_id", "players").append("seq", 253);
      final var teamDocumentoSeq = new Document("_id", "teams").append("seq", 13);
      final var seasonDocumentoSeq = new Document("_id", "seasons").append("seq", 2);
      mongoTemplate.getCollection("counters").insertOne(matchDocumentoSeq);
      mongoTemplate.getCollection("counters").insertOne(playerDocumentoSeq);
      mongoTemplate.getCollection("counters").insertOne(teamDocumentoSeq);
      mongoTemplate.getCollection("counters").insertOne(seasonDocumentoSeq);
   }

   private <X extends Serializable, T extends TypeReference<List<X>>> void load(final String jsonName, final T typeReference) throws IOException {
      final var jsonFile = ResourceUtils.getFile(dataPath + jsonName);
      final var collection = objectMapper.readValue(jsonFile, typeReference);
      mongoTemplate.insertAll(collection);
   }

   private void loadPlayers() throws IOException {
      final var jsonFile = ResourceUtils.getFile(STR."\{dataPath}players.json");
      final var collection = objectMapper.readValue(jsonFile, new TypeReference<List<Player>>() {});
      playerService.saveAll(collection);
   }

   private void loadMatches() throws IOException {
      final var jsonFile = ResourceUtils.getFile(STR."\{dataPath}matches.json");
      final var collection = objectMapper.readValue(jsonFile, new TypeReference<List<Match>>() {});
      matchService.saveAll(collection);
   }

}
