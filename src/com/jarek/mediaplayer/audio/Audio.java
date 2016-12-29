package com.jarek.mediaplayer.audio;

import com.jarek.mediaplayer.api.GUI;
import com.jarek.mediaplayer.api.PlayerFunctions;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

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

    private Audio() {
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
        }
        else if (e.getSource() == playPauseButton) {
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
        }
        else if(e.getSource() == nextButton) {
            i++;
                if(i == fileList.size()) {
                    playerFunctions.Stop();
                    i = 0;
                    playerFunctions.Play(fileList.get(i).getAbsolutePath(), -1);
                    songTitle.setText(fileList.get(i).getName());
                }
                else if(i < fileList.size()){
                    playerFunctions.Stop();
                    playerFunctions.Play(fileList.get(i).getAbsolutePath(), -1);
                    songTitle.setText(fileList.get(i).getName());
                }
                else
                    i = 0;
        }
        else if(e.getSource() == previousButton) {
            i--;
                if(i < 0) {
                    i = fileList.size() - 1;
                    playerFunctions.Stop();
                    playerFunctions.Play(fileList.get(i).getAbsolutePath(), -1);
                    songTitle.setText(fileList.get(i).getName());
                }
                else {
                    playerFunctions.Stop();
                    playerFunctions.Play(fileList.get(i).getAbsolutePath(), -1);
                    songTitle.setText(fileList.get(i).getName());
                }
        }
    }

    public static void main(String[] args) {
        Audio audio = new Audio();
        audio.makeGUI();
    }
}
