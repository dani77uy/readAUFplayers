package com.tipsuy.readaufplayers.domain.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tipsuy.readaufplayers.domain.pk.ExecutionPlayer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExecutionPlayerSerializer extends JsonSerializer<ExecutionPlayer> {

   private final JsonSerializer<Object> defaultSerializer;

   @Override
   public void serialize(final ExecutionPlayer executionPlayer, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeStringField("executionId", executionPlayer.executionId());
      jsonGenerator.writeNumberField("playerId", executionPlayer.playerId());
      defaultSerializer.serialize(executionPlayer, jsonGenerator, serializerProvider);
   }
}
