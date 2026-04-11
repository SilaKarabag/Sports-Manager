package com.team10.domain;

import com.team10.sports.Sport;
import java.util.List;

public class GameController {

    private GameSession session;

    // START NEW GAME
    public void startNewGame(Sport sport, List<Team> teams) {

        session = new GameSession();
        session.setSport(sport);

        League league = new League(teams, sport);

        session.setLeague(league);
        session.startSeason();
    }

    // SELECT TEAM
    public void selectTeam(Team team) {
        ensureSession();
        session.setManagedTeam(team);
    }

    // NEXT WEEK
    public void playNextWeek() {
        ensureSession();
        session.advanceWeek();
    }

    public League getLeague() {
        ensureSession();
        return session.getLeague();
    }

    private void ensureSession() {
        if (session == null) {
            throw new IllegalStateException("Game not started.");
        }
    }
}