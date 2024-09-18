package com.tipsuy.readaufplayers.domain;

import java.io.Serial;
import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Document(collection = "player-execution-difference")
@Data
public class PlayerExecutionDifference implements Serializable {

   @Serial
   private static final long serialVersionUID = -7671747242050314774L;

   @Id
   private String id = null;

   @NonNull
   private Execution execution;

   @NonNull
   private Player player;

   private byte matchesDifference = 0;

   private byte goalsDifference = 0;

   private short minutesDifference = 0;

}
