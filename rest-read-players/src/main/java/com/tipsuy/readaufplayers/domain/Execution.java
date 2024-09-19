package com.tipsuy.readaufplayers.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

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
   private final String id = Instant.now().toString();

   @NonNull
   private LocalDateTime executionDateTime;

   @NonNull
   private Team team;

}
