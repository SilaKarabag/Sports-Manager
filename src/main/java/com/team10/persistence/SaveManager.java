package com.team10.persistence;

import com.team10.domain.GameSession;

import java.io.*;

public class SaveManager {

    public void save(GameSession session, String filePath) throws IOException {
        if (session == null) {
            throw new IllegalArgumentException("Game session cannot be null.");
        }

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be empty.");
        }

        File file = new File(filePath);

        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(session);
        }
    }

    public GameSession load(String filePath) throws IOException, ClassNotFoundException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be empty.");
        }

        File file = new File(filePath);

        if (!file.exists()) {
            throw new FileNotFoundException("Save file not found: " + filePath);
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object object = in.readObject();

            if (!(object instanceof GameSession)) {
                throw new IOException("Selected file is not a valid Sports Manager save file.");
            }

            return (GameSession) object;
        }
    }
}
