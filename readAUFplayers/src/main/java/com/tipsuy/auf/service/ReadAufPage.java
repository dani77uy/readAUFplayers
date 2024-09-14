package com.tipsuy.auf.service;

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

import com.tipsuy.auf.domain.Player;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReadAufPage {

   private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

   public List<Player> read(final String url) throws IOException {
      final Document doc = Jsoup.connect(url).get();
      final Elements playerElements = doc.select("article.jugador_club");
      final var players = new LinkedList<Player>();
      for (Element jugadorElement : playerElements) {
         try {
            final var player = convertElementToPlayer(jugadorElement);
            players.add(player);
            System.out.println(player);
         } catch (Exception e) {
            break;
         }
      }
      return players;
   }

   private Player convertElementToPlayer(final Element element) {
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
      return new Player(null, name, Optional.ofNullable(birthDate).map(b -> LocalDate.parse(b, formatter)).orElse(null),
            Byte.parseByte(totalMatches), Short.parseShort(totalMinutes), Byte.parseByte(totalGoals));
   }

}
