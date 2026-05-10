package com.team10.sports;

import com.team10.domain.TeamRecord; // Importu unutma
import java.io.Serializable;
import java.util.Comparator;

public class VolleyballSport extends AbstractSport implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String SPORT_NAME      = "Volleyball";
    public static final int    LINEUP_SIZE     = 6;
    public static final int    SUBSTITUTE_LIMIT = 6;
    public static final int    SET_COUNT       = 5;
    public static final int POINTS_FOR_WIN  = 3;
    public static final int POINTS_FOR_LOSS = 0;
    public static final int    POINTS_FOR_DRAW = 0; // Beraberlik yok


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
    public boolean isValidSubstitutionCount(int substitutionCount) {
        return substitutionCount >= 0 && substitutionCount <= getSubstituteLimit();
    }

    public int determineWinnerSide(int homeScore, int awayScore) {
        if (!isValidScore(homeScore, awayScore)) {
            throw new IllegalArgumentException("Scores cannot be negative.");
        }

        if (homeScore > awayScore) {
            return 1;
        }

        if (awayScore > homeScore) {
            return -1;
        }

        return 0;
    }

    @Override
    public int calculatePointsFromScore(int scored, int conceded) {
        if (!isValidScore(scored, conceded)) {
            throw new IllegalArgumentException("Scores cannot be negative.");
        }
        // Voleybolda beraberlik olmaz, set skoru üzerinden puan verilir
        if (scored > conceded) return getPointsForWin();
        return getPointsForLoss();
    }

    /**
     * Voleybol için özel sıralama kuralı:
     * 1. Önce Puana bakılır.
     * 2. Puan eşitse Set Averajına (score difference) bakılır.
     */
    @Override
    public Comparator<TeamRecord> getStandingComparator() {
        return (r1, r2) -> {
            if (r1.getPoints() != r2.getPoints()) {
                return Integer.compare(r2.getPoints(), r1.getPoints());
            }
            // Voleybolda averaj yerine set farkı (scored - conceded) önemlidir
            return Integer.compare(r2.getGoalDifference(), r1.getGoalDifference());
        };
    }

    // Gereksiz metodları temizledik, Interface'deki standartları kullanıyoruz.
    @Override
    public String getSportName() {
        return SPORT_NAME;
    }
}