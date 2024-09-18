package com.tipsuy.auf.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record Match(long id, byte matchDay, Season season, LocalDateTime matchDateTime, Team homeTeam, byte goalsHome, Team awayTeam, byte goalsAway, List<MatchPlayer> playerList)
      implements Serializable {

}
