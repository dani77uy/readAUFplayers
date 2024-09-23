package com.tipsuy.readaufplayers.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tipsuy.readaufplayers.config.GlobalConfig;
import com.tipsuy.readaufplayers.domain.Execution;
import com.tipsuy.readaufplayers.domain.PlayerExecution;
import com.tipsuy.readaufplayers.domain.pk.PlayerExecutionPK;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.springframework.util.ResourceUtils;

@UtilityClass
public class Convert {

  public static void main(String[] args) throws IOException {
    final var resource = ResourceUtils.getFile("classpath:./template.json");
    final var file = new File(resource.getAbsolutePath());
    final var gb = new GlobalConfig("America/Montevideo");
    final var mapper = gb.objectMapper();
    final var list = mapper.readValue(file, new TypeReference<ArrayList<Elemento>>() {});
    final var listNew = new ArrayList<PlayerExecution>();
    list.forEach(e -> {
      final var executionID = createExecutionId(e.DATETIME_OF_EXECUTION);
      final var playerProperty = createPlayerUniqueIdentification(e);
      final var pk = new PlayerExecutionPK(executionID, (short) 1, playerProperty);
      final var p = new PlayerExecution(pk);
      p.setTotalMatches(e.NEW_MATCHES_PARTICIPATION_VALUE);
      p.setTotalMinutes(e.NEW_MINUTES_VALUE);
      p.setTotalGoals(e.NEW_GOALS_VALUE);
      System.out.println("Adding : " + p);
      listNew.add(p);
    });
    final var resourceNew = ResourceUtils.getFile("classpath:./newPlayerExecution.json");
    System.out.println("file: " + resourceNew.getAbsolutePath());
    final var fileNew = new File(resourceNew.getAbsolutePath());
    mapper.writeValue(fileNew, listNew);
    fileNew.createNewFile();
  }

  private static String createPlayerUniqueIdentification(final Elemento e) {
    return ReadAufPage.createPlayerId(e.NAME, Optional.ofNullable(e.BIRTHDATE).filter(x -> !x.trim().equalsIgnoreCase("null")).orElse(null));
  }

  private String createExecutionId(final String datetimeOfExecution) {
    return datetimeOfExecution.replace(' ', 'T') + "00Z";
  }

  private record Elemento(short PLAYER_ID, long EXECUTION_ID, short NEW_MINUTES_VALUE, byte NEW_MATCHES_PARTICIPATION_VALUE, byte NEW_GOALS_VALUE,
                          String NAME, String BIRTHDATE, String DATETIME_OF_EXECUTION) {

  }
}
