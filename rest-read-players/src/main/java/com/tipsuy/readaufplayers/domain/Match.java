package com.tipsuy.readaufplayers.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

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
@Document(collection = "matches")
@Data
public class Match implements Serializable {

   @Serial
   private static final long serialVersionUID = -1743047731326953217L;

   @Id
   private Long matchId;

   @NonNull
   private Short homeTeam;

   @NonNull
   private Short awayTeam;

   @NonNull
   private OffsetDateTime matchDateTime;

   private byte matchDay;

   private byte homeGoals;

   private byte awayGoals;

   private final List<MatchPlayer> matchPlayers = new ArrayList<>();

   public void addMatchPlayer(@NonNull final MatchPlayer matchPlayer) {
      matchPlayers.add(matchPlayer);
   }

   public void addMatchPlayers(@NonNull final List<MatchPlayer> matchPlayer) {
      matchPlayers.addAll(matchPlayer);
   }
}
