package com.tipsuy.auf.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.tipsuy.auf.config.H2Config;
import com.tipsuy.auf.domain.model.Match;
import com.tipsuy.auf.domain.model.MatchPlayer;
import com.tipsuy.auf.domain.model.Player;
import com.tipsuy.auf.domain.model.Season;
import com.tipsuy.auf.domain.model.Team;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReadDB {

   private Player getPlayerById(final int playerId, final byte seasonId, final Connection connection) throws SQLException {
      try (var ps = connection.prepareStatement("select * from player where id = ? and season_id = ?")) {
         ps.setInt(1, playerId);
         ps.setByte(2, seasonId);
         try (var rs = ps.executeQuery()) {
            if (rs.next()) {

            }
         }
      }
   }

   private List<Player> getPlayersOfTeam(final short teamId, final byte tournamentId, final Connection connection) throws SQLException {
      final var sqlPlayers = "select * from player where team_id = ? and season_id = ? order by id asc";
      final var players = new ArrayList<Player>();
      try (var ps2 = connection.prepareStatement(sqlPlayers)) {
         ps2.setShort(1, teamId);
         ps2.setByte(2, tournamentId);
         try (var rs2 = ps2.executeQuery()) {
            while (rs2.next()) {
               final var sqlMatchPlayer = "select * from player_change_after_execution where player_id = ? order by execution_id desc limit 1";
               final var playerId = rs2.getInt(1);
               short totalMinutes = 0;
               byte totalParticipations = 0;
               byte totalGoals = 0;
               try (var ps3 = connection.prepareStatement(sqlMatchPlayer)) {
                  ps3.setInt(1, playerId);
                  try (var rs3 = ps3.executeQuery()) {
                     if (rs3.next()) {
                        totalMinutes = rs3.getShort("new_minutes_value");
                        totalParticipations = rs3.getByte("new_matches_participation_value");
                        totalGoals = rs3.getByte("new_goals_value");
                     }
                  }
               }
               final var player = new Player(rs2.getInt(1), rs2.getString(2), Optional.ofNullable(rs2.getDate(3)).map(Date::toLocalDate).orElse(null), totalParticipations,
                     totalMinutes, totalGoals);
               players.add(player);
            }
         }
      }
      return players;
   }

   public Team getTeamById(final short teamId, final byte tournamentId) {
      final var sqlTeams = "select * from team where id = ? and season_that_belong = ?";
      try (var connection = H2Config.INSTANCE.getConnection()) {
         try (var ps = connection.prepareStatement(sqlTeams)) {
            ps.setShort(1, teamId);
            ps.setByte(2, tournamentId);
            try (var rs = ps.executeQuery()) {
               if (rs.next()) {
                  final var team = new Team(rs.getShort(1), rs.getString(2), rs.getString(3));
                  getPlayersOfTeam(teamId, tournamentId, connection).forEach(team::addPlayer);
               }
            }
         }
      } catch (SQLException e) {
         throw new IllegalArgumentException(e);
      }
      return null;
   }

   public Match addMatch(Team home, Team away, String homeGoals, String awayGoals, String matchDay, byte seasonId, Timestamp dateTime) {
      final var sql = """
            insert into match(id,home_team,away_team,home_goals,away_goals,season_id,match_day,match_date_time) values (NEXT VALUE FOR seq_matches,?,?,?,?,?,?,?)
            """;
      try (var connection = H2Config.INSTANCE.getConnection()) {
         final var season = getSeasonById(seasonId, connection);
         try (var ps = connection.prepareStatement(sql)) {
            ps.setShort(1, home.getId());
            ps.setShort(2, away.getId());
            ps.setByte(3, Byte.parseByte(homeGoals));
            ps.setByte(4, Byte.parseByte(awayGoals));
            ps.setByte(5, seasonId);
            ps.setByte(6, Byte.parseByte(matchDay));
            ps.setTimestamp(7, dateTime);
            if (ps.executeUpdate() > 0) {
               try (var rs = ps.getGeneratedKeys()) {
                  if (rs.next()) {
                     final long matchId = rs.getLong(1);
                     return new Match(matchId, Byte.parseByte(matchDay), season, dateTime.toLocalDateTime(), home, Byte.parseByte(homeGoals), away, Byte.parseByte(awayGoals), null);
                  }
               }
            }
         }
         try (var ps = connection.prepareStatement("select currval('seq_matches') as match_id")) {
            try (var rs = ps.executeQuery()) {
               if (rs.next()) {
                  return new Match(rs.getLong("match_id"), Byte.parseByte(matchDay), season, dateTime.toLocalDateTime(), home, Byte.parseByte(homeGoals), away, Byte.parseByte(awayGoals),
                        null);
               }
            }
         }
         throw new IllegalArgumentException("Unable to get the match Id");
      } catch (SQLException e) {
         throw new IllegalArgumentException(e);
      }
   }

   public static void insertPlayers(final List<Player> players, final short teamId, final byte seasonId) {
      final var sqlInsert = "insert into player values (NEXT VALUE FOR seq_players, ?,?,?,?,?)";
      try (var connection = H2Config.INSTANCE.getConnection()) {
         players.forEach(player -> {
            if (!checkIfPlayerIsAlreadyAddedToTeam(player.getName(), player.getBirthDate(), teamId, seasonId, connection)) {
               try (var ps = connection.prepareStatement(sqlInsert)) {
                  ps.setString(1, player.getName());
                  Optional.ofNullable(player.getBirthDate()).map(Date::valueOf).ifPresentOrElse(x -> {
                     try {
                        ps.setDate(2, x);
                     } catch (SQLException ex) {
                        throw new IllegalArgumentException(ex);
                     }
                  }, () -> {
                     try {
                        ps.setDate(2, null);
                     } catch (SQLException ex) {
                        throw new IllegalArgumentException(ex);
                     }
                  });
                  ps.setString(3, "");
                  ps.setShort(4, teamId);
                  ps.setByte(5, seasonId);
                  ps.executeUpdate();
               } catch (SQLException ex) {
                  throw new IllegalArgumentException(ex);
               }
            }
         });
         final var sqlInsertExecution = """ 
               insert into execution(datetime_of_execution,team_id,team_id_last_opponent) 
                 values (CURRENT_TIMESTAMP,?,(select opponent_team from team_last_opponent where current_team=?))
               """;
         try (var ps = connection.prepareStatement(sqlInsertExecution)) {
            ps.setShort(1, teamId);
            ps.setShort(2, teamId);
            ps.executeUpdate();
            try (var ps2 = connection.prepareStatement("select max(id) as maxExId from execution")) {
               try (var rs = ps2.executeQuery()) {
                  if (rs.next()) {
                     final long executionId = rs.getLong("maxExId");
                     System.out.println(STR."::::::: Executed an insert with id: \{executionId} :::::::: ");
                     setPlayerId(players, teamId, seasonId, connection);
                     final var sqlInsertChange = "insert into player_change_after_execution values(?,?,?,?,?)";
                     players.forEach(player -> {
                        try (var ps3 = connection.prepareStatement(sqlInsertChange)) {
                           ps3.setInt(1, player.getId());
                           ps3.setLong(2, executionId);
                           ps3.setShort(3, player.getMinutesPlayed());
                           ps3.setByte(4, player.getMatchesPlayed());
                           ps3.setByte(5, player.getGoalsScored());
                           ps3.executeUpdate();
                           System.out.println(STR.":::: Adding change to player with id: \{player.getId()} and name: \{player.getName()} :::::::::");
                        } catch (SQLException ex) {
                           throw new IllegalArgumentException(ex);
                        }
                     });
                     System.out.println("::::::::::: Players addition success ::::::::::: ");
                  }
               }
            }
         }
      } catch (SQLException ex) {
         throw new IllegalArgumentException(ex);
      }
   }

   public void setPlayerId(final List<Player> players, final short teamId, final byte seasonId, final Connection connection) {
      try {
         if (connection != null) {
            setPlayerIdFromDB(players, teamId, seasonId, connection);
         } else {
            try (var conn = H2Config.INSTANCE.getConnection()) {
               setPlayerIdFromDB(players, teamId, seasonId, conn);
            }
         }
      } catch (SQLException ex) {
         throw new IllegalArgumentException(ex);
      }
   }

   public Collection<MatchPlayer> getMatchPlayers(final Match match) {
      final var matchPlayers = new HashSet<MatchPlayer>();
      try (var connection = H2Config.INSTANCE.getConnection()) {
         try (var ps = connection.prepareStatement("select * from match_players mp, match m where m.id=mp.match_id and mp.match_id=?")) {
            ps.setLong(1, match.id());
            try (var rs = ps.executeQuery()) {
               while (rs.next()) {
                  final var matchPlayer = new MatchPlayer(
                     match, getPlayerById(rs.getInt("mp.player_id"), match.season().id(), connection),
                        rs.getByte("mp.minutes_played"), rs.getByte("mp.goals_converted"),
                        rs.getBoolean("mp.was_starting"), rs.getBoolean("mp.was_substitute"), rs.getBoolean("mp.was_not_present")
                  );
                  matchPlayers.add(matchPlayer);
               }
            }
         }
      } catch (SQLException ex) {
         throw new IllegalArgumentException(ex);
      }
      return matchPlayers;
   }

//   private Player getPlayerById(final int playerId, final Connection connection) throws SQLException {
//      final Connection conn;
//      final boolean wasNewConnection;
//      if (connection == null) {
//         conn = H2Config.INSTANCE.getConnection();
//         wasNewConnection = true;
//      } else {
//         conn = connection;
//         wasNewConnection = false;
//      }
//         try (var ps = conn.prepareStatement("select * from player where id=?")) {
//            ps.setInt(1, playerId);
//            try (var rs = ps.executeQuery()) {
//               if (rs.next()) {
//                  final var player = new Player(rs.getInt(1), rs.getString(2), null, rs.getShort(4))
//               }
//            }
//         }
//      }
//   }

   private boolean checkIfPlayerIsAlreadyAddedToTeam(final String playerName, final LocalDate birthDate, final short teamId,
         final byte seasonId, final Connection connection) {
      final String sql;
      if (birthDate == null) {
         sql = "select count(*) cnt from player where upper(trim(name)) = ? and team_id = ? and season_id = ?";
      } else {
      sql = "select count(*) cnt from player where upper(trim(name)) = ? and team_id = ? and season_id = ? and birthdate = ?";
      }
      try (var ps = connection.prepareStatement(sql)) {
         ps.setString(1, playerName.trim().toUpperCase());
         ps.setShort(2, teamId);
         ps.setByte(3, seasonId);
         if (birthDate != null) {
            ps.setDate(4, java.sql.Date.valueOf(birthDate));
         }
         try (var rs = ps.executeQuery()) {
            if (rs.next()) {
               final var result = rs.getInt("cnt") > 0;
               if (result) {
                  System.out.println(STR."::::::: Player with name: \{playerName} and birthdate: \{birthDate} is already added to database ::::::::");
               } else {
                  System.out.println(STR."::::::: Player with name: \{playerName} and birthdate: \{birthDate} is going to add to the database ::::::::");
               }
               return result;
            }
         }
         System.out.println(STR."::::::: Player with name: \{playerName} and birthdate: \{birthDate} is going to add to the database ::::::::");
         return false;
      } catch (SQLException ex) {
         throw new IllegalArgumentException(ex);
      }
   }

   private void setPlayerIdFromDB(final List<Player> players, final short teamId, final byte seasonId, final Connection connection) throws SQLException {
      final var sql = "select id from player where upper(trim(name)) = ? and team_id = ? and season_id = ?";
      players.forEach(player -> {
         try (var ps = connection.prepareStatement(sql)) {
            ps.setString(1, player.getName().trim().toUpperCase());
            ps.setShort(2, teamId);
            ps.setByte(3, seasonId);
            try (var rs = ps.executeQuery()) {
               while (rs.next()) {
                  final var playerId = rs.getInt(1);
                  player.setId(playerId);
                  System.out.println(STR.":::::: Setting id \{player} to player: \{player.getName()} ::::::::");
               }
            }
         } catch (SQLException ex) {
            throw new IllegalArgumentException(ex);
         }
      });
   }
}
