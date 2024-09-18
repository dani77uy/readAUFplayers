package com.tipsuy.readaufplayers.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
@Document(collection = "executions")
@Data
public class Execution implements Serializable {

   @Serial
   private static final long serialVersionUID = -7665208389160060068L;

   @Id
   private Object id = null;

   @NonNull
   private LocalDateTime executionDateTime;

   @NonNull
   private Team team;

}
