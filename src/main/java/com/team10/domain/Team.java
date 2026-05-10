package com.team10.domain;

import java.util.*;
import java.io.Serializable;

public class Team implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<Player> roster;
    private Coach coach;
    private Lineup currentLineup;

    // Taktik: ATTACKING, BALANCED, DEFENSIVE
    private String tactic = "BALANCED";

    public Team(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Team name cannot be null or blank.");
        this.name = name;
        this.roster = new ArrayList<>();
    }

    public String getName()   { return name; }
    public Coach  getCoach()  { return coach; }
    public String getTactic() { return tactic; }

    public void setTactic(String tactic) {
        if (tactic == null) return;
        this.tactic = tactic;
    }

    public void setCoach(Coach coach) {
        if (coach == null) throw new IllegalArgumentException("Coach cannot be null.");
        this.coach = coach;
    }

    public void addPlayer(Player player) {
        if (player == null) throw new IllegalArgumentException("Player cannot be null.");
        if (roster.contains(player)) throw new IllegalArgumentException("Player already exists in roster.");
        roster.add(player);
    }

    public List<Player> getPlayers()  { return Collections.unmodifiableList(roster); }
    public List<Player> getRoster()   { return Collections.unmodifiableList(roster); }
    public boolean containsPlayer(Player p) { return roster.contains(p); }

    public List<Player> getAvailablePlayers() {
        List<Player> av = new ArrayList<>();
        for (Player p : roster) if (p.isAvailable()) av.add(p);
        return av;
    }

    public void setCurrentLineup(Lineup lineup) {
        if (lineup != null && !lineup.getTeam().equals(this))
            throw new IllegalArgumentException("Lineup does not belong to this team.");
        this.currentLineup = lineup;
    }

    public Lineup getCurrentLineup() { return currentLineup; }

    // CRITICAL BUG FIX: equals/hashCode olmadan Map<Team,TeamRecord> bozuluyordu
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;
        return Objects.equals(name, ((Team) o).name);
    }

    @Override
    public int hashCode() { return Objects.hash(name); }

    @Override
    public String toString() { return name; }
}
