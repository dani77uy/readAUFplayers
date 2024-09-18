package com.tipsuy.auf.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tipsuy.auf.config.H2Config;
import com.tipsuy.auf.domain.model.Season;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SeasonDAO {

   List<Season> getAllSeasons(final Connection connection) throws SQLException {
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
   }

   Season getSeasonById(final byte id, final Connection connection) throws SQLException {
      try (var ps = connection.prepareStatement("select * from season where id = ?")) {
         ps.setByte(1, id);
         try (var rs = ps.executeQuery()) {
            if (rs.next()) {
               return new Season(rs.getByte(1), rs.getShort(2), rs.getString(3));
            }
         }
      }
      return null;
   }
}
