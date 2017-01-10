package com.jarek.mediaplayer.audio;

import com.jarek.mediaplayer.api.GUI;
import com.jarek.mediaplayer.api.PlayerFunctions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Random;

/**
 * Created by jarek on 12/15/16.
 *
 */
public class Audio extends GUI implements ActionListener {

    public FileInputStream fileInputStream;
    public BufferedInputStream bufferedInputStream;
    private PlayerFunctions playerFunctions;
    private boolean stopped = true;
    public int i = 0;
    private Random random = new Random();

    public Audio() {
        super();
        playerFunctions = new PlayerFunctions(this);
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
            playerFunctions.Stop();
            stopped = true;
            isPlaying = false;
            playPauseButton.setText("PLAY");
        } else if (e.getSource() == playPauseButton) {
            if (!isPlaying && stopped) {
                if (fileList != null) {
                    playerFunctions.Play(fileList.get(i).getAbsolutePath(), -1);
                    isPlaying = true;
                    stopped = false;
                    playPauseButton.setText("PAUSE");
                }
            } else if (isPlaying && !stopped) {
                playerFunctions.Pause();
                isPlaying = false;
                playPauseButton.setText("PLAY");
            } else if (!isPlaying) {

                isPlaying = true;
                playPauseButton.setText("PAUSE");
                playerFunctions.Resume();
            }
        } else if (e.getSource() == nextButton) {
            if (!shuffleChooser.isSelected()) {
                i++;
                if (i == fileList.size()) {
                    playerFunctions.Stop();
                    i = 0;
                    playOther(i);
                } else if (i < fileList.size()) {
                    playerFunctions.Stop();
                    playOther(i);
                } else
                    i = 0;
            } else {
                i = randomNumber(i, fileList.size());
                playerFunctions.Stop();
                playOther(i);
            }
        } else if (e.getSource() == previousButton) {
            if (!shuffleChooser.isSelected()) {
                i--;
                if (i < 0) {
                    i = fileList.size() - 1;
                    playerFunctions.Stop();
                    playOther(i);
                } else {
                    playerFunctions.Stop();
                    playOther(i);
                }
            } else {
                i = randomNumber(i, fileList.size());
                playerFunctions.Stop();
                playOther(i);
            }
        }
    }

   // public static void main(String[] args) {
   //     Audio audio = new Audio();
   //     audio.makeGUI();
   // }

    private void playOther(int trackNumber) {
        playerFunctions.Play(fileList.get(trackNumber).getAbsolutePath(), -1);
        songTitle.setText(fileList.get(trackNumber).getName());
    }

    private int randomNumber(int currentNumber, int n) {

        int number = random.nextInt(n);
        return number;
    }

}
