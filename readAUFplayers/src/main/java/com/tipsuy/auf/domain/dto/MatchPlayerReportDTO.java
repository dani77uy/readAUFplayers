package com.tipsuy.auf.domain.dto;

import java.io.Serializable;

record MatchPlayerReportDTO(String name, byte minutesPlayed, byte goalsConverted)  implements Serializable {

}
