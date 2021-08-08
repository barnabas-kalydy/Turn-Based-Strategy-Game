package com.kbarnabas99.turn_based_strategy.logic;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.logging.Logger;

public class Main extends Application {

    private final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
//        playMainSong();

        new GameLoop().start(primaryStage);
    }

    private void playMainSong() {
        try {
            AudioInputStream audioInputStream = AudioSystem
                    .getAudioInputStream(new File("src/main/resources/music/aoe3_theme_song.wav")
                            .getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            LOGGER.warning("Problem playing main sound!");
        }
    }

}
