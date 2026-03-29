package com.team10;

import com.team10.domain.*;
import com.team10.sports.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App {
    public static void main(String[] args) {
        System.out.println("--- Sports Manager Simulation Starting ---");

        // 1. Choose the sport (testing with football )
        Sport football = new FootballSport();

        // 2. Create Teams
        Team teamA = new Team("Red Eagles");
        Team teamB = new Team("Blue Lions");
        Team teamC = new Team("Green Falcons");
        Team teamD = new Team("Black Panthers");

        List<Team> teams = new ArrayList<Team>();
        teams.add(teamA);
        teams.add(teamB);
        teams.add(teamC);
        teams.add(teamD);

        // 3. Add Random Players and a Coach to Each Team
        Random random = new Random();
        for (Team team : teams) {
            team.setCoach(new Coach(team.getName() + " Coach", random.nextInt(10)));
            // Adding 11 players to the roster
            for (int i = 1; i <= 11; i++) {
                team.addPlayer(new Player(team.getName() + " Player " + i, "Position", 50 + random.nextInt(50)));
            }
        }

        // 4. Set up the League (Initializing the core engine)
        League superLeague = new League(teams, football);

        // 5. Simulate the League (Play all fixtures week by week)
        System.out.println("\nLeague is starting...");
        while (!superLeague.isLeagueFinished()) {
            System.out.println("Playing Week " + superLeague.getCurrentWeek() + "...");
            superLeague.playNextWeek();
        }
        System.out.println("League finished!\n");

        // 6. Print the Standings
        System.out.println("--- STANDINGS ---");
        // P: Played, W: Wins, D: Draws, L: Losses, GF: Goals For, GA: Goals Against, Pts: Points
        System.out.printf("%-15s | %-2s | %-2s | %-2s | %-2s | %-3s | %-3s | %-4s%n",
                "Team", "P", "W", "D", "L", "GF", "GA", "Pts");
        System.out.println("------------------------------------------------------------");

        for (TeamRecord record : superLeague.getSortedStandings()) {
            System.out.printf("%-15s | %-2d | %-2d | %-2d | %-2d | %-3d | %-3d | %-4d%n",
                    record.getTeam().getName(),
                    record.getMatchesPlayed(),
                    record.getWins(),
                    record.getDraws(),
                    record.getLosses(),
                    record.getScored(),
                    record.getConceded(),
                    record.getPoints()
            );
        }
    }
}