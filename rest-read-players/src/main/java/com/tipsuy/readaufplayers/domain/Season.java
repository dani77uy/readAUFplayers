package com.tipsuy.readaufplayers.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tipsuy.readaufplayers.domain.pk.TeamPlayer;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Season implements Serializable {

   @Serial
   private static final long serialVersionUID = -6133393392430940895L;

   @Id
   private Short id;

   @NonNull
   private String tournamentName;

   private short year;

   private Collection<Short> teams = new HashSet<>();

   private Collection<Long> matches = new HashSet<>();

   private List<TeamPlayer> teamSeasonPlayersData = new ArrayList<>();

   private List<String> executions = new LinkedList<>();
}
