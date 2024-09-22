package com.tipsuy.readaufplayers.domain;

import static com.tipsuy.readaufplayers.util.DateUtil.DATE_FORMAT;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Document(collection = "players")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Player implements Serializable {

   @Serial
   private static final long serialVersionUID = -6387944600697764807L;

   @JsonProperty(required = false)
   @Id
   private String id = null;

   @JsonProperty(value = "playerUniqueIdentification", index = 1)
   @NonNull
   @Indexed(unique = true)
   private String uniquePropertyOfPlayer;

   @JsonProperty(value = "playerName", index = 2)
   @NonNull
   private String playerName;

   @JsonProperty(value = "birthDate", index = 3)
   @JsonFormat(pattern = DATE_FORMAT)
   @Nullable
   private OffsetDateTime birthDate = null;

}
