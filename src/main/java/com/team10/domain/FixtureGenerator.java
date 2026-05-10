package com.team10.domain;

import com.team10.sports.Sport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lig fikstürünü Round-Robin algoritması kullanarak oluşturan yardımcı sınıf.
 */
public class FixtureGenerator {

    /**
     * Ev sahibi ve deplasman olmak üzere çift devreli lig fikstürü oluşturur.
     * * @param teams Ligdeki takımların listesi
     * @param sport Oynanacak spor dalı
     * @return Haftalık maç listelerini içeren liste
     */
    public static List<List<Match>> generateFixture(List<Team> teams, Sport sport) {
        if (teams == null || teams.size() < 2) {
            throw new IllegalArgumentException("At least 2 teams are required to generate a fixture.");
        }

        // Orijinal listeyi bozmamak için kopya oluştur ve karıştır (fikstür her seferinde aynı olmasın)
        List<Team> fixtureTeams = new ArrayList<>(teams);
        Collections.shuffle(fixtureTeams);

        // Takım sayısı tek ise "BYE" takımı ekleyerek çift sayıya tamamla
        if (fixtureTeams.size() % 2 != 0) {
            fixtureTeams.add(new Team("BYE"));
        }

        int numTeams = fixtureTeams.size();
        int totalWeeksInHalf = numTeams - 1;
        int matchesPerWeek = numTeams / 2;

        List<List<Match>> firstHalfFixture = new ArrayList<>();
        List<List<Match>> secondHalfFixture = new ArrayList<>();

        // Round-Robin Algoritması
        for (int week = 0; week < totalWeeksInHalf; week++) {
            List<Match> weekMatches = new ArrayList<>();
            List<Match> secondHalfWeekMatches = new ArrayList<>();

            for (int match = 0; match < matchesPerWeek; match++) {
                int homeIndex = (week + match) % (numTeams - 1);
                int awayIndex = (numTeams - 1 - match + week) % (numTeams - 1);

                // Klasik Round-Robin: Son elemanı sabit tut
                if (match == 0) {
                    awayIndex = numTeams - 1;
                }

                Team homeTeam = fixtureTeams.get(homeIndex);
                Team awayTeam = fixtureTeams.get(awayIndex);

                // BYE takımı maç listesine eklenmez (O takım o hafta dinlenir)
                if (!homeTeam.getName().equals("BYE") && !awayTeam.getName().equals("BYE")) {
                    // İlk yarı maçı
                    weekMatches.add(new Match(homeTeam, awayTeam, sport));
                    // İkinci yarı maçı (Ev sahibi/Deplasman değişimi)
                    secondHalfWeekMatches.add(new Match(awayTeam, homeTeam, sport));
                }
            }
            firstHalfFixture.add(weekMatches);
            secondHalfFixture.add(secondHalfWeekMatches);
        }

        // Tüm sezonu birleştir (Önce ilk yarı, sonra ikinci yarı maçları)
        List<List<Match>> fullSeasonFixture = new ArrayList<>();
        fullSeasonFixture.addAll(firstHalfFixture);
        fullSeasonFixture.addAll(secondHalfFixture);

        return fullSeasonFixture;
    }
}