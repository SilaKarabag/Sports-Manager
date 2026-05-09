package com.team10.sports;
import java.io.Serializable;

public abstract class AbstractSport implements Sport, Serializable  {
    private static final long serialVersionUID = 1L;

    private final String sportName;
    private final int lineupSize;
    private final int substituteLimit;
    private final int pointsForWin;
    private final int pointsForDraw;
    private final int pointsForLoss;
    private final int quarterCount;

    public AbstractSport(String sportName, int lineupSize, int substituteLimit,
                         int pointsForWin, int pointsForDraw, int pointsForLoss,
                         int quarterCount) {
        this.sportName = sportName;
        this.lineupSize = lineupSize;
        this.substituteLimit = substituteLimit;
        this.pointsForWin = pointsForWin;
        this.pointsForDraw = pointsForDraw;
        this.pointsForLoss = pointsForLoss;
        this.quarterCount = quarterCount;
    }

    @Override public String getSportName()     { return sportName; }
    @Override public int getLineupSize()        { return lineupSize; }
    @Override public int getSubstituteLimit()   { return substituteLimit; }
    @Override public int getPointsForWin()      { return pointsForWin; }
    @Override public int getPointsForDraw()     { return pointsForDraw; }
    @Override public int getPointsForLoss()     { return pointsForLoss; }
    @Override public int getQuarterCount()      { return quarterCount; }
}