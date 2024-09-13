package com.tipsuy.auf.domain;

import java.io.Serializable;

public record MatchPlayer(Match match, Player player, byte minutesPlayed, byte goalsConverted, boolean wasStarter, boolean wasSubstitute, boolean wasNotPresent) implements
      Serializable {

}
