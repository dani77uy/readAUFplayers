package com.tipsuy.auf.service;

import com.tipsuy.auf.config.H2Config;
import com.tipsuy.auf.domain.Match;
import com.tipsuy.auf.domain.Player;
import com.tipsuy.auf.domain.Season;
import com.tipsuy.auf.domain.Team;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReadDB {

  public List<Season> getAllSeasons() {
      try (var connection = H2Config.INSTANCE.getConnection()) {
         try (var ps = connection.prepareStatement("select * from season order by year_of_season desc")) {
            try (var rs = ps.executeQuery()) {
               final var seasons = new ArrayList<Season>();
               while (rs.next()) {
                  final var season = new Season(rs.getByte("id"), rs.getShort("year_of_season"), rs.getString("tournament_name"));
                  seasons.add(season);
               }
               return seasons;
            }
         }
      } catch (SQLException e) {
         throw new IllegalArgumentException(e);
      }
   }

   public List<Team> getAllTeamsByTournament(final byte tournamentId) {
      try (var connection = H2Config.INSTANCE.getConnection()) {
         try (var ps = connection.prepareStatement("select * from team where season_that_belong = ?")) {
            ps.setByte(1, tournamentId);
            try (var rs = ps.executeQuery()) {
               final var teams = new ArrayList<Team>();
               while (rs.next()) {
                  final var id = rs.getShort("id");
                  final var name = rs.getString("name");
                  final var url = rs.getString("url");
                  final var team = new Team(id, name, url, null);
                  teams.add(team);
               }
               return teams;
            }
         }
      } catch (SQLException e) {
         throw new IllegalArgumentException(e);
      }
   }

   public Team getTeamById(final short teamId, final byte tournamentId) {
      final var sqlTeams = "select * from team where id = ? and season_that_belong = ?";
      try (var connection = H2Config.INSTANCE.getConnection()) {
         try (var ps = connection.prepareStatement(sqlTeams)) {
            ps.setShort(1, teamId);
            ps.setByte(2, tournamentId);
            try (var rs = ps.executeQuery()) {
               if (rs.next()) {
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
                           final var player = new Player(rs.getInt(1), rs.getString(2),
                               Optional.ofNullable(rs.getDate(3)).map(Date::toLocalDate).orElse(null), totalParticipations, totalMinutes, totalGoals);
                           players.add(player);
                        }
                     }
                  }
                  return new Team(rs.getShort(1), rs.getString(2), rs.getString(3), players);
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
        try (var ps = connection.prepareStatement(sql)) {
           ps.setShort(1, home.id());
           ps.setShort(2, away.id());
           ps.setByte(3, Byte.parseByte(homeGoals));
           ps.setByte(4, Byte.parseByte(awayGoals));
           ps.setByte(5, seasonId);
           ps.setByte(6, Byte.parseByte(matchDay));
           ps.setTimestamp(7, dateTime);
           if (ps.executeUpdate() > 0) {
              try (var rs = ps.getGeneratedKeys()) {
                 if (rs.next()) {
                    final long matchId = rs.getLong(1);
                    return new Match(matchId, Byte.parseByte(matchDay), dateTime.toLocalDateTime(), home, Byte.parseByte(homeGoals), away , Byte.parseByte(awayGoals), null);
                 }
              }
           }
           throw new IllegalArgumentException("Unable to insert the match");
        }
     } catch (SQLException e) {
        throw new IllegalArgumentException(e);
     }
   }
}
