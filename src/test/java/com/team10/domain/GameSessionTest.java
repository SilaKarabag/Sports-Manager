package com.team10.domain;

import com.team10.sports.FootballSport;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameSessionTest {

    private List<Team> createTwoTeams() {
        List<Team> teams = new ArrayList<>();
        teams.add(TestDataFactory.createTeam());
        teams.add(TestDataFactory.createTeam());
        return teams;
    }

    @Test
    void testAdvanceWeekAdvancesLeague() {
        List<Team> teams = createTwoTeams();

        FootballSport sport = new FootballSport();
        League league = new League(teams, sport);

        GameSession session = new GameSession();
        session.setSport(sport);
        session.setLeague(league);

        int before = league.getCurrentWeek();

        session.advanceWeek();

        int after = league.getCurrentWeek();

        assertEquals(before + 1, after);
    }

    @Test
    void testAdvanceWeekWithoutLeagueThrows() {
        GameSession session = new GameSession();

        assertThrows(IllegalStateException.class, session::advanceWeek);
    }

}