package com.team10.domain;

public class Player {

    private final String name;
    private final String position;
    private int skill;
    private int injuryMatches;

    public Player(String name, String position, int skill) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or blank.");
        }
        if (position == null || position.trim().isEmpty()) {
            throw new IllegalArgumentException("Player position cannot be null or blank.");
        }
        if (skill < 0) {
            throw new IllegalArgumentException("Skill cannot be negative.");
        }

        this.name = name;
        this.position = position;
        this.skill = skill;
        this.injuryMatches = 0;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public int getSkill() {
        return skill;
    }

    public int getInjuryWeeks() {
        return injuryMatches;
    }

    public boolean isAvailable() {
        return injuryMatches == 0;
    }

    public boolean isInjured() {
        return injuryMatches > 0;
    }

    public void improveSkill(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Skill improvement cannot be negative.");
        }
        skill += amount;
    }

    public void injureForMatches(int matches) {
        if (matches <= 0) {
            throw new IllegalArgumentException("Injury matches must be positive.");
        }
        injuryMatches = matches;
    }

    public void recoverOneWeek() {
        if (injuryMatches > 0) {
            injuryMatches--;
        }
    }
}