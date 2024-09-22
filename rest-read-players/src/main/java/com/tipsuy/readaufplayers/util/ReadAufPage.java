package com.tipsuy.readaufplayers.util;

import com.tipsuy.readaufplayers.domain.dto.ReadPlayerDTO;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
@UtilityClass
public class ReadAufPage {

  public List<ReadPlayerDTO> read(final String url) throws IOException {
    final Document doc = Jsoup.connect(url).get();
    final Elements playerElements = doc.select("article.jugador_club");
    final var players = new LinkedList<ReadPlayerDTO>();
    for (Element jugadorElement : playerElements) {
      try {
        final var player = convertElementToPlayer(jugadorElement);
        players.add(player);
        log.info("Player read: {}", player);
      } catch (NumberFormatException e) {
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
    final var completeBirthData = element.select("p").text();
    final var birthData = completeBirthData.split(" - ");
    final String bd;
    if (birthData.length > 1) {
      final var formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
      try {
        final var fecha = formatoEntrada.parse(birthData[0].trim());
        final var formatoSalida = new SimpleDateFormat(DateUtil.DATE_FORMAT);
        bd = formatoSalida.format(fecha);
      } catch (ParseException e) {
        throw new IllegalArgumentException(e);
      }
    } else {
      bd = null;
    }
    final var birthDate = Optional.ofNullable(bd).map(DateUtil::stringDateToOffsetDateTime).orElse(null);
    return new ReadPlayerDTO(Byte.parseByte(totalMatches), Byte.parseByte(totalGoals), Short.parseShort(totalMinutes), birthDate, name, createPlayerId(name,
        completeBirthData));
  }

  public String createPlayerId(final String name, final String birthday) {
    final var normalizedName = StringUtils.replace(name, " ", "_");
    final var replacedBirthday = StringUtils.replace(StringUtils.replace(birthday, "ñ", "n"), " ", "_");
    return STR."\{name.hashCode()}__\{normalizedName}__\{replacedBirthday}";
  }

}
