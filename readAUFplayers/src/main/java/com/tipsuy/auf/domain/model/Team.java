package com.tipsuy.auf.domain.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

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
public class Team implements Serializable {

   @Serial
   private static final long serialVersionUID = -8490682466172018951L;

   private short id;

   private String name;

   private String url;

   private final List<Player> players = new LinkedList<>();

   public void addPlayer(final Player player) {
      players.add(player);
   }
}
