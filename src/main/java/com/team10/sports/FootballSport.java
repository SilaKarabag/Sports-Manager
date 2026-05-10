package com.team10.sports;

import com.team10.domain.TeamRecord;
import java.io.Serializable;
import java.util.Comparator;

/**
 * BUG FIX: Orijinal kodda tiebreak yalnızca puan → averaj idi.
 * Proje açıklaması puan eşitse önce H2H maçına bakılmasını söylüyor.
 * H2H League.java'da fikstüre erişim gerektirdiğinden Comparator içinde
 * yapılamaz; bu nedenle not düşülmüş ve averaj tiebreak korunmuştur.
 * H2H için getSortedStandingsWithH2H() League'e eklendi.
 *
 * BUG FIX 2: Futbol 2 yarı (half), headball 4 çeyrek. Rubric'te "2 yarı" yazan
 * konfigürasyon korundu.
 */
public class FootballSport extends AbstractSport implements Serializable {
    private static final long serialVersionUID = 1L;

    public FootballSport() {
        // Football: 11 kişi, 5 yedek, 3W/1D/0L, 2 yarı
        super("Football", 11, 5, 3, 1, 0, 2);
    }

    @Override
    public boolean isValidPlayerCount(int count) { return count == getLineupSize(); }

    @Override
    public boolean isValidScore(int home, int away) { return home >= 0 && away >= 0; }

    @Override
    public int calculatePointsFromScore(int scored, int conceded) {
        if (!isValidScore(scored, conceded))
            throw new IllegalArgumentException("Scores cannot be negative.");
        if (scored > conceded) return getPointsForWin();
        if (scored == conceded) return getPointsForDraw();
        return getPointsForLoss();
    }

    @Override
    public Comparator<TeamRecord> getStandingComparator() {
        return (r1, r2) -> {
            // 1. Puan
            if (r1.getPoints() != r2.getPoints())
                return Integer.compare(r2.getPoints(), r1.getPoints());
            // 2. Averaj (H2H ayrı metod gerektirir, burada genel averaj)
            if (r1.getGoalDifference() != r2.getGoalDifference())
                return Integer.compare(r2.getGoalDifference(), r1.getGoalDifference());
            // 3. Atılan gol
            return Integer.compare(r2.getScored(), r1.getScored());
        };
    }
}
