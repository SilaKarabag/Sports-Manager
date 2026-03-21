package com.team10.sports;

public interface Sport {

    String getSportName();

    int getLineupSize();

    int getSubstituteLimit();

    int getPointsForWin();

    int getPointsForDraw();

    int getPointsForLoss();

    boolean isValidPlayerCount(int playerCount);

    boolean isValidScore(int homeScore, int awayScore);

    int calculatePointsFromScore(int scored, int conceded);
}