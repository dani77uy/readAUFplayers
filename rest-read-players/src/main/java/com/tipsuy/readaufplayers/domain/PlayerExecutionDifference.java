package com.tipsuy.readaufplayers.domain;

import com.tipsuy.readaufplayers.domain.pk.ExecutionPlayer;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Document(collection = "player-execution-difference")
@Data
public class PlayerExecutionDifference implements Serializable {

   @Serial
   private static final long serialVersionUID = -7671747242050314774L;

   @Id @NonNull
   private ExecutionPlayer executionPlayer;

   private byte matchesDifference = 0;

   private byte goalsDifference = 0;

   private short minutesDifference = 0;

}
