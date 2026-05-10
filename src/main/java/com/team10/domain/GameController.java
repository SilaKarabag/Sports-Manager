package com.team10.domain;

import com.team10.sports.Sport;
import java.util.List;
import java.io.Serializable;

/**
 * Kullanıcı arayüzü (UI) ile iş mantığı (Domain) arasındaki ana köprüdür.
 */
public class GameController implements Serializable {
    private static final long serialVersionUID = 1L;

    private GameSession session;

    public GameSession getSession() {
        return session;
    }

    /**
     * Kaydedilmiş bir oyunu (Session) sisteme yükler.
     * UI tarafında SaveManager kullanıldıktan sonra çağrılır.
     */
    public void loadSession(GameSession session) {
        if (session == null) {
            throw new IllegalArgumentException("GameSession cannot be null.");
        }
        this.session = session;
    }

    /**
     * Yeni bir oyun oturumu başlatır.
     * @param sport Seçilen spor dalı (Football veya Volleyball)
     * @param teams Ligde yer alacak takımlar
     */
    public void startNewGame(Sport sport, List<Team> teams) {
        if (sport == null || teams == null || teams.isEmpty()) {
            throw new IllegalArgumentException("Sport and Teams must be valid to start a game.");
        }

        this.session = new GameSession();
        this.session.setSport(sport);

        // Yeni bir lig oluştur ve fikstürü hazırla
        League league = new League(teams, sport);

        this.session.setLeague(league);
        this.session.startSeason();
    }

    /**
     * Kullanıcının yöneteceği takımı seçer.
     */
    public void selectTeam(Team team) {
        ensureSession();
        session.setManagedTeam(team);
    }

    /**
     * Ligde bir sonraki haftanın maçlarını oynatır.
     */
    public void playNextWeek() {
        ensureSession();
        session.advanceWeek();
    }

    /**
     * Mevcut lig durumunu döner.
     */
    public League getLeague() {
        ensureSession();
        return session.getLeague();
    }

    /**
     * Kullanıcının yönettiği takımı döner.
     */
    public Team getManagedTeam() {
        ensureSession();
        return session.getManagedTeam();
    }

    /**
     * Oturumun (Session) başlatılıp başlatılmadığını kontrol eder.
     */
    private void ensureSession() {
        if (session == null) {
            throw new IllegalStateException("Game session has not been initialized. Start a new game or load a save.");
        }
    }
}