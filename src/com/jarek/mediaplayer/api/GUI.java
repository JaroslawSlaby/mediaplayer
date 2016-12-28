package com.jarek.mediaplayer.api;


import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Created by jarek on 12/15/16.
 */
public class GUI extends JFrame implements ActionListener{
    //GUI
    private JPanel controlPanel = new JPanel();
    private JMenuBar mainMenu = new JMenuBar();
    private JMenu file = new JMenu("File");
    private JMenuItem openFile = new JMenuItem("Open");
    protected JButton playPauseButton = new JButton("PLAY");
    protected JButton stopButton = new JButton("STOP");
    protected JButton nextButton = new JButton("NEXT");
    private JButton previousButton = new JButton("BACK");
    private JTextField songTitle = new JTextField("sample");
    protected JProgressBar progressSong = new JProgressBar();
    protected AdvancedPlayer player;
    protected File newFile;
    protected boolean isPlaying = false;

    protected void makeGUI(){

        songTitle.setEditable(false);
        //action_listeners here
        openFile.addActionListener(this);
        //
        Container container = getContentPane();
        progressSong.setMinimum(0);
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setPreferredSize(new Dimension(400, 100));
        controlPanel.add(previousButton);
        controlPanel.add(playPauseButton);
        controlPanel.add(stopButton);
        controlPanel.add(nextButton);
        file.add(openFile);
        mainMenu.add(file);
        container.add(mainMenu, BorderLayout.NORTH);
        container.add(songTitle, BorderLayout.EAST);
        container.add(controlPanel, BorderLayout.SOUTH);
        container.add(progressSong, BorderLayout.WEST);
        //window listener
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        //continue
        setTitle("JS - Media Player");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

    }
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == openFile) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(this);
            newFile = fileChooser.getSelectedFile();
            if(newFile != null)
                songTitle.setText(newFile.getName());
            else
                songTitle.setText("No file selected!");

        } //

    }

    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.makeGUI();
    }
}
