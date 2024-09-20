package com.tipsuy.readaufplayers.domain.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.tipsuy.readaufplayers.config.GlobalConfig;
import com.tipsuy.readaufplayers.domain.Player;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerDeserializer extends JsonDeserializer<Player> {

  private final String timezone;

  @Override
  public Player deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JacksonException {
    final JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    final var player = new Player();
    player.setPlayerId(node.get("playerId").asLong());
    player.setPlayerName(node.get("playerName").asText());
    final var bd = LocalDate.parse(node.get("birthDate").asText(), DateTimeFormatter.ofPattern(GlobalConfig.DATE_FORMAT));
    final var zonedBD = bd.atStartOfDay(ZoneId.of(timezone));
    player.setBirthDate(zonedBD.toOffsetDateTime());
    return player;
  }
}
