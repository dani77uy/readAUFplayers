package com.tipsuy.readaufplayers.util;

import java.text.Normalizer;
import java.util.Locale;

import io.micrometer.common.util.StringUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TextUtil {

   public String normalizeName(final String texto) {
      if (StringUtils.isEmpty(texto)) {
         return texto;
      }

      final var textoLowerCase = texto.toLowerCase(Locale.ROOT);

      var normalized = Normalizer.normalize(textoLowerCase, Normalizer.Form.NFD);
      normalized = normalized.replaceAll("\\p{M}", "");

      final var result = new StringBuilder();
      boolean passNext = true;
      for (char c : normalized.toCharArray()) {
         if (Character.isSpaceChar(c)) {
            passNext = true;
         } else if (passNext) {
            c = Character.toTitleCase(c);
            passNext = false;
         }
         result.append(c);
      }

      return result.toString();
   }

}
