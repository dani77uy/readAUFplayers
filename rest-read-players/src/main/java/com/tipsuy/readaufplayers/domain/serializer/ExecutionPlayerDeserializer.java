package com.tipsuy.readaufplayers.domain.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.tipsuy.readaufplayers.domain.pk.ExecutionPlayer;

public class ExecutionPlayerDeserializer extends JsonDeserializer<ExecutionPlayer> {

   @Override
   public ExecutionPlayer deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JacksonException {
      final JsonNode node = jsonParser.getCodec().readTree(jsonParser);
      return new ExecutionPlayer(node.get("executionId").asText(), node.get("playerId").asLong());
   }
}
