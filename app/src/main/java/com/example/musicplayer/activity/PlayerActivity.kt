package com.example.musicplayer.activity

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.example.musicplayer.MyMediaPlayer
import com.example.musicplayer.R
import com.example.musicplayer.model.AudioModel
import java.util.concurrent.TimeUnit

class PlayerActivity : AppCompatActivity() {

    lateinit var pauseBtn: ImageView
    lateinit var mediaPlayer: MediaPlayer
    lateinit var title: TextView
    lateinit var artistName:TextView
    lateinit var seekBar: SeekBar
    lateinit var currentTime: TextView
    lateinit var totalTime: TextView
    lateinit var nextBtn: ImageView
    lateinit var prevBtn: ImageView

    lateinit var songList: ArrayList<AudioModel>
    lateinit var currentSong: AudioModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        init()
        title.isSelected = true

        songList = intent.extras?.get("list") as ArrayList<AudioModel>
        mediaPlayer = MyMediaPlayer.getInstance()


        setResourcesWithMusic()

        pauseBtn.setOnClickListener { pauseSong() }
        nextBtn.setOnClickListener { nextSong() }
        prevBtn.setOnClickListener { prevSong() }

        playSong()


        this@PlayerActivity.runOnUiThread(object :Runnable {
            override fun run() {
                if (mediaPlayer!=null){
                    seekBar.progress =mediaPlayer.currentPosition
                    currentTime.text = convertToMMSS(mediaPlayer.currentPosition.toString())

                    if (mediaPlayer.isPlaying){
                        pauseBtn.setImageResource(R.drawable.pause)
                    }else{
                        pauseBtn.setImageResource(R.drawable.play)
                    }
                    Handler().postDelayed(this,100)

                }
            }

        })
        mediaPlayer.setOnCompletionListener {
            try {
                nextSong()
            }catch (e:java.lang.Exception){
                e.printStackTrace()
            }
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                if (mediaPlayer!=null && p2){
                    mediaPlayer.seekTo(progress)
                    mediaPlayer.start()
                }

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    private fun init() {
        pauseBtn = findViewById(R.id.pauseBtn)
        title = findViewById(R.id.display_title)
        seekBar = findViewById(R.id.seekBar)
        currentTime = findViewById(R.id.current_time)
        totalTime = findViewById(R.id.total_time)
        nextBtn = findViewById(R.id.nextBtn)
        prevBtn = findViewById(R.id.prevBtn)
        artistName = findViewById(R.id.artistTxt)
    }

    private fun setResourcesWithMusic() {
        currentSong = songList[MyMediaPlayer.currentIndex]
        title.text = currentSong.title
        artistName.text = currentSong.artist
        totalTime.text = convertToMMSS(currentSong.duration)

    }

    fun convertToMMSS(duration: String): String {
        val millis = duration.toLong()
        return String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1))
    }

    private fun playSong() {
          try {
              mediaPlayer.reset()
              mediaPlayer.setDataSource(currentSong.path)
              mediaPlayer.prepare()
              mediaPlayer.start()
              seekBar.progress = 0
              seekBar.max = mediaPlayer.duration
          }catch (e:Exception){
              e.printStackTrace()
          }
    }

    private fun nextSong() {
        if (MyMediaPlayer.currentIndex == songList.size - 1) {
            return
        }
        MyMediaPlayer.currentIndex += 1
        mediaPlayer.reset()
        setResourcesWithMusic()
        playSong()
    }
        private fun prevSong() {
            if (MyMediaPlayer.currentIndex == 0) {
                return
            }
            MyMediaPlayer.currentIndex -= 1
            mediaPlayer.reset()
            setResourcesWithMusic()
            playSong()
        }

        private fun pauseSong() {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            } else {
                mediaPlayer.start()
            }

        }
    }

