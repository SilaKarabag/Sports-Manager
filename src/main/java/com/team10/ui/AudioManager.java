package com.team10.ui;
import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class AudioManager {

    private static MediaPlayer bgmPlayer;

    private static boolean soundOn = true;

    public static void startBGM() {

        if (!soundOn) return;

        if (bgmPlayer != null) {
            if (!bgmPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                bgmPlayer.play();
            }
            return;
        }

        URL resource = AudioManager.class.getResource("/audio/bgm.mp3");
        if (resource == null) return;

        Media media = new Media(resource.toExternalForm());
        bgmPlayer = new MediaPlayer(media);

        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgmPlayer.setVolume(0.3);
        bgmPlayer.play();
    }

    public static void stopBGM() {

        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.dispose();
            bgmPlayer = null;
        }
    }

    public static void toggleSound() {

        soundOn = !soundOn;

        if (!soundOn) {
            stopBGM();
        } else {
            startBGM();
        }
    }

    public static String getSoundText() {
        return soundOn ? "Sound OFF" : "Sound ON";
    }

}