package com.team10.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Team {

    private final String name;
    private final List<Player> roster;
    private Coach coach;
    private Lineup currentLineup; // Takımın aktif kadrosu

    public Team(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or blank.");
        }
        this.name = name;
        this.roster = new ArrayList<Player>();
    }

    public String getName() {
        return name;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        if (coach == null) {
            throw new IllegalArgumentException("Coach cannot be null.");
        }
        this.coach = coach;
    }

    public void addPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        if (roster.contains(player)) {
            throw new IllegalArgumentException("Player already exists in roster.");
        }
        roster.add(player);
    }

    /** getRoster() ile aynı — League.java'nın getPlayers() çağrısını karşılar */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(roster);
    }

    public List<Player> getRoster() {
        return Collections.unmodifiableList(roster);
    }

    public boolean containsPlayer(Player player) {
        return roster.contains(player);
    }

    public List<Player> getAvailablePlayers() {
        List<Player> availablePlayers = new ArrayList<Player>();
        for (Player player : roster) {
            if (player.isAvailable()) {
                availablePlayers.add(player);
            }
        }
        return availablePlayers;
    }

    /** Aktif kadroyu set et (maç öncesi UI veya otomatik atama tarafından çağrılır) */
    public void setCurrentLineup(Lineup lineup) {
        if (lineup != null && !lineup.getTeam().equals(this)) {
            throw new IllegalArgumentException("Lineup does not belong to this team.");
        }
        this.currentLineup = lineup;
    }

    /** League.java'nın maç öncesi çağırdığı metod */
    public Lineup getCurrentLineup() {
        return currentLineup;
    }
}