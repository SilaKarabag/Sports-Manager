package com.team10.domain;

import com.team10.sports.FootballSport;
import com.team10.sports.Sport;
import com.team10.sports.VolleyballSport;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * BUG FIX 1: Orijinal createTeam() sabit 15 oyuncu ekliyordu.
 *   Voleybol için 6+4=10 yeterliydi ama kodlamada hardcode 15 yazıyordu.
 *   Spor'a göre dinamik boyut eklendi.
 *
 * BUG FIX 2: teamCounter static – birden fazla test çalıştırmada sayaç
 *   sıfırlanmıyordu. reset() metodu eklendi.
 *
 * BUG FIX 3: Gerçekçi isimler eklendi (random name pool).
 */
public class TestDataFactory {

    private static int teamCounter   = 1;
    private static int playerCounter = 1;
    private static int coachCounter  = 1;
    private static final Random RANDOM = new Random();

    private static final List<String> FIRST_NAMES = Arrays.asList(
            "James", "Luca", "Omar", "Yuki", "Carlos", "Erik", "Mateo",
            "Noah", "Aiden", "Sofia", "Amara", "Zara", "Priya", "Leila",
            "Elif", "Mert", "Burak", "Emre", "Arda", "Kerem", "Sercan"
    );
    private static final List<String> LAST_NAMES = Arrays.asList(
            "Santos", "Müller", "Kim", "Rossi", "Diallo", "Chen", "Garcia",
            "Petrov", "Yılmaz", "Kaya", "Şahin", "Demir", "Çelik", "Arslan"
    );
    private static final List<String> TEAM_NAMES = Arrays.asList(
            "Red Eagles", "Blue Lions", "Black Panthers", "Green Falcons",
            "Silver Wolves", "Golden Bears", "Iron Hawks", "Storm United",
            "Crimson Tide", "Royal Knights", "Thunder FC", "Velocity SC"
    );
    private static final String[] FOOTBALL_POSITIONS   = {"GK", "DEF", "DEF", "MID", "MID", "ST"};
    private static final String[] VOLLEYBALL_POSITIONS = {"Setter", "Libero", "Outside", "Outside", "Middle", "Opposite"};

    public static void reset() {
        teamCounter = playerCounter = coachCounter = 1;
    }

    public static Team createTeam() {
        return createTeam(new FootballSport());
    }

    public static Team createTeam(Sport sport) {
        String name = teamCounter <= TEAM_NAMES.size()
                ? TEAM_NAMES.get(teamCounter - 1)
                : "Team " + teamCounter;
        teamCounter++;

        Team team = new Team(name);

        // BUG FIX: spor başına yeterli oyuncu
        int rosterSize = sport.getLineupSize() + 4;
        String[] positions = (sport instanceof VolleyballSport) ? VOLLEYBALL_POSITIONS : FOOTBALL_POSITIONS;

        for (int i = 0; i < rosterSize; i++) {
            String pos = positions[i % positions.length];
            team.addPlayer(new Player(randomName(), pos, 50 + RANDOM.nextInt(40)));
        }

        team.setCoach(createCoach());
        return team;
    }

    public static Player createPlayer() {
        String[] pos = {"GK", "DEF", "MID", "ST"};
        int idx = playerCounter++;
        return new Player(randomName(), pos[idx % pos.length], 40 + (idx % 30));
    }

    public static Coach createCoach() {
        return new Coach(randomName() + " (Coach " + coachCounter++ + ")", 5 + RANDOM.nextInt(10));
    }

    private static String randomName() {
        String first = FIRST_NAMES.get(RANDOM.nextInt(FIRST_NAMES.size()));
        String last  = LAST_NAMES.get(RANDOM.nextInt(LAST_NAMES.size()));
        return first + " " + last;
    }
}
