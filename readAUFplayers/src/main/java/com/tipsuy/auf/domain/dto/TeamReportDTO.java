package com.tipsuy.auf.domain.dto;

import java.io.Serializable;
import java.util.Set;

import com.tipsuy.auf.domain.model.Player;

public record TeamReportDTO(String name, Set<Player> playerList, String season) implements Serializable {

}
