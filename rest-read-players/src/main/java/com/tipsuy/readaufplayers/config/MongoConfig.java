package com.tipsuy.readaufplayers.config;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ReadConcern;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableMongoRepositories(basePackages = { "com.tipsuy.readaufplayers.dao" })
@EnableMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class MongoConfig extends AbstractMongoClientConfiguration  {

   private static final String connectionString = "mongodb+srv://uruguay:Daniel2024@cluster-rap.tygtk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster-RAP";

   public static final String DATABASE = "read-auf-players";

   @Bean
   @Override
   public MongoClient mongoClient() {
      final var mongoClient = MongoClients.create(buildMongoClientSettings());
      try {
         final var database = mongoClient.getDatabase(DATABASE);
         final var command = new BsonDocument("dbStats", new BsonInt64(1));
         final var commandResult = database.runCommand(command);
         log.info("dbStats: {}", commandResult.toJson());
         return mongoClient;
      } catch (MongoException e) {
         throw new IllegalArgumentException(e);
      }
   }

   private static MongoClientSettings buildMongoClientSettings() {
      ServerApi serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();
      return MongoClientSettings.builder().applyConnectionString(new ConnectionString(connectionString)).serverApi(serverApi).build();
   }

   @Bean
   public DateTimeProvider dateTimeProvider(final Clock clock) {
      return () -> Optional.of(Instant.now(clock).truncatedTo(ChronoUnit.MILLIS));
   }

   @Bean
   public MongoTransactionManager transactionManager(final MongoDatabaseFactory mongoDbFactory) {
      final var transactionOptions = TransactionOptions.builder().readConcern(ReadConcern.LOCAL).writeConcern(WriteConcern.W1).build();
      return new MongoTransactionManager(mongoDbFactory, transactionOptions);
   }

   @Override
   protected String getDatabaseName() {
      return "read-auf-players";
   }
}
