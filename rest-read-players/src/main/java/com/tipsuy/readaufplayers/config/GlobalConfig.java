package com.tipsuy.readaufplayers.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfig {

   @Bean
   public Clock utcClock() {
      return Clock.systemUTC();
   }
}
