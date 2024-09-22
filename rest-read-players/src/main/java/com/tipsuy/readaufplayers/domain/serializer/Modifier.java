package com.tipsuy.readaufplayers.domain.serializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.tipsuy.readaufplayers.domain.Match;
import java.io.Serial;
import java.time.Clock;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Modifier extends BeanSerializerModifier {

   @Serial
   private static final long serialVersionUID = 1257175037101640471L;

   private final Clock clock;

   @Override
   public JsonSerializer<?> modifySerializer(final SerializationConfig config, final BeanDescription beanDesc, final JsonSerializer<?> serializer) {
      if (beanDesc.getBeanClass() == Match.class) {
         return new MatchSerializer((JsonSerializer<Object>) serializer, clock);
      }
      return super.modifySerializer(config, beanDesc, serializer);
   }
}
