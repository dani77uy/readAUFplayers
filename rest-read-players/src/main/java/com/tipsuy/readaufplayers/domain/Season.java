package com.tipsuy.readaufplayers.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
@Document(collection = "seasons")
@CompoundIndex(def = "{'tournamentName': 1, 'year': 1}", unique = true)
@Data
public class Season implements Serializable {

   @Serial
   private static final long serialVersionUID = -6133393392430940896L;

   @Id
   private Short id;

   @NonNull
   private String tournamentName;

   private short year;

   private final Collection<Team> teams = new HashSet<>();

   private final Collection<Match> matches = new HashSet<>();

   private final List<TeamSeasonPlayer> teamSeasonPlayersData = new ArrayList<>();

   private final List<Execution> executions = new LinkedList<>();
}
