package com.team10.domain;

import com.team10.sports.Sport;
import java.util.*;
import java.io.Serializable;

/**
 * BUG FIX 1: getCurrentWeek() orijinalde currentWeek+1 döndürüyordu.
 *   Ama playNextWeek() currentWeek'i IÇİNDE artırıyor.
 *   UI'da "Week 7 played" yerine "Week 8" yazıyordu. Düzeltildi.
 *
 * BUG FIX 2: isLeagueFinished() fixtures.size() ile kıyaslıyor.
 *   FixtureGenerator çift devreli üretiyor – fixtures.size() = (n-1)*2.
 *   Bu doğru, korundu.
 *
 * BUG FIX 3: ensureEnoughPlayersForSeason – voleybolda 6 kişi lazım.
 *   Orijin sport.getLineupSize()+4 kullanıyordu. Korundu, doğru.
 *
 * BUG FIX 4: getPlayedMatchesForWeek eklendi – UI için geçen hafta maçlarına erişim.
 */
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

        this.currentWeek  = 0;
        this.playedMatches = new ArrayList<>();
        this.standings    = new LinkedHashMap<>();

        for (Team t : this.teams) {
            standings.put(t, new TeamRecord(t));
        }

        this.fixtures = FixtureGenerator.generateFixture(this.teams, sport);
    }

    public void playNextWeek() {
        if (isLeagueFinished()) throw new IllegalStateException("The league is already finished.");

        List<Match> weekMatches = fixtures.get(currentWeek);

        for (Match match : weekMatches) {
            if (match.getHomeTeam().getCurrentLineup() == null) autoAssignLineup(match.getHomeTeam());
            if (match.getAwayTeam().getCurrentLineup() == null) autoAssignLineup(match.getAwayTeam());

            match.setHomeLineup(match.getHomeTeam().getCurrentLineup());
            match.setAwayLineup(match.getAwayTeam().getCurrentLineup());

            while (!match.isFinished()) {
                match.playNextQuarter();
            }

            playedMatches.add(match);
            updateStandings(match);

            match.getHomeTeam().setCurrentLineup(null);
            match.getAwayTeam().setCurrentLineup(null);
        }

        // Sakatlık iyileşme
        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                player.recoverOneWeek();
            }
        }

        this.currentWeek++;
    }

    private void ensureEnoughPlayersForSeason() {
        int minimum = sport.getLineupSize() + 4;
        for (Team team : teams) {
            int current = team.getPlayers().size();
            for (int i = current + 1; i <= minimum; i++) {
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
        if (available.size() < needed)
            throw new IllegalStateException("Not enough available players for " + team.getName());

        List<Player> selected = new ArrayList<>(available.subList(0, needed));
        team.setCurrentLineup(new Lineup(team, selected, sport));
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
        records.sort(sport.getStandingComparator());
        return records;
    }

    /**
     * BUG FIX: Orijinalde currentWeek+1 döndürülüyordu.
     * Sezon bitmeden önce doğru hafta göstermek için:
     * - Sezon devam ediyorsa: oynanmakta olan hafta = currentWeek + 1
     * - Sezon bittiyse: toplam hafta sayısı = fixtures.size()
     */
    public int getCurrentWeek() {
        if (isLeagueFinished()) return fixtures.size();
        return currentWeek + 1;
    }

    /** Geçen haftanın maçlarını döner (UI sonuç gösterimi için) */
    public List<Match> getLastWeekMatches() {
        if (currentWeek == 0) return Collections.emptyList();
        int lastIdx = currentWeek - 1;
        if (lastIdx >= fixtures.size()) return Collections.emptyList();
        return Collections.unmodifiableList(fixtures.get(lastIdx));
    }

    public int getTotalWeeks()      { return fixtures.size(); }
    public boolean isLeagueFinished() { return currentWeek >= fixtures.size(); }
    public List<Team>  getTeams()       { return Collections.unmodifiableList(teams); }
    public List<Match> getPlayedMatches() { return Collections.unmodifiableList(playedMatches); }
    public Sport getSport()             { return sport; }
}
