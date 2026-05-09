package com.team10.domain;

import com.team10.sports.Sport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.Serializable;

public class Lineup implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Team team;
    private final List<Player> players;

    public Lineup(Team team, List<Player> players, Sport sport) {
        validate(team, players, sport);
        this.team = team;
        this.players = new ArrayList<Player>(players);
    }

    private void validate(Team team, List<Player> players, Sport sport) {
        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null.");
        }
        if (players == null) {
            throw new IllegalArgumentException("Players list cannot be null.");
        }
        if (sport == null) {
            throw new IllegalArgumentException("Sport cannot be null.");
        }
        if (!sport.isValidPlayerCount(players.size())) {
            throw new IllegalArgumentException("Invalid lineup size for selected sport.");
        }

        Set<Player> uniquePlayers = new HashSet<Player>(players);
        if (uniquePlayers.size() != players.size()) {
            throw new IllegalArgumentException("Duplicate players are not allowed in lineup.");
        }

        for (Player player : players) {
            if (player == null) {
                throw new IllegalArgumentException("Lineup cannot contain null player.");
            }
            if (!team.containsPlayer(player)) {
                throw new IllegalArgumentException("Player does not belong to the team.");
            }
            if (!player.isAvailable()) {
                throw new IllegalArgumentException("Injured player cannot be included in lineup.");
            }
        }
    }

    public Team getTeam() {
        return team;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }
}