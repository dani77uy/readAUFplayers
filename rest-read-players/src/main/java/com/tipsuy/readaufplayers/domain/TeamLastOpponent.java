package com.tipsuy.readaufplayers.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Document(collection = "team-last-opponent")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TeamLastOpponent implements Serializable {

   @Serial
   private static final long serialVersionUID = -3333896898271273206L;

   @Id
   private short teamId;

   private short opponentId;

   private long matchId;

   private short seasonId;
}
