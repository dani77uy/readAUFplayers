package com.tipsuy.readaufplayers.config;

import com.tipsuy.readaufplayers.domain.Player;
import com.tipsuy.readaufplayers.domain.serializer.PlayerDeserializer;
import java.time.Clock;
import java.time.ZoneId;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tipsuy.readaufplayers.domain.Match;
import com.tipsuy.readaufplayers.domain.pk.ExecutionPlayer;
import com.tipsuy.readaufplayers.domain.serializer.ExecutionPlayerDeserializer;
import com.tipsuy.readaufplayers.domain.serializer.MatchDeserializer;
import com.tipsuy.readaufplayers.domain.serializer.Modifier;

@Configuration
public class GlobalConfig {

   public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

   public static final String DATE_FORMAT = "yyyy-MM-dd";

   private final String timeZone;

   public GlobalConfig(@Value("${spring.jackson.time-zone:America/Montevideo}") final String timeZone) {
      this.timeZone = timeZone;
   }

   @Bean
   public Clock utcClock() {
      return Clock.systemUTC();
   }

   @Bean(name = "custom-object-mapper")
   public ObjectMapper objectMapper() {
      final var mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      mapper.setTimeZone(TimeZone.getTimeZone(ZoneId.of(timeZone)));
      addProviders(mapper);
      return mapper;
   }

   private void addProviders(final ObjectMapper objectMapper) {
      final var simpleModuleSerializer = new SimpleModule();
      simpleModuleSerializer.setSerializerModifier(new Modifier());
      simpleModuleSerializer.addDeserializer(ExecutionPlayer.class, new ExecutionPlayerDeserializer());
      simpleModuleSerializer.addDeserializer(Match.class, new MatchDeserializer(timeZone));
      simpleModuleSerializer.addDeserializer(Player.class, new PlayerDeserializer(timeZone));
      objectMapper.registerModule(simpleModuleSerializer);
   }
}
