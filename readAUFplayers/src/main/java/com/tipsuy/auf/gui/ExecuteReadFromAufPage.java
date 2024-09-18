package com.tipsuy.auf.gui;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.tipsuy.auf.domain.model.Player;
import com.tipsuy.auf.domain.model.Team;
import com.tipsuy.auf.service.ReadAufPage;
import com.tipsuy.auf.dao.ReadDB;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExecuteReadFromAufPage {

   private final Scanner scanner;

   public void readPage(final byte seasonId) {
      System.out.println("Select the team to generate execution: ");
      final List<Team> allTeams = ReadDB.getAllTeams();
      allTeams.forEach(team -> System.out.println(STR."\t\{team.id()} - \{team.name()}"));
      final var teamSelectedId = Short.parseShort(scanner.nextLine().trim());
      final Team teamSelected = allTeams.stream().filter(team -> team.id() == teamSelectedId).findFirst().orElseThrow();
      try {
         final List<Player> players = ReadAufPage.read(teamSelected.url());
         ReadDB.insertPlayers(players, teamSelectedId, seasonId);
      } catch (IOException e) {
         throw new IllegalArgumentException(e);
      }
   }
}
