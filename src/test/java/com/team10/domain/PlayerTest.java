package com.team10.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void shouldCreatePlayerWithCorrectValues() {
        Player player = new Player("Ali", "Forward", 75);

        assertEquals("Ali", player.getName());
        assertEquals("Forward", player.getPosition());
        assertEquals(75, player.getSkill());
        assertEquals(0, player.getInjuryWeeks());
        assertTrue(player.isAvailable());
        assertFalse(player.isInjured());
    }

    @Test
    void shouldThrowExceptionForBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player("", "Forward", 70));
    }

    @Test
    void shouldThrowExceptionForBlankPosition() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player("Ali", "", 70));
    }

    @Test
    void shouldThrowExceptionForNegativeSkill() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player("Ali", "Forward", -1));
    }

    @Test
    void shouldImproveSkill() {
        Player player = new Player("Ali", "Forward", 70);

        player.improveSkill(5);

        assertEquals(75, player.getSkill());
    }

    @Test
    void shouldThrowExceptionForNegativeSkillImprovement() {
        Player player = new Player("Ali", "Forward", 70);

        assertThrows(IllegalArgumentException.class,
                () -> player.improveSkill(-3));
    }

    @Test
    void shouldMarkPlayerAsInjuredForGivenWeeks() {
        Player player = new Player("Ali", "Forward", 70);

        player.injureForMatches(3);

        assertEquals(3, player.getInjuryWeeks());
        assertTrue(player.isInjured());
        assertFalse(player.isAvailable());
    }

    @Test
    void shouldThrowExceptionForInvalidInjuryWeeks() {
        Player player = new Player("Ali", "Forward", 70);

        assertThrows(IllegalArgumentException.class,
                () -> player.injureForMatches(0));
    }

    @Test
    void shouldRecoverOneWeek() {
        Player player = new Player("Ali", "Forward", 70);
        player.injureForMatches(2);

        player.recoverOneWeek();

        assertEquals(1, player.getInjuryWeeks());
        assertTrue(player.isInjured());
    }

    @Test
    void shouldBecomeAvailableAfterRecovery() {
        Player player = new Player("Ali", "Forward", 70);
        player.injureForMatches(1);

        player.recoverOneWeek();

        assertEquals(0, player.getInjuryWeeks());
        assertTrue(player.isAvailable());
        assertFalse(player.isInjured());
    }
}