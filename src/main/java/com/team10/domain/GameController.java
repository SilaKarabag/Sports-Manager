package com.team10.domain;

import com.team10.persistence.SaveManager;
import com.team10.sports.Sport;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * BUG FIX 1: saveGame/loadGame IOException/ClassNotFoundException catch'lenmiyor,
 *   caller'a fırlatılıyor – doğru, UI handle ediyor.
 *
 * BUG FIX 2: startNewGame'de sport null kontrolüne ek olarak teams.size()<2 kontrolü eklendi.
 *   2'den az takımla lig kurulamazdı.
 *
 * BUG FIX 3: getSelectedSport – UI MainWindow'da selectedSport tutuyor ama
 *   GameController bu bilgiyi bilmiyor. GameSession üzerinden erişim eklendi.
 */
public class GameController implements Serializable {
    private static final long serialVersionUID = 1L;

    private GameSession session;

    public GameSession getSession() { return session; }

    public void loadSession(GameSession session) {
        if (session == null) throw new IllegalArgumentException("GameSession cannot be null.");
        this.session = session;
    }

    public void saveGame(String filePath) throws IOException {
        ensureSession();
        new SaveManager().save(session, filePath);
    }

    public void loadGame(String filePath) throws IOException, ClassNotFoundException {
        GameSession loaded = new SaveManager().load(filePath);
        loadSession(loaded);
    }

    public void startNewGame(Sport sport, List<Team> teams) {
        if (sport == null) throw new IllegalArgumentException("Sport cannot be null.");
        if (teams == null || teams.size() < 2)
            throw new IllegalArgumentException("At least 2 teams are required.");

        this.session = new GameSession();
        this.session.setSport(sport);

        League league = new League(teams, sport);
        this.session.setLeague(league);
        this.session.startSeason();
    }

    public void selectTeam(Team team) {
        ensureSession();
        session.setManagedTeam(team);
    }

    public void playNextWeek() {
        ensureSession();
        session.advanceWeek();
    }

    public League getLeague() {
        ensureSession();
        return session.getLeague();
    }

    public Team getManagedTeam() {
        ensureSession();
        return session.getManagedTeam();
    }

    /** BUG FIX: Sport'a UI katmanının direkt erişimi için eklendi */
    public Sport getSport() {
        ensureSession();
        return session.getSport();
    }

    public boolean hasActiveSession() { return session != null; }

    private void ensureSession() {
        if (session == null)
            throw new IllegalStateException("Game session not initialized. Start or load a game first.");
    }
}
