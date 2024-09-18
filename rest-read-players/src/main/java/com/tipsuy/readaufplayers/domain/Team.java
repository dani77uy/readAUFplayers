package com.tipsuy.readaufplayers.domain;

import java.io.Serial;
import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
@EqualsAndHashCode(callSuper=false)
@Document(collection = "teams")
@Data
public class Team implements Serializable {

   @Serial
   private static final long serialVersionUID = 3350957717424104540L;

   @Id
   private String teamId = null;

   @Indexed(unique = true)
   @NonNull
   private String teamName;

   @Indexed(unique = true)
   @NonNull
   private String url;

   @Indexed(unique = true)
   @NonNull
   private Short customTeamId;

}
