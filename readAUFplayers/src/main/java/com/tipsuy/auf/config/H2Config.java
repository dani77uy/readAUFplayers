package com.tipsuy.auf.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.stream.Collectors;

public enum H2Config {

   INSTANCE;

   private static final String JDBC_URL = "jdbc:h2:../data/auf_players_db";

   private static final String USER = "daniel";

   private static final String PASSWORD = "daniel";

   public Connection getConnection() {
      try {
         Class.forName("org.h2.Driver");
         return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
      } catch (Exception e) {
         throw new IllegalArgumentException(e);
      }
   }

   public static void initScriptExecution(final Connection connection, final String scriptPath) {
      try (var inputStream = H2Config.class.getResourceAsStream(scriptPath);
            var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

         final var sql = reader.lines().collect(Collectors.joining("\n"));

         try (var stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("SQL Script successfully executed...");
         }

      } catch (Exception e) {
         throw new IllegalArgumentException(e);
      }
   }
}
