package com.tipsuy.readaufplayers.domain.dto;

import java.time.LocalDate;

public record ReadPlayerDTO(byte totalMatches, byte totalGoals, short totalMinutes, LocalDate birthdate, String name) {

}
