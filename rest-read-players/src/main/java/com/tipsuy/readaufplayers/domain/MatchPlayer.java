package com.tipsuy.readaufplayers.domain;

import java.io.Serial;
import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tipsuy.readaufplayers.domain.pk.MatchPlayerPK;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Document(collection = "match-players")
@Data
public class MatchPlayer implements Serializable {

   @Serial
   private static final long serialVersionUID = 6773778369322277788L;

   @Id
   private MatchPlayerPK matchPlayerPK;

   private byte minutesPlayed;

   private byte goals;
}
