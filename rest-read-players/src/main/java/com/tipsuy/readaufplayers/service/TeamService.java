package com.tipsuy.readaufplayers.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tipsuy.readaufplayers.dao.SequenceRepository;
import com.tipsuy.readaufplayers.dao.TeamRepository;
import com.tipsuy.readaufplayers.domain.Team;
import com.tipsuy.readaufplayers.domain.dto.TeamDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TeamService {

   private final TeamRepository teamRepository;

   private final SequenceRepository sequenceRepository;

   public Team add(final TeamDTO teamDTO) {
      return teamRepository.save(map(teamDTO));
   }

   public List<Team> addAll(final List<TeamDTO> teamDTOList) {
      final List<Team> teams = new ArrayList<>();
      for (TeamDTO teamDTO : teamDTOList) {
         teams.add(map(teamDTO));
      }
      return teamRepository.saveAll(teams);
   }

   public List<Team> findAll() {
      return teamRepository.findAll();
   }

   private Team map(final TeamDTO teamDTO) {
      final Team team = new Team();
      team.setTeamId(sequenceRepository.getNextSequenceValue("teams").shortValue());
      team.setTeamName(teamDTO.name());
      team.setUrl(teamDTO.url());
      return team;
   }
}
