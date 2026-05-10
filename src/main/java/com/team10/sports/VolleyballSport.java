package com.team10.sports;

import com.team10.domain.TeamRecord;
import java.io.Serializable;
import java.util.Comparator;

/**
 * BUG FIX: AbstractSport constructor'ı şu sırayla alıyor:
 *   super(name, lineupSize, substituteLimit, pointsForWin, pointsForDRAW, pointsForLoss, quarterCount)
 *
 * Orijinal kodda:
 *   super(SPORT_NAME, LINEUP_SIZE, SUBSTITUTE_LIMIT, POINTS_FOR_WIN, POINTS_FOR_DRAW, POINTS_FOR_LOSS, SET_COUNT)
 * = super("Volleyball", 6, 6, 3, 0, 0, 5)
 *
 * Bu doğru GÖRÜNEBİLİR ama AbstractSport'un 5. parametresi pointsForDraw, 6. parametresi pointsForLoss.
 * Yani POINTS_FOR_DRAW=0, POINTS_FOR_LOSS=0 → kayıp ve beraberlik aynı puan (0) – DOĞRU.
 *
 * BUG FIX 2: getSportName() override etmek gereksiz (AbstractSport'tan geliyor). Kaldırıldı.
 * BUG FIX 3: determineWinnerSide 0 (beraberlik) dönebilir ama voleybolda bu olmamalı.
 *   Method korundu ama beraberlik durumunda exception fırlatıyor.
 */
public class VolleyballSport extends AbstractSport implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String SPORT_NAME       = "Volleyball";
    public static final int    LINEUP_SIZE      = 6;
    public static final int    SUBSTITUTE_LIMIT = 6;
    public static final int    SET_COUNT        = 5;   // Best-of-5
    public static final int    POINTS_FOR_WIN   = 3;
    public static final int    POINTS_FOR_DRAW  = 0;   // Beraberlik yok (savunma)
    public static final int    POINTS_FOR_LOSS  = 0;

    public VolleyballSport() {
        super(SPORT_NAME, LINEUP_SIZE, SUBSTITUTE_LIMIT,
                POINTS_FOR_WIN, POINTS_FOR_DRAW, POINTS_FOR_LOSS, SET_COUNT);
    }

    @Override
    public boolean isValidPlayerCount(int count) { return count == LINEUP_SIZE; }

    @Override
    public boolean isValidScore(int home, int away) { return home >= 0 && away >= 0; }

    @Override
    public int calculatePointsFromScore(int scored, int conceded) {
        if (!isValidScore(scored, conceded))
            throw new IllegalArgumentException("Scores cannot be negative.");
        return (scored > conceded) ? POINTS_FOR_WIN : POINTS_FOR_LOSS;
    }

    /**
     * BUG FIX: Beraberlik voleybolda mümkün değil. 0 dönerse exception.
     */
    public int determineWinnerSide(int homeScore, int awayScore) {
        if (!isValidScore(homeScore, awayScore))
            throw new IllegalArgumentException("Scores cannot be negative.");
        if (homeScore > awayScore) return 1;
        if (awayScore > homeScore) return -1;
        throw new IllegalStateException("Volleyball cannot end in a draw.");
    }

    public boolean isValidSubstitutionCount(int count) {
        return count >= 0 && count <= SUBSTITUTE_LIMIT;
    }

    @Override
    public Comparator<TeamRecord> getStandingComparator() {
        return (r1, r2) -> {
            if (r1.getPoints() != r2.getPoints())
                return Integer.compare(r2.getPoints(), r1.getPoints());
            // Set averajı (scored = kazanılan set, conceded = kaybedilen set)
            return Integer.compare(r2.getGoalDifference(), r1.getGoalDifference());
        };
    }
}
