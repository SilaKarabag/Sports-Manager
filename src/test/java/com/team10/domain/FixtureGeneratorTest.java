package com.team10.domain;

import com.team10.sports.FootballSport;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class FixtureGeneratorTest {

    @Test
    public void testFixtureStructure() {
        List<Team> teams = new ArrayList<Team>();
        teams.add(new Team("Eagles"));
        teams.add(new Team("Lions"));
        teams.add(new Team("Falcons"));
        teams.add(new Team("Panthers"));

        List<List<Match>> fixture = FixtureGenerator.generateFixture(teams, new FootballSport());

        assertNotNull(fixture);
        assertEquals(6, fixture.size()); // Hafta sayısı kontrolü

        for (List<Match> week : fixture) {
            assertEquals(2, week.size());
        }
    }
}