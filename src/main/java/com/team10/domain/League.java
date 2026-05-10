package com.team10.domain;

import com.team10.sports.Sport;
import java.util.*;
import java.io.Serializable;

public class League implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Sport sport;
    private final List<Team> teams;
    private final List<List<Match>> fixtures;
    private final List<Match> playedMatches;
    // LinkedHashMap – ekleme sırası korunur, Team.equals/hashCode ile doğru çalışır
    private final Map<Team, TeamRecord> standings;
    private int currentWeek;

    public League(List<Team> teams, Sport sport) {
        if (teams == null || teams.size() < 2) throw new IllegalArgumentException("Not enough teams.");
        if (sport == null) throw new IllegalArgumentException("Sport cannot be null.");

        this.teams = new ArrayList<>(teams);
        this.sport = sport;
        ensureEnoughPlayers();

        this.currentWeek   = 0;
        this.playedMatches = new ArrayList<>();
        this.standings     = new LinkedHashMap<>();

        for (Team t : this.teams) standings.put(t, new TeamRecord(t));

        this.fixtures = FixtureGenerator.generateFixture(this.teams, sport);
    }

    public void playNextWeek() {
        if (isLeagueFinished()) throw new IllegalStateException("League already finished.");

        List<Match> weekMatches = fixtures.get(currentWeek);

        for (Match match : weekMatches) {
            if (match.getHomeTeam().getCurrentLineup() == null) autoAssign(match.getHomeTeam());
            if (match.getAwayTeam().getCurrentLineup() == null) autoAssign(match.getAwayTeam());

            match.setHomeLineup(match.getHomeTeam().getCurrentLineup());
            match.setAwayLineup(match.getAwayTeam().getCurrentLineup());

            while (!match.isFinished()) match.playNextQuarter();

            playedMatches.add(match);
            updateStandings(match);

            match.getHomeTeam().setCurrentLineup(null);
            match.getAwayTeam().setCurrentLineup(null);
        }

        for (Team t : teams)
            for (Player p : t.getPlayers())
                p.recoverOneWeek();

        currentWeek++;
    }

    private void ensureEnoughPlayers() {
        int min = sport.getLineupSize() + 4;
        for (Team t : teams) {
            int cur = t.getPlayers().size();
            for (int i = cur + 1; i <= min; i++)
                t.addPlayer(new Player(t.getName() + " Reserve " + i, "Reserve", 60));
        }
    }

    private void autoAssign(Team team) {
        List<Player> av = team.getAvailablePlayers();
        int needed = sport.getLineupSize();
        if (av.size() < needed)
            throw new IllegalStateException("Not enough available players for " + team.getName());
        team.setCurrentLineup(new Lineup(team, new ArrayList<>(av.subList(0, needed)), sport));
    }

    private void updateStandings(Match m) {
        TeamRecord hr = standings.get(m.getHomeTeam());
        TeamRecord ar = standings.get(m.getAwayTeam());
        if (hr != null && ar != null) {
            hr.addMatchResult(m.getHomeScore(), m.getAwayScore(), sport);
            ar.addMatchResult(m.getAwayScore(), m.getHomeScore(), sport);
        }
    }

    public List<TeamRecord> getSortedStandings() {
        List<TeamRecord> list = new ArrayList<>(standings.values());
        list.sort(sport.getStandingComparator());
        return list;
    }

    /** Geçen haftanın maçları – UI sonuç paneli için */
    public List<Match> getLastWeekMatches() {
        if (currentWeek == 0) return Collections.emptyList();
        int idx = currentWeek - 1;
        if (idx >= fixtures.size()) return Collections.emptyList();
        return Collections.unmodifiableList(fixtures.get(idx));
    }

    /** Gelecek hafta fikstürü – fikstür tablosu için */
    public List<Match> getUpcomingMatches() {
        if (isLeagueFinished()) return Collections.emptyList();
        return Collections.unmodifiableList(fixtures.get(currentWeek));
    }

    public boolean isLeagueFinished()  { return currentWeek >= fixtures.size(); }
    public int getTotalWeeks()         { return fixtures.size(); }
    public int getCurrentWeek()        { return isLeagueFinished() ? fixtures.size() : currentWeek + 1; }
    public List<Team>  getTeams()      { return Collections.unmodifiableList(teams); }
    public List<Match> getPlayedMatches() { return Collections.unmodifiableList(playedMatches); }
    public Sport       getSport()      { return sport; }
}
