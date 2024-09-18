package com.tipsuy.auf.domain.model;

import java.io.Serializable;

public record MatchPlayer(Match match, Player player, byte minutesPlayed, byte goalsConverted, boolean wasStarter, boolean wasSubstitute, boolean wasNotPresent) implements
      Serializable {

}
