package com.team10.sports;
import java.io.Serializable;

public interface Sport extends Serializable {

    String getSportName();

    int getLineupSize();

    int getSubstituteLimit();

    int getPointsForWin();

    int getPointsForDraw();

    int getPointsForLoss();

    int getQuarterCount();

    boolean isValidPlayerCount(int playerCount);

    boolean isValidScore(int homeScore, int awayScore);

    int calculatePointsFromScore(int scored, int conceded);
}