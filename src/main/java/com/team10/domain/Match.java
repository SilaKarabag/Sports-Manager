package com.team10.domain;

import com.team10.sports.Sport;
import java.util.Random;

public class Match {

    private final Team homeTeam;
    private final Team awayTeam;
    private final Sport sport;

    private int homeScore;
    private int awayScore;

    private int currentQuarter;
    private boolean isFinished;

    public Match(Team homeTeam, Team awayTeam, Sport sport) {
        if (homeTeam == null || awayTeam == null) {
            throw new IllegalArgumentException("Teams cannot be null.");
        }
        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("A team cannot play against itself.");
        }
        if (sport == null) {
            throw new IllegalArgumentException("Sport cannot be null.");
        }

        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.sport = sport;
        this.homeScore = 0;
        this.awayScore = 0;
        this.currentQuarter = 0;
        this.isFinished = false;
    }

    public void playNextQuarter() {
        if (isFinished) {
            throw new IllegalStateException("Match is already finished.");
        }

        currentQuarter++;
        Random random = new Random();

        int homePower = calculateTeamPower(homeTeam);
        int awayPower = calculateTeamPower(awayTeam);

        int homeGoals = random.nextInt(Math.max(1, (homePower / 20) + 2));
        int awayGoals = random.nextInt(Math.max(1, (awayPower / 20) + 2));

        this.homeScore += homeGoals;
        this.awayScore += awayGoals;

        if (currentQuarter == 4) {
            finishMatch();
        }
    }

    /**
     * Takımın sahadaki oyuncularının ve koçunun yeteneğini toplar.
     */
    private int calculateTeamPower(Team team) {
        int power = 0;
        for (Player p : team.getAvailablePlayers()) {
            power += p.getSkill();
        }
        if (team.getCoach() != null) {
            power += team.getCoach().getTrainingBonus();
        }
        return power > 0 ? power : 10; // Takım boşsa veya gücü 0 ise varsayılan değer
    }

    /**
     * Maçı bitir ve rastgele sakatlık kuralı
     */
    private void finishMatch() {
        this.isFinished = true;
        applyInjuries(homeTeam);
        applyInjuries(awayTeam);
    }

    /**
     * Maç sonu oyuncularda %5 ihtimalle 1-3 maç arası sakatlık oluştur
     */
    private void applyInjuries(Team team) {
        Random random = new Random();
        // isAvailable oyuncular sakatlanabilir
        for (Player player : team.getAvailablePlayers()) {
            if (random.nextInt(100) < 5) {
                int injuryDuration = random.nextInt(3) + 1; // 1 ile 3 maç arası
                player.injureForWeeks(injuryDuration);
            }
        }
    }

    public Team getHomeTeam() { return homeTeam; }
    public Team getAwayTeam() { return awayTeam; }
    public Sport getSport() { return sport; }
    public int getHomeScore() { return homeScore; }
    public int getAwayScore() { return awayScore; }
    public int getCurrentQuarter() { return currentQuarter; }
    public boolean isFinished() { return isFinished; }
}