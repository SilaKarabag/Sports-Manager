package com.team10.sports;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VolleyballSportTest {

    @Test
    void shouldReturnCorrectSportName() {
        VolleyballSport sport = new VolleyballSport();
        assertEquals("Volleyball", sport.getSportName());
    }

    @Test
    void shouldReturnCorrectLineupSize() {
        VolleyballSport sport = new VolleyballSport();
        assertEquals(6, sport.getLineupSize());
    }

    @Test
    void shouldReturnCorrectSubstitutionLimit() {
        VolleyballSport sport = new VolleyballSport();
        assertEquals(6, sport.getSubstituteLimit());
    }

    @Test
    void shouldReturnCorrectQuarterCount() {
        VolleyballSport sport = new VolleyballSport();
        // best-of-5 sets
        assertEquals(5, sport.getQuarterCount());
    }

    @Test
    void shouldReturnCorrectPointsForWin() {
        VolleyballSport sport = new VolleyballSport();
        assertEquals(3, sport.getPointsForWin());
    }

    @Test
    void shouldReturnCorrectPointsForDraw() {
        VolleyballSport sport = new VolleyballSport();
        // Volleyball does not officially allow draws — modelled as 0.
        assertEquals(0, sport.getPointsForDraw());
    }

    @Test
    void shouldReturnCorrectPointsForLoss() {
        VolleyballSport sport = new VolleyballSport();
        assertEquals(0, sport.getPointsForLoss());
    }

    @Test
    void shouldAcceptValidPlayerCount() {
        VolleyballSport sport = new VolleyballSport();
        assertTrue(sport.isValidPlayerCount(6));
    }

    @Test
    void shouldRejectTooFewPlayers() {
        VolleyballSport sport = new VolleyballSport();
        assertFalse(sport.isValidPlayerCount(5));
    }

    @Test
    void shouldRejectTooManyPlayers() {
        VolleyballSport sport = new VolleyballSport();
        assertFalse(sport.isValidPlayerCount(7));
    }

    @Test
    void shouldRejectZeroPlayers() {
        VolleyballSport sport = new VolleyballSport();
        assertFalse(sport.isValidPlayerCount(0));
    }

    @Test
    void shouldAcceptValidScore() {
        VolleyballSport sport = new VolleyballSport();
        assertTrue(sport.isValidScore(25, 23));
    }

    @Test
    void shouldAcceptZeroZeroScore() {
        VolleyballSport sport = new VolleyballSport();
        assertTrue(sport.isValidScore(0, 0));
    }

    @Test
    void shouldRejectNegativeHomeScore() {
        VolleyballSport sport = new VolleyballSport();
        assertFalse(sport.isValidScore(-1, 25));
    }

    @Test
    void shouldRejectNegativeAwayScore() {
        VolleyballSport sport = new VolleyballSport();
        assertFalse(sport.isValidScore(25, -1));
    }

    @Test
    void shouldReturnWinPointsWhenScoredMore() {
        VolleyballSport sport = new VolleyballSport();
        assertEquals(3, sport.calculatePointsFromScore(125, 100));
    }

    @Test
    void shouldReturnDrawPointsWhenScoresAreEqual() {
        VolleyballSport sport = new VolleyballSport();
        // No draws in real volleyball; modelled as 0 league points.
        assertEquals(0, sport.calculatePointsFromScore(110, 110));
    }

    @Test
    void shouldReturnLossPointsWhenScoredLess() {
        VolleyballSport sport = new VolleyballSport();
        assertEquals(0, sport.calculatePointsFromScore(95, 110));
    }

    @Test
    void shouldThrowExceptionForNegativeScoresInPointCalculation() {
        VolleyballSport sport = new VolleyballSport();
        assertThrows(IllegalArgumentException.class,
                () -> sport.calculatePointsFromScore(-1, 5));
    }

    @Test
    void shouldThrowExceptionForNegativeAwayScoreInPointCalculation() {
        VolleyballSport sport = new VolleyballSport();
        assertThrows(IllegalArgumentException.class,
                () -> sport.calculatePointsFromScore(5, -3));
    }

    @Test
    void shouldAcceptValidSubstitutionCount() {
        VolleyballSport sport = new VolleyballSport();
        assertTrue(sport.isValidSubstitutionCount(0));
        assertTrue(sport.isValidSubstitutionCount(3));
        assertTrue(sport.isValidSubstitutionCount(6));
    }

    @Test
    void shouldRejectSubstitutionCountAboveLimit() {
        VolleyballSport sport = new VolleyballSport();
        assertFalse(sport.isValidSubstitutionCount(7));
    }

    @Test
    void shouldRejectNegativeSubstitutionCount() {
        VolleyballSport sport = new VolleyballSport();
        assertFalse(sport.isValidSubstitutionCount(-1));
    }

    @Test
    void shouldDetermineHomeWinner() {
        VolleyballSport sport = new VolleyballSport();
        assertEquals(1, sport.determineWinnerSide(125, 100));
    }

    @Test
    void shouldDetermineAwayWinner() {
        VolleyballSport sport = new VolleyballSport();
        assertEquals(-1, sport.determineWinnerSide(80, 110));
    }

    @Test
    void shouldDetermineDrawAsZero() {
        VolleyballSport sport = new VolleyballSport();
        assertEquals(0, sport.determineWinnerSide(100, 100));
    }

    @Test
    void shouldThrowOnNegativeScoresInWinnerDetermination() {
        VolleyballSport sport = new VolleyballSport();
        assertThrows(IllegalArgumentException.class,
                () -> sport.determineWinnerSide(-1, 100));
    }

    @Test
    void shouldImplementSportInterface() {
        VolleyballSport sport = new VolleyballSport();
        assertTrue(sport instanceof Sport);
    }

    @Test
    void shouldExtendAbstractSport() {
        VolleyballSport sport = new VolleyballSport();
        assertTrue(sport instanceof AbstractSport);
    }
}
