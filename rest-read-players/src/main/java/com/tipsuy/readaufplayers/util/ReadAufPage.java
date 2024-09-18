package com.tipsuy.readaufplayers.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.tipsuy.readaufplayers.domain.dto.ReadPlayerDTO;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class ReadAufPage {

   private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

   public List<ReadPlayerDTO> read(final String url) throws IOException {
      final Document doc = Jsoup.connect(url).get();
      final Elements playerElements = doc.select("article.jugador_club");
      final var players = new LinkedList<ReadPlayerDTO>();
      for (Element jugadorElement : playerElements) {
         try {
            final var player = convertElementToPlayer(jugadorElement);
            players.add(player);
            log.info("Player read: {}", player);
         } catch (Exception e) {
            break;
         }
      }
      return players;
   }

   private ReadPlayerDTO convertElementToPlayer(final Element element) {
      final var name = element.select("h3").text();
      final var totalMatches = element.select(".icono-partidos + span").text().trim();
      final var totalMinutes = element.select(".icono-minutos + span").text().trim();
      final var totalGoals = element.select(".icono-goles + span").text();
      final var birthData = element.select("p").text().split(" - ");
      final String birthDate;
      if (birthData.length > 1) {
         birthDate = birthData[0];
      } else  {
         birthDate = null;
      }
      return new ReadPlayerDTO(Byte.parseByte(totalMatches), Byte.parseByte(totalGoals),Short.parseShort(totalMinutes),
            Optional.ofNullable(birthDate).map(b -> LocalDate.parse(b, DATE_TIME_FORMATTER)).orElse(null), name);
   }

}
