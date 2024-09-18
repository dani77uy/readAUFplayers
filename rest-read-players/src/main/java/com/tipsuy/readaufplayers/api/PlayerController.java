package com.tipsuy.readaufplayers.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tipsuy.readaufplayers.domain.Player;
import com.tipsuy.readaufplayers.domain.PlayerExecutionDifference;
import com.tipsuy.readaufplayers.service.PlayerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/player")
@RestController
public class PlayerController {

   private final PlayerService playerService;

   @GetMapping("/readFromAugPage/{seasonId}/{customTeamId}")
   public ResponseEntity<Map<Player, PlayerExecutionDifference>> readPlayers(@PathVariable("seasonId") @NonNull final Short seasonId, @PathVariable("customTeamId") @Nullable final Short customTeamId) {
      return ResponseEntity.of(playerService.readPlayers(seasonId, customTeamId));
   }
}
