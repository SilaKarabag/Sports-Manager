package com.team10.domain;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int MAX_SKILL = 100;

    private final String name;
    private final String position;
    private int skill;
    private int injuryMatches;

    public Player(String name, String position, int skill) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Player name cannot be null or blank.");
        if (position == null || position.trim().isEmpty())
            throw new IllegalArgumentException("Player position cannot be null or blank.");
        if (skill < 0 || skill > MAX_SKILL)
            throw new IllegalArgumentException("Skill must be between 0 and " + MAX_SKILL + ".");

        this.name = name;
        this.position = position;
        this.skill = skill;
        this.injuryMatches = 0;
    }

    public String getName()      { return name; }
    public String getPosition()  { return position; }
    public int    getSkill()     { return skill; }
    public int    getInjuryMatches() { return injuryMatches; }

    public boolean isAvailable() { return injuryMatches == 0; }
    public boolean isInjured()   { return injuryMatches > 0; }

    public void improveSkill(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Skill improvement cannot be negative.");
        this.skill = Math.min(skill + amount, MAX_SKILL);
    }

    public void injureForMatches(int matches) {
        if (matches <= 0)
            throw new IllegalArgumentException("Injury duration must be positive.");
        this.injuryMatches = matches;
    }

    public void recoverOneWeek() {
        if (injuryMatches > 0) injuryMatches--;
    }

    @Override
    public String toString() {
        return name + " [" + position + ", Skill:" + skill + (isInjured() ? ", INJ:" + injuryMatches : "") + "]";
    }
}