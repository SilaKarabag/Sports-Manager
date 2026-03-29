package com.team10.domain;

import com.team10.sports.FootballSport;
import com.team10.sports.Sport;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class LeagueTest {

    @Test
    public void testHeadToHeadTieBreaker() {
        Sport football = new FootballSport();
        Team teamA = new Team("Team A");
        Team teamB = new Team("Team B");
        List<Team> teams = new ArrayList<Team>();
        teams.add(teamA);
        teams.add(teamB);

        League league = new League(teams, football);

        // Senaryo: İki takımın da 3 puanı var.
        // Ama Team A, Team B'yi 5-0 yendi. Team B ise Team A'yı 1-0 yendi.
        // İkili averaj kuralına göre Team A (toplamda 5-1 önde olduğu için) 1. olmalı.

        List<TeamRecord> standings = league.getSortedStandings();

        standings.get(0).updateRecord(5, 0, football);
        standings.get(1).updateRecord(0, 5, football);

        standings.get(1).updateRecord(1, 0, football);
        standings.get(0).updateRecord(0, 1, football);

        List<TeamRecord> sorted = league.getSortedStandings();

        assertEquals("Team A", sorted.get(0).getTeam().getName());
        assertTrue(sorted.get(0).getPoints() == sorted.get(1).getPoints());
    }
}