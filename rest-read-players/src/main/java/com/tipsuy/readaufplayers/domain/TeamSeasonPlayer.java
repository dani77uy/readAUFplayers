package com.tipsuy.readaufplayers.domain;

import java.io.Serial;
import java.io.Serializable;

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
@Document(collection = "team-season-player")
@Data
public class TeamSeasonPlayer implements Serializable {

   @Serial
   private static final long serialVersionUID = 5178010682595418013L;

   @Id
   private String id = null;

   @NonNull
   private Player player;

   @NonNull
   private Team team;

   private byte totalGoals = 0;

   private short totalMinutes = 0;

   private byte totalMatches = 0;

}
