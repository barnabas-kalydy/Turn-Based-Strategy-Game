package com.kbarnabas99.turn_based_strategy.logic;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        playMainSong();

        new GameLoop().start(primaryStage);

    }

    private void playMainSong() {
        // FIXME: the music don't play(sometimes it plays)
        try {
            AudioInputStream audioInputStream = AudioSystem
                    .getAudioInputStream(new File("src/main/resources/music/aoe3_theme_song.wav")
                            .getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error with playing sound.");
            e.printStackTrace();
        }
    }

}
