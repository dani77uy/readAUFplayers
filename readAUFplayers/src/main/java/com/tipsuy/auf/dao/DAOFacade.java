package com.tipsuy.auf.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.tipsuy.auf.config.H2Config;
import com.tipsuy.auf.domain.model.Season;
import com.tipsuy.auf.domain.model.Team;

public enum DAOFacade {

   INSTANCE;

   private final Connection connection;

   DAOFacade() {
      connection = H2Config.INSTANCE.getConnection();
   }

   public List<Season> getAllSeasons() {
      try {
         return SeasonDAO.getAllSeasons(connection);
      } catch (SQLException e) {
         throw new IllegalArgumentException(e);
      }
   }

   public Season getSeasonById(final byte seasonId) {
      try {
         return SeasonDAO.getSeasonById(seasonId, connection);
      } catch (SQLException e) {
         throw new IllegalArgumentException(e);
      }
   }

   public List<Team> getAllTeamsByTournament(final byte tournamentId) {
      try {
         return TeamDAO.getAllTeamsByTournament(tournamentId, connection);
      } catch (SQLException e) {
         throw new IllegalArgumentException(e);
      }
   }

   public List<Team> getTeamById() {
      try {
         return TeamDAO.getAllTeams(connection);
      } catch (SQLException e) {
         throw new IllegalArgumentException(e);
      }
   }

}
