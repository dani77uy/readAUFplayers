package com.tipsuy.auf.domain;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.NonNull;

public record Player(Integer id, @NonNull String name, LocalDate birthDate, int matchesPlayed, int minutesPlayed, int goalsScored) implements Serializable {

}
