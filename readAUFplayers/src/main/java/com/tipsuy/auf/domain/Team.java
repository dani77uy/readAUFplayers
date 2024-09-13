package com.tipsuy.auf.domain;

import java.io.Serializable;
import java.util.List;

import lombok.NonNull;

public record Team(short id, @NonNull String name, @NonNull String url, List<Player> players) implements Serializable {

}
