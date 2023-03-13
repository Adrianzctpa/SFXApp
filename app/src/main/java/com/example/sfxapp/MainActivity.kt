package com.example.sfxapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var seekBar: SeekBar
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBar = findViewById(R.id.sbPlay)
        handler = Handler(Looper.getMainLooper())
        val pauseBtn = findViewById<FloatingActionButton>(R.id.fabPause)
        val playBtn = findViewById<FloatingActionButton>(R.id.fabPlay)
        val stopBtn = findViewById<FloatingActionButton>(R.id.fabStop)

        pauseBtn.setOnClickListener {
            mediaPlayer?.pause()
        }

        playBtn.setOnClickListener {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.cheer)
                initSeekBar()
            }

            mediaPlayer?.start()
        }

        stopBtn.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(runnable)
            seekBar.progress = 0
        }
    }

    private fun initSeekBar() {
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(sb: SeekBar?) {
                TODO("Not yet implemented")
            }

        })

        val tvPlay = findViewById<TextView>(R.id.tvPlayed)
        val tvDue = findViewById<TextView>(R.id.tvDue)

        seekBar.max = mediaPlayer!!.duration
        runnable = Runnable {
            seekBar.progress = mediaPlayer!!.currentPosition

            val played = mediaPlayer!!.currentPosition / 1000
            tvPlay.text = "$played sec"

            val duration = mediaPlayer!!.duration / 1000
            val dueTime = duration - played
            tvDue.text = "$dueTime sec"

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }
}