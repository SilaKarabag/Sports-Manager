package com.team10.domain;

import com.team10.sports.Sport;
import java.util.Random;

public class Match {

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

    // Constructor Güncellendi: Artık Lineup parametrelerini alıyor
    public Match(Team homeTeam, Team awayTeam, Lineup homeLineup, Lineup awayLineup, Sport sport) {
        if (homeTeam == null || awayTeam == null || homeLineup == null || awayLineup == null) {
            throw new IllegalArgumentException("Teams and Lineups cannot be null.");
        }
        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("A team cannot play against itself.");
        }
        if (sport == null) {
            throw new IllegalArgumentException("Sport cannot be null.");
        }

        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeLineup = homeLineup;
        this.awayLineup = awayLineup;
        this.sport = sport;
        this.homeScore = 0;
        this.awayScore = 0;
        this.currentQuarter = 0;
        this.isFinished = false;
    }
    // Fikstür oluşturulurken kullanılacak "hafif" constructor
    public Match(Team homeTeam, Team awayTeam, Sport sport) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.sport = sport;
        this.homeScore = 0;
        this.awayScore = 0;
        this.currentQuarter = 0;
        this.isFinished = false;
        // Kadrolar maç başlayana kadar null kalabilir veya boş atanabilir
        this.homeLineup = null;
        this.awayLineup = null;
    }

    public void playNextQuarter() {
        if (isFinished) {
            throw new IllegalStateException("Match is already finished.");
        }

        currentQuarter++;

        // HESAPLAMA: Artık Team yerine sahadaki Lineup (Kadro) gücü kullanılıyor
        int homePower = calculateLineupPower(homeLineup, homeTeam.getCoach());
        int awayPower = calculateLineupPower(awayLineup, awayTeam.getCoach());

        // Skor üretimi (Random nesnesi yukarıdan geliyor)
        int homeGoals = random.nextInt(Math.max(1, (homePower / 20) + 2));
        int awayGoals = random.nextInt(Math.max(1, (awayPower / 20) + 2));

        this.homeScore += homeGoals;
        this.awayScore += awayGoals;

        // Bitiş kontrolü Sport interface'inden dinamik gelmeli (Örn: Headball 4, Futbol 2)
        if (currentQuarter >= sport.getQuarterCount()) {
            finishMatch();
        }
    }

    /**
     * Sadece sahadaki (Lineup içindeki) oyuncuların ve koçun yeteneğini hesaplar.
     */
    private int calculateLineupPower(Lineup lineup, Coach coach) {
        if (lineup == null) return 10;
        int power = 0;
        for (Player p : lineup.getPlayers()) {
            power += p.getSkill();
        }
        if (coach != null) {
            power += coach.getTrainingBonus();
        }
        return power > 0 ? power : 10;
    }

    private void finishMatch() {
        this.isFinished = true;
        applyInjuries(homeLineup); // Sadece maçta oynayanlar sakatlanabilir
        applyInjuries(awayLineup);
    }

    /**
     * Maç sonunda sahadaki oyuncularda %5 ihtimalle sakatlık oluşturur.
     */
    private void applyInjuries(Lineup lineup) {
        if (lineup==null){
            return;
        }
        for (Player player : lineup.getPlayers()) {
            if (random.nextInt(100) < 5) {
                int injuryDuration = random.nextInt(3) + 1;
                player.injureForMatches(injuryDuration);
            }
        }
    }

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