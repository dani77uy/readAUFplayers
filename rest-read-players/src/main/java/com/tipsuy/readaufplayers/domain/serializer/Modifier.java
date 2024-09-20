package com.tipsuy.readaufplayers.domain.serializer;

import com.tipsuy.readaufplayers.domain.Player;
import java.io.Serial;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.tipsuy.readaufplayers.domain.Match;
import com.tipsuy.readaufplayers.domain.pk.ExecutionPlayer;

public class Modifier extends BeanSerializerModifier {

   @Serial
   private static final long serialVersionUID = 1257175037101640471L;

   @Override
   public JsonSerializer<?> modifySerializer(final SerializationConfig config, final BeanDescription beanDesc, final JsonSerializer<?> serializer) {
      if (beanDesc.getBeanClass() == ExecutionPlayer.class) {
         return new ExecutionPlayerSerializer((JsonSerializer<Object>) serializer);
      }
      if (beanDesc.getBeanClass() == Match.class) {
         return new MatchSerializer((JsonSerializer<Object>) serializer);
      }
      if (beanDesc.getBeanClass() == Player.class) {
         return new PlayerSerializer((JsonSerializer<Object>) serializer);
      }
      return super.modifySerializer(config, beanDesc, serializer);
   }
}
