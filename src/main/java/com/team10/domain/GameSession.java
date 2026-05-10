package com.team10.domain;

import com.team10.sports.Sport;
import java.io.Serializable;

/**
 * Oyunun o anki tüm durumunu (Lig, Seçili Takım, Spor Dalı) saklayan sınıftır.
 * Kaydetme/Yükleme işlemleri bu obje üzerinden yapılır.
 */
public class GameSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private Sport sport;
    private League league;
    private Team managedTeam;

    /**
     * Sezonun başlatılması için gerekli kontrolleri yapar.
     */
    public void startSeason() {
        if (sport == null || league == null) {
            throw new IllegalStateException("Sport and League must be set before starting the season.");
        }
    }

    /**
     * Ligde bir sonraki haftaya geçiş yapar.
     */
    public void advanceWeek() {
        if (league == null) {
            throw new IllegalStateException("League must be set before advancing week.");
        }
        if (league.isLeagueFinished()) {
            throw new IllegalStateException("The league is already finished.");
        }
        league.playNextWeek();
    }

    /**
     * Sezonun bitip bitmediğini kontrol eder.
     */
    public boolean isSeasonFinished() {
        return league != null && league.isLeagueFinished();
    }

    // GETTER VE SETTER METODLARI

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Team getManagedTeam() {
        return managedTeam;
    }

    public void setManagedTeam(Team managedTeam) {
        this.managedTeam = managedTeam;
    }
}