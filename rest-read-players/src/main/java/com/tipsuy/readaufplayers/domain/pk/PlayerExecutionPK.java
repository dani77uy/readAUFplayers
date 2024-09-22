package com.tipsuy.readaufplayers.domain.pk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlayerExecutionPK(@JsonProperty(value = "executionId") String executionId, @JsonProperty(value = "seasonId") short seasonId,
                                @JsonProperty(value = "playerUniqueIdentification") String playerUniqueIdentification) implements Serializable {

}
