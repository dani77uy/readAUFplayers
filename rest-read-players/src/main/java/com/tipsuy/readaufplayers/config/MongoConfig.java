package com.tipsuy.readaufplayers.config;

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
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableMongoRepositories(basePackages = {"com.tipsuy.readaufplayers.dao"})
@EnableMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class MongoConfig extends AbstractMongoClientConfiguration {

  public static final String DATABASE = "read-auf-players";

  private static final String connectionString = "mongodb+srv://uruguay:Daniel2024@cluster-rap.tygtk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster-RAP";

  private static MongoClientSettings buildMongoClientSettings() {
    final var codecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()),
        CodecRegistries.fromCodecs(new OffsetDateTimeCodec()));
    ServerApi serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();
    return MongoClientSettings.builder().applyConnectionString(new ConnectionString(connectionString)).serverApi(serverApi).codecRegistry(codecRegistry).build();
  }

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
