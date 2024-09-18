package com.tipsuy.readaufplayers.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Document(collection = "match")
@Data
public class Match implements Serializable {

   @Serial
   private static final long serialVersionUID = -1743047731326953216L;

   @Id
   private String matchId = null;

   @NonNull
   private Team homeTeam;

   @NonNull
   private Team awayTeam;

   @NonNull
   private LocalDateTime matchDate;

   private byte homeGoals;

   private byte awayGoals;

   private List<PlayerExecutionDifference> matchPlayers = new ArrayList<>();

}
