package com.jarek.mediaplayer;

import com.jarek.mediaplayer.api.GUI;
import com.jarek.mediaplayer.audio.Audio;


/**
 * Created by jarek on 12/15/16.
 *
 */
public class Main extends GUI {

    public static void main(String[] args) {
        Audio audio = new Audio();
        audio.makeGUI();
    }
}
