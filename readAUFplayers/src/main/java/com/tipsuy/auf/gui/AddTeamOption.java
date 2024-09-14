package com.tipsuy.auf.gui;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

import com.tipsuy.auf.domain.Team;
import com.tipsuy.auf.service.ReadDB;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddTeamOption {

   private final Scanner scanner;

   public byte selectSeason() {
      System.out.println("Select season or tournament: ");
      ReadDB.getAllSeasons().forEach(season -> System.out.println(STR."\t \{season}"));
      final var seasonId = scanner.nextLine();
      try {
         return Byte.parseByte(seasonId);
      } catch (Exception e) {
         System.err.println("Season not found. Using default season with id 1.");
         return 1;
      }
   }

   public void addMatch(final byte seasonId) {
      Timestamp dateTime = new Timestamp(System.currentTimeMillis());
      boolean isValidDate = false;
      String readDateTime = null;
      do {
         try {
            System.out.println("Select date and time (format yyyy-MM-dd HH:mm): ");
            readDateTime = scanner.nextLine();
            dateTime = getTimestamp(readDateTime);
            isValidDate = true;
         } catch (ParseException ex) {
            System.err.println(STR."Invalid dateTime \{readDateTime} format:");
         }
      } while (!isValidDate);

      System.out.println("Select match day: ");
      final var matchDay = scanner.nextLine();
      System.out.println(STR."\tMatch Day: \{matchDay}");

      System.out.println("Select home team: ");
      final var allTeams = new ArrayList<>(ReadDB.getAllTeamsByTournament(seasonId));
      allTeams.forEach(team -> System.out.println(STR."\t \{team.id()} - \{team.name()}"));
      final var selectedHomeTeam = ReadDB.getTeamById(Short.parseShort(scanner.nextLine()), seasonId);
      allTeams.remove(selectedHomeTeam);

      Team selectedAwayTeam;
      do {
         System.out.println("Select away team: ");
         allTeams.forEach(team -> System.out.println(STR."\t \{team.id()} - \{team.name()}"));
         selectedAwayTeam = ReadDB.getTeamById(Short.parseShort(scanner.nextLine()), seasonId);
      } while (teamIsAlreadySelected(selectedHomeTeam, selectedAwayTeam));

      System.out.println(STR."Select home [\{selectedHomeTeam}] goals: ");
      final var homeGoals = scanner.nextLine();
      System.out.println(STR."Select away [\{selectedAwayTeam}] goals: ");
      final var awayGoals = scanner.nextLine();
      final var match = ReadDB.addMatch(selectedHomeTeam, selectedAwayTeam, homeGoals, awayGoals, matchDay, seasonId, dateTime);
      System.out.println("*******************************");
      System.out.println(STR."\tMatch [\{match}] was added.");
      System.out.println("*******************************");
   }

   private static boolean teamIsAlreadySelected(final Team team1, final Team team2) {
      final var b = team1.equals(team2);
      if (b) {
         System.err.println(STR."Away team \{team2.name()} is the same home team: \{team1.name()}");
      }
      return b;
   }

   private static Timestamp getTimestamp(final String date) throws ParseException {
      final var dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      final var parsedDate = dateFormat.parse(date);
      return new Timestamp(parsedDate.getTime());
   }

}
