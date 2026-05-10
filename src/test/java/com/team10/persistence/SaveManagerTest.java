package com.team10.persistence;

import com.team10.domain.GameController;
import com.team10.domain.GameSession;
import com.team10.domain.TestDataFactory;
import com.team10.sports.FootballSport;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SaveManagerTest {

    @Test
    public void saveAndLoadGameSessionTest() throws Exception {
        GameController controller = new GameController();

        controller.startNewGame(
                new FootballSport(),
                List.of(
                        TestDataFactory.createTeam(),
                        TestDataFactory.createTeam(),
                        TestDataFactory.createTeam(),
                        TestDataFactory.createTeam()
                )
        );

        controller.playNextWeek();

        GameSession originalSession = controller.getSession();

        File tempFile = File.createTempFile("sports-manager-test-save", ".dat");
        tempFile.deleteOnExit();

        SaveManager saveManager = new SaveManager();
        saveManager.save(originalSession, tempFile.getAbsolutePath());

        GameSession loadedSession = saveManager.load(tempFile.getAbsolutePath());

        assertNotNull(loadedSession);
        assertNotNull(loadedSession.getSport());
        assertNotNull(loadedSession.getLeague());

        assertEquals(
                originalSession.getLeague().getCurrentWeek(),
                loadedSession.getLeague().getCurrentWeek()
        );

        assertEquals(
                originalSession.getLeague().getTeams().size(),
                loadedSession.getLeague().getTeams().size()
        );
    }

    @Test
    public void saveNullSessionThrowsExceptionTest() {
        SaveManager saveManager = new SaveManager();

        assertThrows(
                IllegalArgumentException.class,
                () -> saveManager.save(null, "invalid.dat")
        );
    }

    @Test
    public void loadMissingFileThrowsExceptionTest() {
        SaveManager saveManager = new SaveManager();

        assertThrows(
                Exception.class,
                () -> saveManager.load("file-that-does-not-exist.dat")
        );
    }
}
