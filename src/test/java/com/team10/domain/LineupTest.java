package com.team10.domain;

import com.team10.sports.FootballSport;
import com.team10.sports.Sport;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LineupTest {

    private List<Player> createElevenPlayers() {
        List<Player> players = new ArrayList<Player>();
        for (int i = 1; i <= 11; i++) {
            players.add(new Player("Player" + i, "Position" + i, 70 + i));
        }
        return players;
    }

    private Team createTeamWithPlayers(List<Player> players) {
        Team team = new Team("Team A");
        for (Player player : players) {
            team.addPlayer(player);
        }
        return team;
    }

    @Test
    void shouldCreateValidLineup() {
        Sport sport = new FootballSport();
        List<Player> players = createElevenPlayers();
        Team team = createTeamWithPlayers(players);

        Lineup lineup = new Lineup(team, players, sport);

        assertEquals(team, lineup.getTeam());
        assertEquals(11, lineup.getPlayers().size());
    }

    @Test
    void shouldThrowExceptionForInvalidPlayerCount() {
        Sport sport = new FootballSport();
        List<Player> players = createElevenPlayers();
        players.remove(0);
        Team team = createTeamWithPlayers(players);

        assertThrows(IllegalArgumentException.class,
                () -> new Lineup(team, players, sport));
    }

    @Test
    void shouldThrowExceptionForDuplicatePlayers() {
        Sport sport = new FootballSport();
        List<Player> players = createElevenPlayers();
        Team team = createTeamWithPlayers(players);

        List<Player> invalidLineup = new ArrayList<Player>(players);
        invalidLineup.set(10, invalidLineup.get(0));

        assertThrows(IllegalArgumentException.class,
                () -> new Lineup(team, invalidLineup, sport));
    }

    @Test
    void shouldThrowExceptionForInjuredPlayer() {
        Sport sport = new FootballSport();
        List<Player> players = createElevenPlayers();
        Team team = createTeamWithPlayers(players);
        players.get(0).injureForMatches(2);

        assertThrows(IllegalArgumentException.class,
                () -> new Lineup(team, players, sport));
    }

    @Test
    void shouldThrowExceptionWhenPlayerDoesNotBelongToTeam() {
        Sport sport = new FootballSport();
        List<Player> players = createElevenPlayers();
        Team team = new Team("Team A");

        for (int i = 0; i < 10; i++) {
            team.addPlayer(players.get(i));
        }

        assertThrows(IllegalArgumentException.class,
                () -> new Lineup(team, players, sport));
    }

    @Test
    void shouldThrowExceptionForNullTeam() {
        Sport sport = new FootballSport();
        List<Player> players = createElevenPlayers();

        assertThrows(IllegalArgumentException.class,
                () -> new Lineup(null, players, sport));
    }

    @Test
    void shouldThrowExceptionForNullPlayers() {
        Sport sport = new FootballSport();
        Team team = new Team("Team A");

        assertThrows(IllegalArgumentException.class,
                () -> new Lineup(team, null, sport));
    }

    @Test
    void shouldThrowExceptionForNullSport() {
        List<Player> players = createElevenPlayers();
        Team team = createTeamWithPlayers(players);

        assertThrows(IllegalArgumentException.class,
                () -> new Lineup(team, players, null));
    }
}