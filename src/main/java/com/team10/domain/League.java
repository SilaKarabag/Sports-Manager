package com.team10.domain;

import com.team10.sports.Sport;
import java.util.*;

public class League {
    private final Sport sport;
    private final List<Team> teams;
    private final List<List<Match>> fixtures; // tüm haftaların maçları
    private final List<Match> playedMatches;
    private final Map<Team, TeamRecord> standings;
    private int currentWeek;

    public League(List<Team> teams, Sport sport) {
        if (teams == null || teams.size() < 2) throw new IllegalArgumentException("Not enough teams.");
        if (sport == null) throw new IllegalArgumentException("Sport cannot be null.");

        this.teams = teams;
        this.sport = sport;
        this.currentWeek = 0;
        this.playedMatches = new ArrayList<Match>();
        this.standings = new HashMap<Team, TeamRecord>();

        // her takım için puan tablosunda sıfır bir kayıt oluştur
        for (Team t : teams) {
            standings.put(t, new TeamRecord(t));
        }

        // 1. fikstürü oluşturur
        this.fixtures = FixtureGenerator.generateFixture(teams, sport);
    }

    /**
     * O haftaki tüm maçları (4 çeyrek dahil) simüle eder ve puan durumunu günceller
     */
    public void playNextWeek() {
        if (isLeagueFinished()) {
            throw new IllegalStateException("The league is already finished.");
        }

        List<Match> currentWeekMatches = fixtures.get(currentWeek);

        for (Match match : currentWeekMatches) {
            // maçın 4 çeyreğini de oynat
            while (!match.isFinished()) {
                match.playNextQuarter();
            }

            playedMatches.add(match);

            // takımların puan durumunu güncelle
            TeamRecord homeRecord = standings.get(match.getHomeTeam());
            TeamRecord awayRecord = standings.get(match.getAwayTeam());

            homeRecord.updateRecord(match.getHomeScore(), match.getAwayScore(), sport);
            awayRecord.updateRecord(match.getAwayScore(), match.getHomeScore(), sport);
        }

        currentWeek++;
    }

    /**
     * sıralama:
     * 1. Puan
     * 2. İkili averaj
     * 3. Genel averaj
     * 4. yazı tura (alfabetik sıra ile simüle)
     */
    public List<TeamRecord> getSortedStandings() {
        List<TeamRecord> records = new ArrayList<TeamRecord>(standings.values());

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

    public int getCurrentWeek() { return currentWeek + 1; }
    public List<Team> getTeams() { return teams; }
}