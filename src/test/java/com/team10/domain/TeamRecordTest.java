package com.team10.domain;

import com.team10.sports.FootballSport;
import com.team10.sports.Sport;
import org.junit.Test;
import static org.junit.Assert.*;

public class TeamRecordTest {

    @Test
    public void testUpdateRecordWin() {
        Team team = new Team("Test Team");
        TeamRecord record = new TeamRecord(team);
        Sport football = new FootballSport();

        record.updateRecord(3, 1, football);

        assertEquals(1, record.getMatchesPlayed());
        assertEquals(1, record.getWins());
        assertEquals(3, record.getPoints());
        assertEquals(2, record.getGoalDifference());
    }

    @Test
    public void testUpdateRecordDraw() {
        Team team = new Team("Test Team");
        TeamRecord record = new TeamRecord(team);
        Sport football = new FootballSport();

        record.updateRecord(2, 2, football);

        assertEquals(1, record.getDraws());
        assertEquals(1, record.getPoints());
    }
}