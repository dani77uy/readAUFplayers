package com.tipsuy.readaufplayers.domain.projection;

import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Value;

public interface PlayerProjection {

  @Value("#{target.property}")
  String getUniquePropertyOfPlayer();
  @Value("#{target.playerName}")
  String getPlayerName();
  @Value("#{target.birthDate}")
  OffsetDateTime getPlayerBirthDate();
}
