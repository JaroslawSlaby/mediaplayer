package com.jarek.mediaplayer.api;


import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by jarek on 12/15/16.
 *
 */
public class GUI extends JFrame implements ActionListener{
    //GUI
    private JPanel controlPanel = new JPanel();
    private JMenuBar mainMenu = new JMenuBar();
    private JMenu file = new JMenu("File");
    private JMenuItem openFile = new JMenuItem("Open file");
    private JMenuItem openFolder = new JMenuItem("Open folder");
    protected JButton playPauseButton = new JButton("PLAY");
    protected JButton stopButton = new JButton("STOP");
    protected JButton nextButton = new JButton("NEXT");
    protected JButton previousButton = new JButton("BACK");
    protected JTextField songTitle = new JTextField("sample");
    protected JRadioButton shuffleChooser = new JRadioButton("Shuffle");
    protected JProgressBar progressSong = new JProgressBar();
    protected AdvancedPlayer player;
    protected ArrayList<File> fileList = new ArrayList<>();
    protected boolean isPlaying = false;
    private int i = 0;
    //private PlayerFunctions playerFunctions = new PlayerFunctions();

    public void makeGUI(){

        songTitle.setEditable(false);
        //action_listeners here
        openFile.addActionListener(this);
        openFolder.addActionListener(this);
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
        file.add(openFolder);
        mainMenu.add(file);
        //shuffleChooser.
        container.add(mainMenu, BorderLayout.NORTH);
        container.add(songTitle, BorderLayout.EAST);
        container.add(controlPanel, BorderLayout.SOUTH);
        container.add(progressSong, BorderLayout.WEST);
        container.add(shuffleChooser, BorderLayout.WEST);
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
            int choose = fileChooser.showOpenDialog(this);
            if(choose == JFileChooser.APPROVE_OPTION) {
                fileList.add(i, fileChooser.getSelectedFile());
                    if(isPlaying) {
                        player.close();
                        isPlaying = false;
                        playPauseButton.setText("PLAY");
                    }
                if (!fileList.isEmpty()) {
                    songTitle.setText(fileList.get(0).getName());
                }
                else
                    songTitle.setText("No file selected!");
            }
            else
                songTitle.setText("Cancelled by user!");
        }
        else if(e.getSource() == openFolder) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int choose = fileChooser.showOpenDialog(this);
            if(choose == JFileChooser.APPROVE_OPTION)
            {
                File folder = fileChooser.getSelectedFile();
                File[] files = folder.listFiles();
                assert files != null;
                for (File file1 : files) {
                    if (file1.isFile()) {
                        fileList.add(i, file1);
                        i++;
                    }
                }
                songTitle.setText(fileList.get(0).getName());
//                for(int z = 0; z < fileList.size(); z++)
//                    System.out.println(z + ": " + fileList.get(z).getName().toString());
            }
        }
    }
}
