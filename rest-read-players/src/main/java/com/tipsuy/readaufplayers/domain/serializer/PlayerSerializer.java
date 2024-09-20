package com.tipsuy.readaufplayers.domain.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tipsuy.readaufplayers.config.GlobalConfig;
import com.tipsuy.readaufplayers.domain.Player;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerSerializer extends JsonSerializer<Player> {

  private final JsonSerializer<Object> defaultSerializer;

  @Override
  public void serialize(final Player player, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeNumberField("playerId", player.getPlayerId());
    jsonGenerator.writeStringField("playerName", player.getPlayerName());
    jsonGenerator.writeStringField("birthDate", player.getBirthDate().format(DateTimeFormatter.ofPattern(GlobalConfig.DATE_FORMAT)));
    defaultSerializer.serialize(player, jsonGenerator, serializerProvider);
  }
}
