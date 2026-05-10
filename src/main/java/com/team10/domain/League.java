package com.team10.domain;

import com.team10.sports.Sport;
import com.team10.sports.VolleyballSport;
import java.util.*;
import java.io.Serializable;

public class League implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Sport sport;
    private final List<Team> teams;
    private final List<List<Match>> fixtures;
    private final List<Match> playedMatches;
    private final Map<Team, TeamRecord> standings;
    private int currentWeek;

    public League(List<Team> teams, Sport sport) {
        if (teams == null || teams.size() < 2) throw new IllegalArgumentException("Not enough teams.");
        if (sport == null) throw new IllegalArgumentException("Sport cannot be null.");

        this.teams = new ArrayList<>(teams);

        this.sport = sport;
        ensureEnoughPlayersForSeason();
        this.currentWeek = 0;
        this.playedMatches = new ArrayList<>();
        this.standings = new HashMap<>();

        for (Team t : teams) {
            standings.put(t, new TeamRecord(t));
        }

        this.fixtures = FixtureGenerator.generateFixture(teams, sport);
    }

    public void playNextWeek() {
        if (isLeagueFinished()) {
            throw new IllegalStateException("The league is already finished.");
        }

        List<Match> currentWeekMatches = fixtures.get(currentWeek);

        for (Match match : currentWeekMatches) {
            // Otomatik kadro atama (Eğer kullanıcı manuel atamamışsa)
            if (match.getHomeTeam().getCurrentLineup() == null) {
                autoAssignLineup(match.getHomeTeam());
            }
            if (match.getAwayTeam().getCurrentLineup() == null) {
                autoAssignLineup(match.getAwayTeam());
            }

            match.setHomeLineup(match.getHomeTeam().getCurrentLineup());
            match.setAwayLineup(match.getAwayTeam().getCurrentLineup());

            // Maçı sonuna kadar oynat
            while (!match.isFinished()) {
                match.playNextQuarter();
            }

            playedMatches.add(match);
            updateStandings(match);

            // Maç bitince kadroları sıfırla (Bir sonraki hafta sakatlık kontrolü için)
            match.getHomeTeam().setCurrentLineup(null);
            match.getAwayTeam().setCurrentLineup(null);
        }

        // Sakatlık iyileşme süreci
        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                player.recoverOneWeek();
            }
        }

        this.currentWeek++;
    }
    private void ensureEnoughPlayersForSeason() {
        int minimumPlayers = sport.getLineupSize() + 4;

        for (Team team : teams) {
            int currentSize = team.getPlayers().size();

            for (int i = currentSize + 1; i <= minimumPlayers; i++) {
                team.addPlayer(new Player(
                        team.getName() + " Reserve " + i,
                        "Reserve",
                        60
                ));
            }
        }
    }

    private void autoAssignLineup(Team team) {
        List<Player> available = team.getAvailablePlayers();
        int needed = sport.getLineupSize();

        if (available.size() < needed) {
            throw new IllegalStateException("Not enough available players to create a valid lineup.");
        }

        List<Player> lineupPlayers = new ArrayList<>(available.subList(0, needed));
        Lineup lineup = new Lineup(team, lineupPlayers, sport);
        team.setCurrentLineup(lineup);
    }
    private void updateStandings(Match match) {
        TeamRecord homeRec = standings.get(match.getHomeTeam());
        TeamRecord awayRec = standings.get(match.getAwayTeam());

        if (homeRec != null && awayRec != null) {
            homeRec.addMatchResult(match.getHomeScore(), match.getAwayScore(), sport);
            awayRec.addMatchResult(match.getAwayScore(), match.getHomeScore(), sport);
        }
    }

    public List<TeamRecord> getSortedStandings() {
        List<TeamRecord> records = new ArrayList<>(standings.values());

        // Spora özel sıralayıcıyı kullan (Voleybol ve Futbol farkı için)
        Collections.sort(records, sport.getStandingComparator());

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

    public List<Match> getPlayedMatches() { return playedMatches; }
}