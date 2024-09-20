package com.tipsuy.readaufplayers.domain.pk;

import java.io.Serializable;

public record MatchPlayerPK(short teamId, long playerId) implements Serializable {

}
