package com.tipsuy.readaufplayers.domain.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tipsuy.readaufplayers.domain.Match;
import com.tipsuy.readaufplayers.util.DateUtil;
import java.io.IOException;
import java.time.Clock;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MatchSerializer extends JsonSerializer<Match> {

   private final JsonSerializer<Object> defaultSerializer;

   private final Clock clock;

   @Override
   public void serialize(final Match match, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeNumberField("matchId", match.getMatchId());
      jsonGenerator.writeNumberField("homeTeam", match.getHomeTeam());
      jsonGenerator.writeNumberField("awayTeam", match.getAwayTeam());
      jsonGenerator.writeNumberField("homeGoals", match.getHomeGoals());
      jsonGenerator.writeNumberField("awayGoals", match.getAwayGoals());
      jsonGenerator.writeNumberField("matchDay", match.getMatchDay());
      jsonGenerator.writeStringField("matchDateTime", DateUtil.offsetDateTimeToStringDateTime(match.getMatchDateTime(), clock));
      jsonGenerator.writeEndObject();
      defaultSerializer.serialize(match, jsonGenerator, serializerProvider);
   }
}
