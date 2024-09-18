package com.tipsuy.auf.domain.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Data
public class TeamCurrentRooster implements Serializable {

   @Serial
   private static final long serialVersionUID = 723162263332672400L;

   private Team team;

   private Season season;

   private final Collection<Player> players = new HashSet<>();

   public void addPlayer(final Player player) {
      players.add(player);
   }

   public void removePlayer(final Player player) {
      players.remove(player);
   }
}
