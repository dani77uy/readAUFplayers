package com.tipsuy.auf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tipsuy.auf.config.H2Config;
import com.tipsuy.auf.domain.model.Team;
import com.tipsuy.auf.domain.model.TeamCurrentRooster;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TeamDAO {

   List<Team> getAllTeamsByTournament(final byte tournamentId, final Connection connection) throws SQLException {
      try (var ps = connection.prepareStatement("select * from team where season_that_belong = ?")) {
         ps.setByte(1, tournamentId);
         return generateTeamList(ps);
      }
   }

   List<Team> getAllTeams(final Connection connection) throws SQLException {
      final var sqlAllTeams = "select * from team order by season_that_belong,name";
      try (var ps = connection.prepareStatement(sqlAllTeams)) {
         return generateTeamList(ps);
      }
   }

   TeamCurrentRooster getCurrentRooster(final short teamId, final byte seasonId, final Connection connection) throws SQLException {
      final var sql1 = "select * from current_team_rooster where team_id = ? and season_id = ?";
      try (var ps = connection.prepareStatement(sql1)) {
         ps.setShort(1, teamId);
         ps.setByte(2, seasonId);
         try (var ps2 = ps.executeQuery()) {
            while (ps2.next()) {
               final var currentRooster = new TeamCurrentRooster();
            }
         }
      }
   }

   Team getTeamById(final short teamId, final byte seasonId, final Connection connection) throws SQLException {
      try (var ps = connection.prepareStatement("select * from team where id = ? and season_that_belong = ?")) {
         ps.setShort(1, teamId);
         ps.setByte(2, seasonId);
         try (var rs = ps.executeQuery()) {
            if (rs.next()) {
               final var team = new Team();
               team.setId(rs.getShort(1));
               team.setName(rs.getString(2));
               
               return team;
            }
         }
      }
   }

   private List<Team> generateTeamList(final PreparedStatement ps) throws SQLException {
      try (var rs = ps.executeQuery()) {
         final var teams = new ArrayList<Team>();
         while (rs.next()) {
            final var id = rs.getShort("id");
            final var name = rs.getString("name");
            final var url = rs.getString("url");
            final var team = new Team(id, name, url);
            teams.add(team);
         }
         return teams;
      }
   }

}
