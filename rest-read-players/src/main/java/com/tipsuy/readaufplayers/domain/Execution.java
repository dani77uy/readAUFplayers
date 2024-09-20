package com.tipsuy.readaufplayers.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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
@Document(collection = "executions")
@Data
public class Execution implements Serializable {

   @Serial
   private static final long serialVersionUID = -7665208389160060339L;

   @Id
   private final String id = Instant.now().toString();

   @NonNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
   private LocalDateTime executionDateTime;

   @NonNull
   private Short teamId;

   @Nullable
   private Short teamLastOpponentId;

}
