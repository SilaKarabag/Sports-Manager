package com.team10.sports;

import com.team10.domain.TeamRecord; // TeamRecord importu şart
import java.io.Serializable;
import java.util.Comparator;

public class FootballSport extends AbstractSport implements Serializable {
    private static final long serialVersionUID = 1L;

    public FootballSport() {
        // Futbol: 11 kişi, 5 yedek hakkı, 3 puan galibiyet, 1 puan beraberlik, 0 puan mağlubiyet, 2 yarı
        super("Football", 11, 5, 3, 1, 0, 2);
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

    /**
     * Futbol için standart sıralama kuralı:
     * 1. Puan
     * 2. Puan eşitse Genel Averaj (Goal Difference)
     */
    @Override
    public Comparator<TeamRecord> getStandingComparator() {
        return (r1, r2) -> {
            // Önce puanlara bak (Büyük olan üstte)
            if (r1.getPoints() != r2.getPoints()) {
                return Integer.compare(r2.getPoints(), r1.getPoints());
            }
            // Puanlar eşitse averaja bak (Büyük olan üstte)
            return Integer.compare(r2.getGoalDifference(), r1.getGoalDifference());
        };
    }
}