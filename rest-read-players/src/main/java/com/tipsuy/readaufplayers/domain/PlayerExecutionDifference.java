package com.tipsuy.readaufplayers.domain;

import java.io.Serial;
import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tipsuy.readaufplayers.domain.pk.ExecutionPlayer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = false)
@Document(collection = "player-execution-difference")
@Data
public class PlayerExecutionDifference implements Serializable {

   @Serial
   private static final long serialVersionUID = -7671747242050314773L;

   @Id @NonNull
   private ExecutionPlayer executionPlayer;

   private byte totalMatches = 0;

   private byte totalGoals = 0;

   private short totalMinutes = 0;

   public PlayerExecutionDifference() {}

   @JsonCreator
   public PlayerExecutionDifference(@JsonProperty("executionPlayer") final ExecutionPlayer executionPlayer) {
      this.executionPlayer = executionPlayer;
   }

}
