package com.tipsuy.readaufplayers.domain.dto;

import java.io.Serializable;

import org.springframework.lang.Nullable;

public record MatchDTO(short homeTeam, short awayTeam, int date, int time, short season, byte matchDay,
                       @Nullable String stadium, @Nullable String city) implements Serializable {

}
