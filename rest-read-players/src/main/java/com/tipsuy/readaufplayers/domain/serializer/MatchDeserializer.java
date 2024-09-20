package com.tipsuy.readaufplayers.domain.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.tipsuy.readaufplayers.config.GlobalConfig;
import com.tipsuy.readaufplayers.domain.Match;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MatchDeserializer extends JsonDeserializer<Match> {

   private final String timezone;

   @Override
   public Match deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JacksonException {
      final JsonNode node = jsonParser.getCodec().readTree(jsonParser);
      final var match = new Match();
      match.setMatchId(node.get("matchId").asLong());
      match.setHomeTeam(node.get("homeTeam").shortValue());
      match.setAwayTeam(node.get("awayTeam").shortValue());
      match.setHomeGoals(node.get("homeGoals").decimalValue().byteValue());
      match.setAwayGoals(node.get("awayGoals").decimalValue().byteValue());
      match.setMatchDay(node.get("matchDay").decimalValue().byteValue());
      final var dateTime = node.get("matchDateTime").asText();
      final var localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(GlobalConfig.DATETIME_FORMAT));
      match.setMatchDateTime(localDateTime.atZone(ZoneId.of(timezone)).toOffsetDateTime());
      return match;
   }
}
