package com.tipsuy.readaufplayers.config;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class MongoConfig  {

   @Bean
   public DateTimeProvider dateTimeProvider(final Clock clock) {
      return () -> Optional.of(Instant.now(clock).truncatedTo(ChronoUnit.MILLIS));
   }

}
