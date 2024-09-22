package com.tipsuy.readaufplayers.api;

import com.tipsuy.readaufplayers.domain.Team;
import com.tipsuy.readaufplayers.domain.dto.TeamDTO;
import com.tipsuy.readaufplayers.service.TeamService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/team")
@RestController
public class TeamController {

   private final TeamService teamService;

   @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Team> add(@RequestBody final TeamDTO teamDTO) {
      return ResponseEntity.ok(teamService.add(teamDTO));
   }

   @PostMapping(value = "/addAll", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<Team>> addAll(@RequestBody final List<TeamDTO> teams) {
      return ResponseEntity.ok(teamService.addAll(teams));
   }

   @GetMapping(value = "/findAll", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<List<Team>> findAll() {
      return ResponseEntity.ok(teamService.findAll());
   }

}
