package com.tipsuy.readaufplayers.domain.pk;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlayerExecutionPK(@JsonProperty(value = "executionId") String executionId, @JsonProperty(value = "seasonId") short seasonId,
                                @JsonProperty(value = "playerUniqueIdentification") String playerUniqueIdentification) implements Serializable {

   @Override
   public boolean equals(final Object obj) {
      if (obj == null) return false;
      if (obj == this) return true;
      if (!(obj instanceof final PlayerExecutionPK other)) return false;
      return executionId.equals(other.executionId) && seasonId == other.seasonId;
   }

   @Override
   public int hashCode() {
      return Objects.hash(executionId, seasonId, playerUniqueIdentification);
   }
}
