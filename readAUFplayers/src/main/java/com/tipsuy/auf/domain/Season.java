package com.tipsuy.auf.domain;

import java.io.Serializable;
import lombok.NonNull;

public record Season(byte id, short year, @NonNull String name) implements Serializable {

}
