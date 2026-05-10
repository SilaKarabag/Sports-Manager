package com.team10.domain;

import com.team10.sports.Sport;
import com.team10.sports.VolleyballSport;
import java.util.Random;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Bir maçı temsil eder.
 * DÜZELTMELER:
 * 1) Random transient – serialize sorunu giderildi
 * 2) Voleybol best-of-5: 3 seti kazanan oyunu bitirir
 * 3) Taktik bonusu: ATTACKING/DEFENSIVE etkisi var
 * 4) getQuarterEvents() – UI'da periyot olaylarını göstermek için
 */
public class Match implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Team homeTeam;
    private final Team awayTeam;
    private Lineup homeLineup;
    private Lineup awayLineup;
    private final Sport sport;

    // CRITICAL FIX: Random Serializable değil, transient olmalı
    private transient Random random;

    private int homeScore;
    private int awayScore;
    private int currentQuarter;
    private boolean isFinished;

    // Her periyodun özet olayları (UI için)
    private final List<String> quarterEvents = new ArrayList<>();

    public Match(Team homeTeam, Team awayTeam, Sport sport) {
        if (homeTeam == null || awayTeam == null || sport == null)
            throw new IllegalArgumentException("Teams and Sport cannot be null.");
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.sport    = sport;
    }

    public Match(Team homeTeam, Team awayTeam, Lineup homeLineup, Lineup awayLineup, Sport sport) {
        this(homeTeam, awayTeam, sport);
        this.homeLineup = homeLineup;
        this.awayLineup = awayLineup;
    }

    private Random rng() {
        if (random == null) random = new Random();
        return random;
    }

    public void playNextQuarter() {
        if (isFinished) throw new IllegalStateException("Match is already finished.");
        currentQuarter++;

        int homePower = calculatePower(homeLineup, homeTeam);
        int awayPower = calculatePower(awayLineup, awayTeam);

        StringBuilder event = new StringBuilder();

        if (sport instanceof VolleyballSport) {
            // Voleybol: her periyot bir SET – beraberlik YOK
            int homeRoll = homePower + rng().nextInt(50);
            int awayRoll = awayPower + rng().nextInt(50);
            if (homeRoll >= awayRoll) {
                homeScore++;
                event.append("Set ").append(currentQuarter).append(": ")
                        .append(homeTeam.getName()).append(" wins the set! (")
                        .append(homeScore).append("-").append(awayScore).append(")");
            } else {
                awayScore++;
                event.append("Set ").append(currentQuarter).append(": ")
                        .append(awayTeam.getName()).append(" wins the set! (")
                        .append(homeScore).append("-").append(awayScore).append(")");
            }
            // Best-of-5: 3 set kazanan maçı kazanır
            int setsToWin = (sport.getQuarterCount() / 2) + 1;
            if (homeScore >= setsToWin || awayScore >= setsToWin) {
                finishMatch();
            }
        } else {
            // Futbol: gol bazlı, 2 yarı
            int homeGoals = rng().nextInt(Math.max(1, homePower / 25 + 2));
            int awayGoals = rng().nextInt(Math.max(1, awayPower / 25 + 2));
            homeScore += homeGoals;
            awayScore += awayGoals;
            String half = currentQuarter == 1 ? "1st Half" : "2nd Half";
            event.append(half).append(": ")
                    .append(homeTeam.getName()).append(" ").append(homeScore)
                    .append(" - ").append(awayScore).append(" ")
                    .append(awayTeam.getName());
            if (homeGoals > 0)
                event.append(" (").append(homeTeam.getName()).append(" scored ").append(homeGoals).append(")");
            if (currentQuarter >= sport.getQuarterCount()) {
                finishMatch();
            }
        }

        quarterEvents.add(event.toString());
    }

    /** Taktik dahil güç hesabı */
    private int calculatePower(Lineup lineup, Team team) {
        if (lineup == null || lineup.getPlayers() == null || lineup.getPlayers().isEmpty())
            return 10;
        int power = 0;
        for (Player p : lineup.getPlayers()) {
            if (p != null) power += p.getSkill();
        }
        if (team.getCoach() != null) power += team.getCoach().getTrainingBonus();

        // Taktik bonusu
        String tactic = team.getTactic();
        if ("ATTACKING".equals(tactic))  power += 15;
        if ("DEFENSIVE".equals(tactic))  power -= 10;

        return Math.max(power, 10);
    }

    private void finishMatch() {
        isFinished = true;
        applyInjuries(homeLineup);
        applyInjuries(awayLineup);
    }

    private void applyInjuries(Lineup lineup) {
        if (lineup == null) return;
        for (Player p : lineup.getPlayers()) {
            if (p != null && rng().nextInt(100) < 8) { // %8 sakatlık
                int dur = rng().nextInt(3) + 1;
                p.injureForMatches(dur);
                quarterEvents.add("⚠ " + p.getName() + " injured for " + dur + " match(es)!");
            }
        }
    }

    public List<String> getQuarterEvents() { return new ArrayList<>(quarterEvents); }

    // Getters & Setters
    public void setHomeLineup(Lineup l) { this.homeLineup = l; }
    public void setAwayLineup(Lineup l) { this.awayLineup = l; }
    public Team    getHomeTeam()    { return homeTeam; }
    public Team    getAwayTeam()    { return awayTeam; }
    public Sport   getSport()       { return sport; }
    public int     getHomeScore()   { return homeScore; }
    public int     getAwayScore()   { return awayScore; }
    public int     getCurrentQuarter() { return currentQuarter; }
    public boolean isFinished()     { return isFinished; }
    public Lineup  getHomeLineup()  { return homeLineup; }
    public Lineup  getAwayLineup()  { return awayLineup; }
}
