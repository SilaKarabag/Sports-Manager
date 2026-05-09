package com.team10.ui;
import javafx.scene.media.MediaPlayer;


public class AudioManager {

    private static MediaPlayer bgmPlayer;

    private static boolean soundOn = true;

    //private static final String BGM_PATH = "/src/audio/bgm.mp3";

    public static void startBGM() {

        if (!soundOn) return;

        if (bgmPlayer != null) return;

        //resource problem will fix later

        //Media media = new Media(resource.toString());
        //bgmPlayer = new MediaPlayer(media);

        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgmPlayer.setVolume(0.5);
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

    public static boolean isSoundOn() {
        return soundOn;
    }
}