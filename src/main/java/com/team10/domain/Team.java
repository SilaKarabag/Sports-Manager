package com.team10.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.util.Objects;

/**
 * BUG FIX: Orijinal kodda equals/hashCode yoktu. standings Map<Team,TeamRecord>
 * içinde aynı takımın birden fazla kayıt oluşturmasına yol açıyordu.
 * FIX: equals/hashCode name'e göre eklendi.
 */
public class Team implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<Player> roster;
    private Coach coach;
    private Lineup currentLineup;

    public Team(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Team name cannot be null or blank.");
        this.name = name;
        this.roster = new ArrayList<>();
    }

    public String getName()    { return name; }
    public Coach  getCoach()   { return coach; }

    public void setCoach(Coach coach) {
        if (coach == null) throw new IllegalArgumentException("Coach cannot be null.");
        this.coach = coach;
    }

    public void addPlayer(Player player) {
        if (player == null) throw new IllegalArgumentException("Player cannot be null.");
        if (roster.contains(player)) throw new IllegalArgumentException("Player already in roster.");
        roster.add(player);
    }

    public List<Player> getPlayers()  { return Collections.unmodifiableList(roster); }
    public List<Player> getRoster()   { return Collections.unmodifiableList(roster); }

    public boolean containsPlayer(Player player) { return roster.contains(player); }

    public List<Player> getAvailablePlayers() {
        List<Player> available = new ArrayList<>();
        for (Player p : roster) {
            if (p.isAvailable()) available.add(p);
        }
        return available;
    }

    public void setCurrentLineup(Lineup lineup) {
        if (lineup != null && !lineup.getTeam().equals(this))
            throw new IllegalArgumentException("Lineup does not belong to this team.");
        this.currentLineup = lineup;
    }

    public Lineup getCurrentLineup() { return currentLineup; }

    // BUG FIX: equals ve hashCode olmadan HashMap<Team, TeamRecord> bozuluyordu.
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
