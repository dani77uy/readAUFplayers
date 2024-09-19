package com.tipsuy.readaufplayers.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfig {

   @Bean
   public Clock utcClock() {
      return Clock.systemUTC();
   }

   @Bean
   public ObjectMapper objectMapper() {
     return JsonMapper.builder()
          .addModule(new JavaTimeModule())
          .build();
   }
}
