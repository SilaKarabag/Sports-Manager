package com.team10.domain;

import com.team10.persistence.SaveManager;
import com.team10.sports.Sport;
import java.io.*;
import java.util.List;

public class GameController implements Serializable {
    private static final long serialVersionUID = 1L;

    private GameSession session;

    public GameSession getSession()   { return session; }
    public boolean hasActiveSession() { return session != null; }

    public void loadSession(GameSession s) {
        if (s == null) throw new IllegalArgumentException("Session cannot be null.");
        this.session = s;
    }

    public void saveGame(String path) throws IOException {
        ensureSession();
        new SaveManager().save(session, path);
    }

    public void loadGame(String path) throws IOException, ClassNotFoundException {
        loadSession(new SaveManager().load(path));
    }

    public void startNewGame(Sport sport, List<Team> teams) {
        if (sport == null) throw new IllegalArgumentException("Sport cannot be null.");
        if (teams == null || teams.size() < 2) throw new IllegalArgumentException("Need at least 2 teams.");
        this.session = new GameSession();
        this.session.setSport(sport);
        League league = new League(teams, sport);
        this.session.setLeague(league);
        this.session.startSeason();
    }

    public void selectTeam(Team team) { ensureSession(); session.setManagedTeam(team); }
    public void playNextWeek()        { ensureSession(); session.advanceWeek(); }
    public League getLeague()         { ensureSession(); return session.getLeague(); }
    public Team   getManagedTeam()    { ensureSession(); return session.getManagedTeam(); }
    /** Returns null if no team has been set as managed */
    public Team   getManagedTeamOrNull() { return session != null ? session.getManagedTeam() : null; }

    /** CRITICAL FIX: UI bu metodu çağırıyor ama yoktu → NullPointerException */
    public Sport getSport() {
        ensureSession();
        return session.getSport();
    }

    private void ensureSession() {
        if (session == null)
            throw new IllegalStateException("No active game session. Start or load a game first.");
    }
}
