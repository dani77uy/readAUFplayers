package com.tipsuy.auf.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

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
public class Player implements Serializable {

   @Serial
   private static final long serialVersionUID = 3041213461253811957L;

   private Integer id;

   private String name;

   private LocalDate birthDate;

   private byte matchesPlayed;

   private short minutesPlayed;

   private byte goalsScored;
}
