package com.team10.domain;

import com.team10.sports.Sport;
import com.team10.sports.VolleyballSport;
import java.util.Random;
import java.io.Serializable;

/**
 * BUG FIX 1: Voleybolda beraberlik olamaz (3-2 kazanır, beraberlik yok).
 *   Orijinal kod seti eşit bırakabiliyordu.
 * BUG FIX 2: calculateLineupPower null/boş lineup durumunda 10 döndürüyor, güzel.
 * BUG FIX 3: Random her serialization'da yeniden oluşturulmalı (transient).
 * BUG FIX 4: Voleybol için "bestOf" mantığı eklendi – 3 seti kazanan maçı kazanır,
 *   gereksiz setler oynanmaz.
 */
public class Match implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Team homeTeam;
    private final Team awayTeam;
    private Lineup homeLineup;
    private Lineup awayLineup;
    private final Sport sport;

    // Random transient – deserialize sonrasında yeniden init edilir
    private transient Random random;

    private int homeScore;
    private int awayScore;
    private int currentQuarter;
    private boolean isFinished;

    public Match(Team homeTeam, Team awayTeam, Sport sport) {
        if (homeTeam == null || awayTeam == null || sport == null)
            throw new IllegalArgumentException("Teams and Sport cannot be null.");
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.sport    = sport;
        this.homeScore = 0;
        this.awayScore = 0;
        this.currentQuarter = 0;
        this.isFinished = false;
        this.random = new Random();
    }

    public Match(Team homeTeam, Team awayTeam, Lineup homeLineup, Lineup awayLineup, Sport sport) {
        this(homeTeam, awayTeam, sport);
        this.homeLineup = homeLineup;
        this.awayLineup = awayLineup;
    }

    private Random getRandom() {
        if (random == null) random = new Random();
        return random;
    }

    public void playNextQuarter() {
        if (isFinished) throw new IllegalStateException("Match is already finished.");

        currentQuarter++;

        int homePower = calculateLineupPower(homeLineup, homeTeam.getCoach());
        int awayPower = calculateLineupPower(awayLineup, awayTeam.getCoach());

        if (sport instanceof VolleyballSport) {
            // BUG FIX: Voleybol – her set kazanan alır, beraberlik YOK.
            // Gücü yüksek olan seti kazanma şansı daha fazla.
            int homeRoll = homePower + getRandom().nextInt(40);
            int awayRoll = awayPower + getRandom().nextInt(40);
            // Eşitlik durumunda bile birinin kazanması şart (tie-break)
            if (homeRoll >= awayRoll) {
                this.homeScore++;
            } else {
                this.awayScore++;
            }

            // BUG FIX: Best-of mantığı – 3 seti kazanan maçı bitirir (5 set üzerinden)
            int setsToWin = (sport.getQuarterCount() / 2) + 1; // 5 set → 3 kazanınca biter
            if (homeScore >= setsToWin || awayScore >= setsToWin) {
                finishMatch();
            }
        } else {
            // Futbol / diğer sporlar – gol bazlı
            int homeGoals = getRandom().nextInt(Math.max(1, (homePower / 25) + 2));
            int awayGoals = getRandom().nextInt(Math.max(1, (awayPower / 25) + 2));
            this.homeScore += homeGoals;
            this.awayScore += awayGoals;

            if (currentQuarter >= sport.getQuarterCount()) {
                finishMatch();
            }
        }
    }

    private int calculateLineupPower(Lineup lineup, Coach coach) {
        if (lineup == null || lineup.getPlayers() == null || lineup.getPlayers().isEmpty())
            return 10;
        int power = 0;
        for (Player p : lineup.getPlayers()) {
            if (p != null) power += p.getSkill();
        }
        if (coach != null) power += coach.getTrainingBonus();
        return Math.max(power, 10);
    }

    private void finishMatch() {
        this.isFinished = true;
        applyInjuries(homeLineup);
        applyInjuries(awayLineup);
    }

    private void applyInjuries(Lineup lineup) {
        if (lineup == null || lineup.getPlayers() == null) return;
        for (Player player : lineup.getPlayers()) {
            if (player != null && getRandom().nextInt(100) < 5) {
                int duration = getRandom().nextInt(3) + 1;
                player.injureForMatches(duration);
            }
        }
    }

    public String getScoreDisplay() {
        if (sport instanceof VolleyballSport) {
            return homeScore + "-" + awayScore + " (sets)";
        }
        return homeScore + "-" + awayScore;
    }

    // Getters & Setters
    public void setHomeLineup(Lineup l) { this.homeLineup = l; }
    public void setAwayLineup(Lineup l) { this.awayLineup = l; }
    public Team  getHomeTeam()    { return homeTeam; }
    public Team  getAwayTeam()    { return awayTeam; }
    public Sport getSport()       { return sport; }
    public int   getHomeScore()   { return homeScore; }
    public int   getAwayScore()   { return awayScore; }
    public int   getCurrentQuarter() { return currentQuarter; }
    public boolean isFinished()   { return isFinished; }
    public Lineup getHomeLineup() { return homeLineup; }
    public Lineup getAwayLineup() { return awayLineup; }
}
