package com.tipsuy.readaufplayers.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tipsuy.readaufplayers.domain.pk.PlayerExecutionPK;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@EqualsAndHashCode(callSuper = false)
@Document(collection = "player-execution")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PlayerExecution implements Serializable {

  @Serial
  private static final long serialVersionUID = -7671747242050314773L;

  @JsonProperty("playerExecutionPk")
  @Id
  @NonNull
  private PlayerExecutionPK playerExecutionPk;

  @JsonProperty
  private byte totalMatches = 0;

  @JsonProperty
  private byte totalGoals = 0;

  @JsonProperty
  private short totalMinutes = 0;

  @JsonCreator
  public PlayerExecution(@JsonProperty("playerExecutionPk") final PlayerExecutionPK playerExecutionPk) {
    this.playerExecutionPk = playerExecutionPk;
  }

}
