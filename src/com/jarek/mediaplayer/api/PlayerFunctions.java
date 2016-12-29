package com.jarek.mediaplayer.api;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import com.jarek.mediaplayer.audio.Audio;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

/**
 * Created by jarek on 12/29/16.
 */
public class PlayerFunctions {

    private long pauseLocation;
    private long totalLength;
    private boolean canResume = false;
    private boolean valid = false;
    private Audio audioPlayer;

    PlayerFunctions() {
    }

    public PlayerFunctions(Audio audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public void Stop() {
        if(audioPlayer.player != null)
            audioPlayer.player.close();
    }

    public void Pause() {
        try {
            pauseLocation = audioPlayer.fileInputStream.available();
            audioPlayer.player.close();
            audioPlayer.fileInputStream = null;
            audioPlayer.bufferedInputStream = null;
            audioPlayer.player = null;
            audioPlayer.progressSong.setValue((int)pauseLocation);
            if(valid) canResume = true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(audioPlayer, "IO Error! Check this out!",
                    "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean Play(String path, long pos) {
        valid = true;
        canResume = false;
        try {
            audioPlayer.fileInputStream = new FileInputStream(path);
            totalLength = audioPlayer.fileInputStream.available();
            audioPlayer.progressSong.setMaximum(((int) getFileLength() / 60 )* 100);
            System.out.println("Time: " + (int)getFileLength() / 60 + "." + getFileLength() % 60);
            if(pos > -1)
                audioPlayer.fileInputStream.skip(pos);
            audioPlayer.bufferedInputStream = new BufferedInputStream(audioPlayer.fileInputStream);
            audioPlayer.player = new AdvancedPlayer(audioPlayer.bufferedInputStream);
        } catch (JavaLayerException | IOException e) {
            JOptionPane.showMessageDialog(audioPlayer, "ERROR!");
            valid = false;
        }
        new Thread(
                () -> {
                    try {
                        audioPlayer.player.play();
                    } catch (JavaLayerException e) {
                        valid = false;
                    }
                }).start();
        return valid;
    }

    public void Resume() {
        if(!canResume) return;
        if(Play(audioPlayer.fileList.get(audioPlayer.i).getAbsolutePath(),totalLength - pauseLocation)) {
            canResume = false;
        }
    }

    private void Plus10seconds() {
        pauseLocation += (getFileLength() % 60);
    }

    private void Minus10seconds() {
        pauseLocation -=(getFileLength() % 60);
    }

    private double getFileLength() {
        double duration = 0;
        try {
            AudioFile audioFile = AudioFileIO.read(audioPlayer.fileList.get(audioPlayer.i));
            duration = audioFile.getAudioHeader().getTrackLength();
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        return duration;
    }
}
