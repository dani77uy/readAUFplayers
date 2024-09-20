package com.tipsuy.readaufplayers.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Document(collection = "players")
@Data
public class Player implements Serializable {

   @Serial
   private static final long serialVersionUID = -5164657253912077128L;

   @Id
   private Long playerId;

   @NonNull
   private String playerName;

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Montevideo")
   private LocalDate birthDate = null;

}
