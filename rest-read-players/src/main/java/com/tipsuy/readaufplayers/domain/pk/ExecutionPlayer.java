package com.tipsuy.readaufplayers.domain.pk;

import java.io.Serial;
import java.io.Serializable;

public record ExecutionPlayer(String executionId, Long playerId) implements Serializable {

  @Serial
  private static final long serialVersionUID = 7014264580092854741L;

}
