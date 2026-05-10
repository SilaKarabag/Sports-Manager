package com.team10.domain;

import com.team10.sports.FootballSport;
import com.team10.sports.Sport;
import com.team10.sports.VolleyballSport;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestDataFactory {

    private static final Random RNG = new Random();

    private static final List<String> TEAM_NAMES = Arrays.asList(
            "Red Eagles", "Blue Lions", "Black Panthers", "Green Falcons",
            "Silver Wolves", "Golden Bears", "Iron Hawks", "Thunder United",
            "Crimson Tide", "Royal Knights", "Storm City", "Velocity SC"
    );
    private static final List<String> FIRST = Arrays.asList(
            "James", "Luca", "Omar", "Yuki", "Carlos", "Erik", "Mateo",
            "Noah", "Aiden", "Elif", "Mert", "Burak", "Emre", "Arda",
            "Kerem", "Sofia", "Amara", "Zara", "Priya", "Leila", "Sara"
    );
    private static final List<String> LAST = Arrays.asList(
            "Santos", "Müller", "Kim", "Rossi", "Diallo", "Chen", "Garcia",
            "Yılmaz", "Kaya", "Şahin", "Demir", "Çelik", "Arslan", "Petrov"
    );
    private static final String[] FB_POS = {"GK","DEF","DEF","DEF","DEF","MID","MID","MID","MID","ST","ST"};
    private static final String[] VB_POS = {"Setter","Libero","Outside Hitter","Outside Hitter","Middle Blocker","Opposite"};
    private static final String[] COACH_STYLES = {"Tactical","Aggressive","Defensive","Motivational","Technical"};

    private static int teamIdx = 0;

    //actually shuffles team names now
    public static void reset() {
        teamIdx = 0;
        Collections.shuffle(TEAM_NAMES);
    }

    public static Team createTeam(Sport sport) {
        String name = teamIdx < TEAM_NAMES.size() ? TEAM_NAMES.get(teamIdx) : "Team " + (teamIdx + 1);
        teamIdx++;

        Team team = new Team(name);
        String[] positions = (sport instanceof VolleyballSport) ? VB_POS : FB_POS;
        int rosterSize = sport.getLineupSize() + 4;

        for (int i = 0; i < rosterSize; i++) {
            String pos = positions[i % positions.length];
            int skill = 45 + RNG.nextInt(45);
            team.addPlayer(new Player(randomName(), pos, skill));
        }

        String coachStyle = COACH_STYLES[RNG.nextInt(COACH_STYLES.length)];
        int bonus = 5 + RNG.nextInt(15);
        team.setCoach(new Coach(randomName() + " (" + coachStyle + ")", bonus));

        return team;
    }

    /** Backward compat – football default */
    public static Team createTeam() { return createTeam(new FootballSport()); }

    public static Player createPlayer(Sport sport) {
        String[] pos = (sport instanceof VolleyballSport) ? VB_POS : FB_POS;
        return new Player(randomName(), pos[RNG.nextInt(pos.length)], 50 + RNG.nextInt(40));
    }

    public static Coach createCoach() {
        String style = COACH_STYLES[RNG.nextInt(COACH_STYLES.length)];
        return new Coach(randomName() + " (" + style + ")", 5 + RNG.nextInt(15));
    }

    private static String randomName() {
        return FIRST.get(RNG.nextInt(FIRST.size())) + " " + LAST.get(RNG.nextInt(LAST.size()));
    }
}
