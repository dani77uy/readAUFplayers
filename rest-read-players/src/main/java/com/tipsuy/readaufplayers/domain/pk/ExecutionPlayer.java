package com.tipsuy.readaufplayers.domain.pk;

import com.tipsuy.readaufplayers.domain.Execution;
import com.tipsuy.readaufplayers.domain.Player;
import java.io.Serial;
import java.io.Serializable;

public record ExecutionPlayer(Execution execution, Player player) implements Serializable {

  @Serial
  private static final long serialVersionUID = 7014264580092854736L;

}
