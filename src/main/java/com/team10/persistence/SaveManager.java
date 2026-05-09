package com.team10.persistence;

import com.team10.domain.GameSession;

import java.io.*;

public class SaveManager {

    public void save(GameSession session, String filePath) throws IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(filePath))) {

            out.writeObject(session);
        }
    }

    public GameSession load(String filePath)
            throws IOException, ClassNotFoundException {

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(filePath))) {

            return (GameSession) in.readObject();
        }
    }
}

