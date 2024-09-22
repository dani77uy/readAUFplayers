package com.tipsuy.readaufplayers.domain.pk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tipsuy.readaufplayers.domain.Player;
import com.tipsuy.readaufplayers.domain.Team;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TeamPlayer(Team team, Player player) implements Serializable {

}
