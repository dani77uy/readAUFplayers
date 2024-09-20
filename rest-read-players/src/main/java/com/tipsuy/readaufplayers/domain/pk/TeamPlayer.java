package com.tipsuy.readaufplayers.domain.pk;

import java.io.Serial;
import java.io.Serializable;

import com.tipsuy.readaufplayers.domain.Player;
import com.tipsuy.readaufplayers.domain.Team;

public record TeamPlayer(Team team, Player player) implements Serializable {

  @Serial
  private static final long serialVersionUID = 471964637067332052L;

}
