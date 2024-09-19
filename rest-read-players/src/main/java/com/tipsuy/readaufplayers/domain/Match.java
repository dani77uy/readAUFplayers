package com.tipsuy.readaufplayers.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Document(collection = "matches")
@Data
public class Match implements Serializable {

   @Serial
   private static final long serialVersionUID = -1743047731326953216L;

   @Id
   private Long matchId;

   @NonNull
   private Team homeTeam;

   @NonNull
   private Team awayTeam;

   @NonNull
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
   private LocalDateTime matchDate;

   private byte homeGoals;

   private byte awayGoals;

   private List<PlayerExecutionDifference> matchPlayers = new ArrayList<>();

}
