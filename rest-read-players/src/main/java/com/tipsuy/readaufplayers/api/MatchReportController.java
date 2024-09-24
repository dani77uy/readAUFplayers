package com.tipsuy.readaufplayers.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tipsuy.readaufplayers.domain.dto.MatchDTO;
import com.tipsuy.readaufplayers.service.MatchService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/match")
@RestController
public class MatchReportController {

   private final MatchService matchService;

   @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> create(@RequestBody final MatchDTO matchDTO) {
      try {
         return ResponseEntity.ok(matchService.createMatch(matchDTO));
      } catch (Exception e) {
         return ResponseEntity.internalServerError().body(e.getMessage());
      }
   }

   @PatchMapping(value = "/setMatchGoals", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<?> setMatchGoals(@RequestParam(value = "matchId") final long matchId, @RequestParam(value = "homeGoals") final byte homeGoals,
         @RequestParam(value = "awayGoals") final byte awayGoals) {
      try {
         return ResponseEntity.ok(matchService.addGoalsToMatch(matchId, homeGoals, awayGoals));
      } catch (Exception e) {
         return ResponseEntity.internalServerError().body(e.getMessage());
      }
   }

   @PatchMapping(value = "/assignPlayersToMatchesBySeasonAndMatchDay/{seasonId}/{matchDay}")
   public ResponseEntity<?> assignPlayersToMatchesBySeasonAndMatchDay(@PathVariable(value = "seasonId") final short seasonId,
         @PathVariable(value = "matchDay") final byte matchDay) {

   }
}
