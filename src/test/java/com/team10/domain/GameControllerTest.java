package com.team10.domain;

import com.team10.sports.FootballSport;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    private List<Team> createTwoTeams() {
        List<Team> teams = new ArrayList<>();
        teams.add(TestDataFactory.createTeam());
        teams.add(TestDataFactory.createTeam());
        return teams;
    }

    @Test
    void testStartNewGameInitializesLeague() {
        GameController controller = new GameController();
        List<Team> teams = createTwoTeams();

        controller.startNewGame(new FootballSport(), teams);

        assertNotNull(controller.getLeague());
        assertEquals(2, controller.getLeague().getTeams().size());
    }

    @Test
    void testStartNewGameWithNullSportThrows() {
        GameController controller = new GameController();
        List<Team> teams = createTwoTeams();

        assertThrows(IllegalArgumentException.class, () ->
            controller.startNewGame(null, teams)
        );
    }

    @Test
    void testStartNewGameWithTooFewTeamsThrows() {
        GameController controller = new GameController();

        List<Team> teams = new ArrayList<>();
        teams.add(TestDataFactory.createTeam());

        assertThrows(IllegalArgumentException.class, () ->
            controller.startNewGame(new FootballSport(), teams)
        );
    }

    @Test
    void testSelectTeamStoresCorrectTeam() {
        GameController controller = new GameController();
        List<Team> teams = createTwoTeams();

        controller.startNewGame(new FootballSport(), teams);

        Team selected = teams.get(0);
        controller.selectTeam(selected);

        assertEquals(selected, controller.getManagedTeam());
    }

    @Test
    void testPlayNextWeekAdvancesWeek() {
        GameController controller = new GameController();
        List<Team> teams = createTwoTeams();

        controller.startNewGame(new FootballSport(), teams);

        int before = controller.getLeague().getCurrentWeek();

        controller.playNextWeek();

        int after = controller.getLeague().getCurrentWeek();

        assertEquals(before + 1, after);
    }

    @Test
    void testPlayNextWeekWithoutStartingGameThrows() {
        GameController controller = new GameController();

        assertThrows(IllegalStateException.class, controller::playNextWeek);
    }

    @Test
    void testGetLeagueWithoutStartingGameThrows() {
        GameController controller = new GameController();

        assertThrows(IllegalStateException.class, controller::getLeague);
    }


    @Test
    void testStartNewGameWithNullTeamsThrows() {
        GameController controller = new GameController();

        assertThrows(IllegalArgumentException.class, () ->
            controller.startNewGame(new FootballSport(), null)
        );
    }

}