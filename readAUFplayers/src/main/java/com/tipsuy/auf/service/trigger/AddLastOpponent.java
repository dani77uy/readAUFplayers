package com.tipsuy.auf.service.trigger;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.api.Trigger;

public class AddLastOpponent implements Trigger {

   @Override
   public void init(final Connection conn, final String schemaName, final String triggerName, final String tableName, final boolean before, final int type) throws SQLException {
      // do nothing
   }

   @Override
   public void fire(final Connection conn, final Object[] oldRow, final Object[] newRow) throws SQLException {
      final var currentTeam = Short.parseShort(newRow[1].toString());
      final var opponentTeam = Short.parseShort(newRow[2].toString());
      final var matchId = Long.parseLong(newRow[0].toString());
      evaluate(conn, currentTeam, opponentTeam, matchId);
      evaluate(conn, opponentTeam, currentTeam, matchId);
   }

   private void evaluate(final Connection conn, final short currentTeam, final short opponentTeam, final long matchId) throws SQLException {
      final var sqlCountHomeTeam = "select count(*) as cnt from team_last_opponent where current_team = ?";
      try (var ps = conn.prepareStatement(sqlCountHomeTeam)) {
         ps.setShort(1, currentTeam);
         try (var rs = ps.executeQuery()) {
            if (rs.next()) {
               final var counter = rs.getInt("cnt");
               if (counter > 0) {
                  update(conn, opponentTeam, currentTeam, matchId);
               } else {
                  insert(conn, currentTeam, opponentTeam, matchId);
               }
            } else {
               insert(conn, currentTeam, opponentTeam, matchId);
            }
         }
      }
   }

   private static void update(final Connection conn, final short currentTeam, final short opponentTeam, final long matchId) throws SQLException {
      final var sqlUpdateOpponent = "update team_last_opponent set opponent_team = ?, match_id = ? where current_team = ?";
      try (var ps2 = conn.prepareStatement(sqlUpdateOpponent)) {
         ps2.setShort(1, opponentTeam);
         ps2.setLong(2, matchId);
         ps2.setShort(3, currentTeam);
         ps2.executeUpdate();
      }
   }
   private static void insert(final Connection conn, final short currentTeam, final short opponentTeam, final long matchId) throws SQLException {
      final var sqlInsertOpponent = "insert into team_last_opponent values (?,?,?)";
      try (var ps2 = conn.prepareStatement(sqlInsertOpponent)) {
         ps2.setShort(1, currentTeam);
         ps2.setShort(2, opponentTeam);
         ps2.setLong(3, matchId);
         ps2.executeUpdate();
      }
   }
}
