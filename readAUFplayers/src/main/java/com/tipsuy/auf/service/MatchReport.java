package com.tipsuy.auf.service;

import com.tipsuy.auf.dao.ReadDB;
import com.tipsuy.auf.domain.dto.MatchReportDTO;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MatchReport {

   public MatchReportDTO generateMatchReport(final long matchId) {
      final var matchPlayers = ReadDB.getMatchPlayers(matchId);
   }
}
