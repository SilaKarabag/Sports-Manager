package com.team10.sports;

import java.io.Serializable;


public class VolleyballSport extends AbstractSport implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String SPORT_NAME      = "Volleyball";
    public static final int    LINEUP_SIZE     = 6;
    public static final int    SUBSTITUTE_LIMIT = 6;
    public static final int    SET_COUNT       = 5;
    public static final int    POINTS_FOR_WIN  = 3;
    public static final int    POINTS_FOR_DRAW = 0;
    public static final int    POINTS_FOR_LOSS = 0;

    public VolleyballSport() {
        super(SPORT_NAME,
                LINEUP_SIZE,
                SUBSTITUTE_LIMIT,
                POINTS_FOR_WIN,
                POINTS_FOR_DRAW,
                POINTS_FOR_LOSS,
                SET_COUNT);
    }

    @Override
    public boolean isValidPlayerCount(int playerCount) {
        return playerCount == getLineupSize();
    }

    @Override
    public boolean isValidScore(int homeScore, int awayScore) {
        return homeScore >= 0 && awayScore >= 0;
    }

    @Override
    public int calculatePointsFromScore(int scored, int conceded) {
        if (!isValidScore(scored, conceded)) {
            throw new IllegalArgumentException("Scores cannot be negative.");
        }
        if (scored > conceded) return getPointsForWin();
        if (scored == conceded) return getPointsForDraw();
        return getPointsForLoss();
    }

    /** Volleyball-specific: at most {@link #SUBSTITUTE_LIMIT} substitutions per match. */
    public boolean isValidSubstitutionCount(int substitutions) {
        return substitutions >= 0 && substitutions <= getSubstituteLimit();
    }

    /**
     * Volleyball winner side, derived from raw match scores.
     *
     * @return  1  if the home side scored more,
     *         -1  if the away side scored more,
     *          0  if the totals are equal (rare in real volleyball but
     *             possible inside the random simulator).
     */
    public int determineWinnerSide(int homeScore, int awayScore) {
        if (!isValidScore(homeScore, awayScore)) {
            throw new IllegalArgumentException("Scores cannot be negative.");
        }
        if (homeScore > awayScore) return 1;
        if (awayScore > homeScore) return -1;
        return 0;
    }
}