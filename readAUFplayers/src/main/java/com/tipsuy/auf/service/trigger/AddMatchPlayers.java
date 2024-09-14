package com.tipsuy.auf.service.trigger;

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
      final var minutes = Short.parseShort(newRow[2].toString());
      final var matches = Byte.parseByte(newRow[3].toString());
      final var goals = Byte.parseByte(newRow[4].toString());
      
   }
}
