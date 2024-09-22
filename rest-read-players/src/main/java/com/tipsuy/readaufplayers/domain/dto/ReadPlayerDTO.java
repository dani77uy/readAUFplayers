package com.tipsuy.readaufplayers.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReadPlayerDTO(byte totalMatches, byte totalGoals, short totalMinutes, OffsetDateTime birthdate, String name, String playerUniqueIdentification) {

}
