package com.tipsuy.readaufplayers.domain;

import com.tipsuy.readaufplayers.domain.pk.TeamPlayer;
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
@Document(collection = "team-season-player")
@Data
public class TeamSeasonPlayer implements Serializable {

   @Serial
   private static final long serialVersionUID = 5178010682595418013L;

   @Id
   private TeamPlayer id;

   private byte totalGoals = 0;

   private short totalMinutes = 0;

   private byte totalMatches = 0;

}
