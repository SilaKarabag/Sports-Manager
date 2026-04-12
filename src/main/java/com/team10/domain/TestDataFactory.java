package com.team10.domain;

public class TestDataFactory {

    private static int teamCounter = 1;
    private static int playerCounter = 1;
    private static int coachCounter = 1;

    // TEAM
    public static Team createTeam() {
        Team team = new Team("Team " + teamCounter++);

        // Football lineup size is 11 → creates 15 players
        for (int i = 0; i < 15; i++) {
            team.addPlayer(createPlayer());
        }

        team.setCoach(createCoach());

        return team;
    }

    // PLAYER
    public static Player createPlayer() {
        String[] positions = {"GK", "DEF", "MID", "ST"};

        int index = playerCounter++;

        return new Player(
            "Player " + index,
            positions[index % positions.length],
            40 + (index % 30)
        );
    }

    // COACH
    public static Coach createCoach() {
        return new Coach(
            "Coach " + coachCounter++,
            5
        );
    }

}