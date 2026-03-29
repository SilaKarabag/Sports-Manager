package com.team10.domain;

import com.team10.sports.Sport;
import java.util.*;

public class League {
    private final Sport sport;
    private final List<Team> teams;
    private final List<List<Match>> fixtures;
    private final List<Match> playedMatches;
    private final Map<Team, TeamRecord> standings;
    private int currentWeek;

    public League(List<Team> teams, Sport sport) {
        if (teams == null || teams.size() < 2) throw new IllegalArgumentException("Not enough teams.");
        if (sport == null) throw new IllegalArgumentException("Sport cannot be null.");

        this.teams = teams;
        this.sport = sport;
        this.currentWeek = 0;
        this.playedMatches = new ArrayList<>();
        this.standings = new HashMap<>();

        for (Team t : teams) {
            standings.put(t, new TeamRecord(t));
        }

        this.fixtures = FixtureGenerator.generateFixture(teams, sport);
    }

    /**
     * O haftaki tüm maçları simüle eder, puan durumunu günceller ve oyuncuları iyileştirir.
     */
    public void playNextWeek() {
        if (isLeagueFinished()) {
            throw new IllegalStateException("The league is already finished.");
        }

        List<Match> currentWeekMatches = fixtures.get(currentWeek);

        for (Match match : currentWeekMatches) {
            match.getHomeTeam().setCurrentLineup(null);
            match.getAwayTeam().setCurrentLineup(null);
            // Kadro atanmamışsa otomatik olarak müsait oyunculardan oluştur
            if (match.getHomeTeam().getCurrentLineup() == null) {
                autoAssignLineup(match.getHomeTeam());
            }
            if (match.getAwayTeam().getCurrentLineup() == null) {
                autoAssignLineup(match.getAwayTeam());
            }

            match.setHomeLineup(match.getHomeTeam().getCurrentLineup());
            match.setAwayLineup(match.getAwayTeam().getCurrentLineup());

            while (!match.isFinished()) {
                match.playNextQuarter();
            }

            playedMatches.add(match);
            updateStandings(match);
        }

        // Tüm maçlar bittikten sonra oyuncuların sakatlık sürelerini bir azalt
        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                player.recoverOneWeek();
            }
        }

        this.currentWeek++;
    }

    /**
     * Takımın müsait oyuncularından otomatik kadro oluşturur.
     * Müsait oyuncu sayısı yetersizse mevcut tüm müsait oyuncuları alır.
     */
    private void autoAssignLineup(Team team) {
        List<Player> available = team.getAvailablePlayers();
        int needed = sport.getLineupSize();

        if (available.size() < needed) {
            // Yeterli oyuncu yoksa (aşırı sakatlık durumu) mevcut müsait oyuncularla devam et
            // Bu durum için Sport'un minimum oyuncu sayısını kontrol etmek gerekebilir
            needed = available.size();
        }

        List<Player> lineupPlayers = new ArrayList<>(available.subList(0, needed));
        // Not: available.size() == needed olduğunda Lineup validasyonu geçer
        if (lineupPlayers.size() == sport.getLineupSize()) {
            Lineup lineup = new Lineup(team, lineupPlayers, sport);
            team.setCurrentLineup(lineup);
        }
        // Yeterli oyuncu yoksa currentLineup null kalır — Match bunu handle etmeli
    }

    /**
     * Maç sonucunu ilgili takımların kayıtlarına işler.
     */
    private void updateStandings(Match match) {
        TeamRecord homeRec = standings.get(match.getHomeTeam());
        TeamRecord awayRec = standings.get(match.getAwayTeam());

        if (homeRec != null && awayRec != null) {
            homeRec.addMatchResult(match.getHomeScore(), match.getAwayScore(), sport);
            awayRec.addMatchResult(match.getAwayScore(), match.getHomeScore(), sport);
        }
    }

    /**
     * Sıralama: 1. Puan → 2. İkili averaj → 3. Genel averaj → 4. Alfabetik (yazı tura simülasyonu)
     */
    public List<TeamRecord> getSortedStandings() {
        List<TeamRecord> records = new ArrayList<>(standings.values());

        Collections.sort(records, new Comparator<TeamRecord>() {
            @Override
            public int compare(TeamRecord r1, TeamRecord r2) {
                if (r1.getPoints() != r2.getPoints()) {
                    return Integer.compare(r2.getPoints(), r1.getPoints());
                }

                int hth1 = 0, hth2 = 0;
                for (Match m : playedMatches) {
                    if (m.getHomeTeam().equals(r1.getTeam()) && m.getAwayTeam().equals(r2.getTeam())) {
                        hth1 += m.getHomeScore(); hth2 += m.getAwayScore();
                    } else if (m.getHomeTeam().equals(r2.getTeam()) && m.getAwayTeam().equals(r1.getTeam())) {
                        hth1 += m.getAwayScore(); hth2 += m.getHomeScore();
                    }
                }
                if (hth1 != hth2) {
                    return Integer.compare(hth2, hth1);
                }

                if (r1.getGoalDifference() != r2.getGoalDifference()) {
                    return Integer.compare(r2.getGoalDifference(), r1.getGoalDifference());
                }

                return r1.getTeam().getName().compareTo(r2.getTeam().getName());
            }
        });

        return records;
    }

    public boolean isLeagueFinished() {
        return currentWeek >= fixtures.size();
    }

    public int getCurrentWeek() {
        return currentWeek + 1;
    }

    public List<Team> getTeams() {
        return teams;
    }
}