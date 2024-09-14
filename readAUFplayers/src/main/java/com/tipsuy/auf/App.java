package com.tipsuy.auf;

import com.tipsuy.auf.gui.AddTeamOption;
import java.io.IOException;
import java.util.Scanner;

import com.tipsuy.auf.config.H2Config;
import com.tipsuy.auf.gui.ExecuteReadFromAufPage;
import com.tipsuy.auf.service.ReadAufPage;

public class App {

   public static void main(final String[] args) {

      System.out.println("Init H2 database...");
      final var connection = H2Config.INSTANCE.getConnection();
   //   H2Config.initScriptExecution(connection, "/data.sql");
      showMenu();

   }

   private static void showMenu() {
      final var scanner = new Scanner(System.in);

      while (true) {
         System.out.println("Select an action:");
         System.out.println("1 - Add match");
         System.out.println("2 - Execute a read from AUF page");
         System.out.println("3 - Generate a rooster report");
         System.out.println("4 - Exit");

         final var option = scanner.nextLine();
         final var addTeamOption = new AddTeamOption(scanner);
         final var readPageOption = new ExecuteReadFromAufPage(scanner);
         switch (option) {
            case "1": {
               addTeamOption.addMatch(addTeamOption.selectSeason());
               break;
            }
            case "2": {
               readPageOption.readPage(addTeamOption.selectSeason());
               break;
            }
//            case "3": generateReport(scanner); break;
            case "4":  scanner.close(); return;
            default: System.exit(-1);
         }
      }
   }


}
