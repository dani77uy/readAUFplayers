package com.tipsuy.readaufplayers.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tipsuy.readaufplayers.util.DateUtil;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATETIME_FORMAT)
   private OffsetDateTime matchDateTime;

   private byte matchDay;

   private byte homeGoals;

   private byte awayGoals;

   private List<MatchPlayer> matchPlayers = new ArrayList<>();

   public void addMatchPlayer(@NonNull final MatchPlayer matchPlayer) {
      matchPlayers.add(matchPlayer);
   }

   public void addMatchPlayers(@NonNull final List<MatchPlayer> matchPlayer) {
      matchPlayers.addAll(matchPlayer);
   }
}
