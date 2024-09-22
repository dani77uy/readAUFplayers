package com.tipsuy.readaufplayers.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tipsuy.readaufplayers.domain.Match;
import com.tipsuy.readaufplayers.domain.serializer.MatchDeserializer;
import com.tipsuy.readaufplayers.domain.serializer.Modifier;
import java.time.Clock;
import java.time.ZoneId;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfig {

   private final String timeZone;

   public GlobalConfig(@Value("${spring.jackson.time-zone:America/Montevideo}") final String timeZone) {
      this.timeZone = timeZone;
   }

   @Bean
   public Clock clock() {
      return Clock.system(ZoneId.of(timeZone));
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
      simpleModuleSerializer.setSerializerModifier(new Modifier(clock()));
      simpleModuleSerializer.addDeserializer(Match.class, new MatchDeserializer(clock()));
      objectMapper.registerModule(simpleModuleSerializer);
   }
}
