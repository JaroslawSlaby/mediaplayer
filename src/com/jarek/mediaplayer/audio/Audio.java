package com.jarek.mediaplayer.audio;

import com.jarek.mediaplayer.api.GUI;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by jarek on 12/15/16.
 */
public class Audio extends GUI implements ActionListener {

    private FileInputStream fileInputStream;
    private BufferedInputStream bufferedInputStream;

    private long pauseLocation;
    private long totalLength;
    private boolean canResume = false;
    private boolean valid = false;
    private boolean stopped = true;

    private int i = 0;

    private Audio() {
        super();
        playPauseButton.addActionListener(this);
        stopButton.addActionListener(this);
        nextButton.addActionListener(this);
        previousButton.addActionListener(this);

//        nextButton.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                Pause();
//                Plus10seconds();
//                Resume();
//            }
//        });
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() == stopButton) {
            Stop();
            stopped = true;
            isPlaying = false;
            playPauseButton.setText("PLAY");
        }
        else if (e.getSource() == playPauseButton) {
            if (!isPlaying && stopped) {
                if (fileList != null) {
                    Play(fileList.get(i).getAbsolutePath(), -1);
                    isPlaying = true;
                    stopped = false;
                    playPauseButton.setText("PAUSE");
                }
            } else if (isPlaying && !stopped) {
                Pause();
                isPlaying = false;
                playPauseButton.setText("PLAY");
            } else if (!isPlaying) {

                isPlaying = true;
                playPauseButton.setText("PAUSE");
                Resume();
            }
        }
        else if(e.getSource() == nextButton) {
            i++;
                if(i == fileList.size()) {
                    Stop();
                    Play(fileList.get(0).getAbsolutePath(), -1);
                }
                else if(i < fileList.size()){
                    Stop();
                    Play(fileList.get(i).getAbsolutePath(), -1);
                }
        }
        else if(e.getSource() == previousButton) {
            i--;
                if(i < 0) {
                    i = fileList.size();
                    Stop();
                    Play(fileList.get(i-1).getAbsolutePath(), -1);
                }
                else {
                    Stop();
                    Play(fileList.get(i).getAbsolutePath(), -1);
                }
        }
    }

    private void Stop() {
        if(player != null)
            player.close();
    }

    private void Pause() {
            try {
                pauseLocation = fileInputStream.available();
                player.close();
                fileInputStream = null;
                bufferedInputStream = null;
                player = null;
                progressSong.setValue((int)pauseLocation);
                if(valid) canResume = true;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "IO Error! Check this out!",
                        "Error!", JOptionPane.ERROR_MESSAGE);
            }
    }

    private boolean Play(String path, long pos) {
        valid = true;
        canResume = false;
        try {
            fileInputStream = new FileInputStream(path);
            totalLength = fileInputStream.available();
            progressSong.setMaximum(((int) getFileLength() / 60 )* 100);
            System.out.println("Time: " + (int)getFileLength() / 60 + "." + getFileLength() % 60);
                    if(pos > -1)
                        fileInputStream.skip(pos);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            player = new AdvancedPlayer(bufferedInputStream);
        } catch (JavaLayerException | IOException e) {
            JOptionPane.showMessageDialog(this, "ERROR!");
            valid = false;
        }
        new Thread(
                () -> {
                    try {
                        player.play();
                        } catch (JavaLayerException e) {
                        valid = false;
                    }
                }).start();
        return valid;
    }

    private void Resume() {
        if(!canResume) return;
        if(Play(fileList.get(i).getAbsolutePath(),totalLength - pauseLocation)) {
            canResume = false;
        }
    }

    private double getFileLength() {
        double duration = 0;
        try {
            AudioFile audioFile = AudioFileIO.read(fileList.get(i));
            duration = audioFile.getAudioHeader().getTrackLength();
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        return duration;
    }

    private void Plus10seconds() {
        pauseLocation += (getFileLength() % 60);
    }

    private void Minus10seconds() {
        pauseLocation -=(getFileLength() % 60);
    }

    public static void main(String[] args) {
        Audio audio = new Audio();
        audio.makeGUI();
    }
}
