package com.tipsuy.readaufplayers.api;

import com.tipsuy.readaufplayers.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/player")
@RestController
public class PlayerController {

  private final PlayerService playerService;

  @GetMapping(value = "/readFromAufPage/{seasonId}/{teamId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> readPlayers(@PathVariable("seasonId") @NonNull final Short seasonId, @PathVariable("teamId") @Nullable final Short teamId) {
    if (teamId == null || NumberUtils.SHORT_ZERO.equals(teamId)) {
      return ResponseEntity.of(playerService.readPlayersInAllTeams(seasonId));
    } else {
      return ResponseEntity.of(playerService.readPlayers(seasonId, teamId));
    }
  }
}
