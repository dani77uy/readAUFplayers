package com.tipsuy.auf.dao.trigger;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.api.Trigger;

public class AddMatchPlayers implements Trigger {

   @Override
   public void init(final Connection conn, final String schemaName, final String triggerName, final String tableName, final boolean before, final int type) throws SQLException {
      // do nothing
   }

   @Override
   public void fire(final Connection conn, final Object[] oldRow, final Object[] newRow) throws SQLException {
      final var playerId = Integer.parseInt(newRow[0].toString());
      final var executionId = Long.parseLong(newRow[1].toString());
      final var newMinutes = Short.parseShort(newRow[2].toString());
      final var newMatches = Byte.parseByte(newRow[3].toString());
      final var newGoals = Byte.parseByte(newRow[4].toString());
      try (var ps = conn.prepareStatement("select * from execution where id = ?")) {
         ps.setLong(1, executionId);
         try (var rs = ps.executeQuery()) {
            while (rs.next()) {
               final var timestampOfExecution = rs.getTime(2);
               final var playerTeamId = rs.getShort(3);
               final var opponentTeamId = rs.getShort(4);
               try (var ps1 = conn.prepareStatement("select * from match where home_team = ? and away_team = ? and match_date_time between ? and ?")) {
                  ps1.setShort(1, playerTeamId);
                  ps1.setShort(2, opponentTeamId);
               }
            }
         }
      }
   }
}
