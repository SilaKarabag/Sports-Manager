package com.team10.sports;

import com.team10.domain.TeamRecord; // TeamRecord sınıfını burada tanıtmamız şart
import java.util.Comparator;         // Sıralama yapabilmek için gerekli
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

    /**
     * Spora özel sıralama kuralını döner.
     * Bu sayede futbol ve voleybol farklı kurallara (averaj, set farkı vb.) göre sıralanabilir.
     */
    Comparator<TeamRecord> getStandingComparator();
}