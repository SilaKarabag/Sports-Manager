package com.team10.domain;

import com.team10.sports.Sport;
import com.team10.sports.VolleyballSport;
import java.io.Serializable;

/**
 * BUG FIX: Voleybolda beraberlik olamaz (draws her zaman 0 olmalı).
 * Orijinal kod futbol kurallarını voleybol için de uyguluyordu.
 */
public class TeamRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Team team;
    private int matchesPlayed;
    private int wins;
    private int draws;
    private int losses;
    private int points;
    private int scored;
    private int conceded;

    public TeamRecord(Team team) {
        if (team == null) throw new IllegalArgumentException("Team cannot be null.");
        this.team = team;
    }

    public void addMatchResult(int goalsFor, int goalsAgainst, Sport sport) {
        this.matchesPlayed++;
        this.scored   += goalsFor;
        this.conceded += goalsAgainst;

        if (goalsFor > goalsAgainst) {
            this.wins++;
        } else if (goalsFor < goalsAgainst) {
            this.losses++;
        } else {
            // BUG FIX: Voleybolda beraberlik olamaz.
            // Voleybol için bu dal hiç çalışmamalı; savunma katmanı olarak bırakıldı.
            if (sport instanceof VolleyballSport) {
                // Bu durum teorik olarak oluşmamalı (Match içinde önlenmiş)
                this.losses++; // Voleybolda kazanamayan kaybeder
            } else {
                this.draws++;
            }
        }

        this.points += sport.calculatePointsFromScore(goalsFor, goalsAgainst);
    }

    public int getGoalDifference() { return scored - conceded; }

    public Team getTeam()         { return team; }
    public int getMatchesPlayed() { return matchesPlayed; }
    public int getWins()          { return wins; }
    public int getDraws()         { return draws; }
    public int getLosses()        { return losses; }
    public int getPoints()        { return points; }
    public int getScored()        { return scored; }
    public int getConceded()      { return conceded; }

    @Override
    public String toString() {
        return String.format("%-15s Pts:%-3d W:%d D:%d L:%d GF:%-3d GA:%-3d GD:%+d",
                team.getName(), points, wins, draws, losses, scored, conceded, getGoalDifference());
    }
}