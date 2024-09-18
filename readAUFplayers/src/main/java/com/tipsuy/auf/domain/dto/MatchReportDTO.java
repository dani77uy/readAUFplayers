package com.tipsuy.auf.domain.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

public record MatchReportDTO(String home, String away, LocalDateTime dateTime, byte homeGoals, byte awayGoals, Set<MatchPlayerReportDTO> players) implements Serializable {

}
