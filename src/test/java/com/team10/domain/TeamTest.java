package com.team10.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    @Test
    void shouldCreateTeamWithCorrectName() {
        Team team = new Team("Team A");

        assertEquals("Team A", team.getName());
        assertTrue(team.getRoster().isEmpty());
        assertNull(team.getCoach());
    }

    @Test
    void shouldThrowExceptionForBlankTeamName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Team(""));
    }

    @Test
    void shouldAddPlayerToRoster() {
        Team team = new Team("Team A");
        Player player = new Player("Ali", "Forward", 80);

        team.addPlayer(player);

        assertEquals(1, team.getRoster().size());
        assertTrue(team.containsPlayer(player));
    }

    @Test
    void shouldThrowExceptionWhenAddingNullPlayer() {
        Team team = new Team("Team A");

        assertThrows(IllegalArgumentException.class,
                () -> team.addPlayer(null));
    }

    @Test
    void shouldThrowExceptionWhenAddingDuplicatePlayer() {
        Team team = new Team("Team A");
        Player player = new Player("Ali", "Forward", 80);

        team.addPlayer(player);

        assertThrows(IllegalArgumentException.class,
                () -> team.addPlayer(player));
    }

    @Test
    void shouldSetCoach() {
        Team team = new Team("Team A");
        Coach coach = new Coach("Kaya", 2);

        team.setCoach(coach);

        assertEquals(coach, team.getCoach());
    }

    @Test
    void shouldThrowExceptionWhenSettingNullCoach() {
        Team team = new Team("Team A");

        assertThrows(IllegalArgumentException.class,
                () -> team.setCoach(null));
    }

    @Test
    void shouldReturnOnlyAvailablePlayers() {
        Team team = new Team("Team A");
        Player p1 = new Player("Ali", "Forward", 80);
        Player p2 = new Player("Veli", "Defender", 75);
        p2.injureForMatches(2);

        team.addPlayer(p1);
        team.addPlayer(p2);

        List<Player> availablePlayers = team.getAvailablePlayers();

        assertEquals(1, availablePlayers.size());
        assertTrue(availablePlayers.contains(p1));
        assertFalse(availablePlayers.contains(p2));
    }
}