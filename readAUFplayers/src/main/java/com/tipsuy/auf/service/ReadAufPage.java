package com.tipsuy.auf.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

   public void read(final String url) throws IOException {
      final Document doc = Jsoup.connect(url).get();
      final Elements jugadoresElements = doc.select("article.jugador_club");

      for (Element jugadorElement : jugadoresElements) {
         final String nombre = jugadorElement.select("h3").text();
         try {
            final String posicion = jugadorElement.select("h4").text();
            final int partidos = Integer.parseInt(jugadorElement.select(".icono-partidos + span").text());
            final int minutos = Integer.parseInt(jugadorElement.select(".icono-minutos + span").text());
            final int goles = Integer.parseInt(jugadorElement.select(".icono-goles + span").text());

            // Extraer fecha de nacimiento y edad desde el texto
            final String[] datos = jugadorElement.select("p").text().split(" - ");
            if (datos.length > 1) {
               final String fechaNacimiento = datos[0];
               final int edad = Integer.parseInt(datos[1].split(" ")[0]);
            } else {
               //
            }
            System.out.println(convertElementToPlayer(jugadorElement));
         } catch (Exception e) {
            break;
         }

      }

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
            Integer.parseInt(totalMatches), Integer.parseInt(totalMinutes), Integer.parseInt(totalGoals));
   }

}
