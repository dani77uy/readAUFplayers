package com.tipsuy.readaufplayers.domain.dto;

import java.time.OffsetDateTime;

public record ReadPlayerDTO(byte totalMatches, byte totalGoals, short totalMinutes, OffsetDateTime birthdate, String name) {

}
