package com.team10.domain;

import com.team10.sports.Sport;
import com.team10.sports.VolleyballSport; // Bu import kritik, sakın unutma
import java.util.Random;
import java.io.Serializable;

public class Match implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Team homeTeam;
    private final Team awayTeam;
    private Lineup homeLineup;
    private Lineup awayLineup;
    private final Sport sport;
    private final Random random = new Random();

    private int homeScore;
    private int awayScore;

    private int currentQuarter;
    private boolean isFinished;

    // Fikstür oluşturulurken kullanılacak ana constructor
    public Match(Team homeTeam, Team awayTeam, Sport sport) {
        if (homeTeam == null || awayTeam == null || sport == null) {
            throw new IllegalArgumentException("Teams and Sport cannot be null.");
        }
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.sport = sport;
        this.homeScore = 0;
        this.awayScore = 0;
        this.currentQuarter = 0;
        this.isFinished = false;
        this.homeLineup = null;
        this.awayLineup = null;
    }

    // Maç oynatılırken kullanılacak tam constructor
    public Match(Team homeTeam, Team awayTeam, Lineup homeLineup, Lineup awayLineup, Sport sport) {
        this(homeTeam, awayTeam, sport);
        this.homeLineup = homeLineup;
        this.awayLineup = awayLineup;
    }

    public void playNextQuarter() {
        if (isFinished) {
            throw new IllegalStateException("Match is already finished.");
        }

        currentQuarter++;

        // Kadro güçlerini hesapla
        int homePower = calculateLineupPower(homeLineup, homeTeam.getCoach());
        int awayPower = calculateLineupPower(awayLineup, awayTeam.getCoach());

        // SPORA ÖZEL SKOR MANTIĞI
        if (sport instanceof VolleyballSport) {
            // VOLEYBOL: Her çeyrek (set) bir takım tarafından kazanılır (1-0 veya 0-1)
            // Güçlü olanın seti alma ihtimali daha yüksek ama şans faktörü var
            if (homePower + random.nextInt(30) > awayPower + random.nextInt(30)) {
                this.homeScore += 1;
            } else {
                this.awayScore += 1;
            }
        } else {
            // FUTBOL / HEADBALL: Rastgele gol üretimi
            int homeGoals = random.nextInt(Math.max(1, (homePower / 25) + 2));
            int awayGoals = random.nextInt(Math.max(1, (awayPower / 25) + 2));
            this.homeScore += homeGoals;
            this.awayScore += awayGoals;
        }

        // Bitiş kontrolü Sport interface'inden dinamik gelmeli
        if (currentQuarter >= sport.getQuarterCount()) {
            finishMatch();
        }
    }

    /**
     * Sadece sahadaki (Lineup içindeki) oyuncuların ve koçun yeteneğini hesaplar.
     */
    private int calculateLineupPower(Lineup lineup, Coach coach) {
        if (lineup == null || lineup.getPlayers() == null || lineup.getPlayers().isEmpty()) {
            return 10; // Kadro yoksa minimum güç
        }
        int power = 0;
        for (Player p : lineup.getPlayers()) {
            if (p != null) {
                power += p.getSkill();
            }
        }
        if (coach != null) {
            power += coach.getTrainingBonus();
        }
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
            if (player != null && random.nextInt(100) < 5) { // %5 sakatlık ihtimali
                int injuryDuration = random.nextInt(3) + 1;
                player.injureForMatches(injuryDuration);
            }
        }
    }

    // Getter ve Setter Metodları
    public void setHomeLineup(Lineup homeLineup) { this.homeLineup = homeLineup; }
    public void setAwayLineup(Lineup awayLineup) { this.awayLineup = awayLineup; }
    public Team getHomeTeam() { return homeTeam; }
    public Team getAwayTeam() { return awayTeam; }
    public Sport getSport() { return sport; }
    public int getHomeScore() { return homeScore; }
    public int getAwayScore() { return awayScore; }
    public int getCurrentQuarter() { return currentQuarter; }
    public boolean isFinished() { return isFinished; }
}