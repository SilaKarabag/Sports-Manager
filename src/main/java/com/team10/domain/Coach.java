package com.team10.domain;

import java.io.Serializable;

public class Coach implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final int trainingBonus;

    public Coach(String name, int trainingBonus) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Coach name cannot be null or blank.");
        if (trainingBonus < 0)
            throw new IllegalArgumentException("Training bonus cannot be negative.");
        this.name = name;
        this.trainingBonus = trainingBonus;
    }

    public String getName()        { return name; }
    public int getTrainingBonus()  { return trainingBonus; }

    public void trainPlayer(Player player) {
        if (player == null)
            throw new IllegalArgumentException("Player cannot be null.");
        player.improveSkill(trainingBonus);
    }

    @Override
    public String toString() {
        return name + " (bonus+" + trainingBonus + ")";
    }
}
