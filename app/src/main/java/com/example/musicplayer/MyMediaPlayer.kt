package com.example.musicplayer

import android.media.MediaPlayer

 class MyMediaPlayer {
    companion object{
        private var instance: MediaPlayer? = null

        fun getInstance():MediaPlayer{
            if (instance==null){
                instance = MediaPlayer()
            }
            return instance as MediaPlayer
        }
        var currentIndex = -1
    }
}