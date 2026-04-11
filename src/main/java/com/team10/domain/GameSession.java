package com.team10.domain;

import com.team10.sports.Sport;

public class GameSession {

    private Sport sport;
    private League league;
    private Team managedTeam;

    public void startSeason() {
        if (sport == null || league == null) {
            throw new IllegalStateException("Sport and League must be set.");
        }
    }

    public void advanceWeek() {
        league.playNextWeek();
    }

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
