package com.team10.domain;

import com.team10.sports.Sport;
import java.io.Serializable;

public class TeamRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Team team;
    private int matchesPlayed;
    private int wins;
    private int draws;
    private int losses;
    private int points;
    private int scored;   // Atılan sayı/gol
    private int conceded; // Yenilen sayı/gol

    public TeamRecord(Team team) {
        if (team == null) throw new IllegalArgumentException("Team cannot be null.");
        this.team = team;
    }

    /**
     * Maç bittiğinde çağrılır — istatistikleri ve puanı günceller.
     * League.java bu metodu "addMatchResult" adıyla çağırıyor.
     */
    public void addMatchResult(int goalsFor, int goalsAgainst, Sport sport) {
        this.matchesPlayed++;
        this.scored += goalsFor;
        this.conceded += goalsAgainst;

        if (goalsFor > goalsAgainst) {
            this.wins++;
        } else if (goalsFor == goalsAgainst) {
            this.draws++;
        } else {
            this.losses++;
        }

        this.points += sport.calculatePointsFromScore(goalsFor, goalsAgainst);
    }

    public int getGoalDifference() {
        return scored - conceded;
    }

    public Team getTeam()          { return team; }
    public int getMatchesPlayed()  { return matchesPlayed; }
    public int getWins()           { return wins; }
    public int getDraws()          { return draws; }
    public int getLosses()         { return losses; }
    public int getPoints()         { return points; }
    public int getScored()         { return scored; }
    public int getConceded()       { return conceded; }
}