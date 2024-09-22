package com.tipsuy.readaufplayers.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tipsuy.readaufplayers.domain.pk.MatchPlayerPK;
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
@Document(collection = "match-players")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MatchPlayer implements Serializable {

   @Serial
   private static final long serialVersionUID = 6773778369322277788L;

   @Id
   private MatchPlayerPK matchPlayerPK;

   private byte minutesPlayed;

   private byte goals;
}
