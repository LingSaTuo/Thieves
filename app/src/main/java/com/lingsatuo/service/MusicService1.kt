package com.lingsatuo.service

import android.app.*
import android.os.*
import android.content.*
import android.media.*
import android.util.Log
import android.widget.Toast
import com.lingsatuo.getqqmusic.GetMusicAbsPath
import com.lingsatuo.getqqmusic.GetMusicFileName
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.getqqmusic.RunOnUiThread
import com.lingsatuo.thieves.Controller
import com.lingsatuo.utils.MNotificationManager
import com.lingsatuo.utils.NetWork
import java.util.ArrayList

class MusicService1 : Service() {
    val listeners = ArrayList<(MusicItem) -> Unit>()
    private var am: AudioManager? = null
    var item = MusicItem()
    private var playing = false
    private var canplay = true
    var path: String? = null

    companion object {
        var instance: MusicService1? = null
        val player: MediaPlayer = MediaPlayer()
    }



    private val bufferingupdate :(MediaPlayer,Int)->Unit={m,p->
        if (p>10) {
            playing = true
            canplay = true
        }
        if (p>30&&!m.isPlaying&&playing&&canplay) {
            play()
            canplay = false
        }
    }
    private var afChangeListener: AudioManager.OnAudioFocusChangeListener? = null



    override fun onCreate() {
        super.onCreate()
        instance = this
        am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        addListener()
        MNotificationManager.show(this)
        Controller.addBufferingUpdateListener(bufferingupdate)
    }

    fun addListener(lis: (MusicItem) -> Unit) {
        if (!listeners.contains(lis)) {
            listeners.add(lis)
        }
    }

    fun removeListener(lis: (MusicItem) -> Unit) {
        if (listeners.contains(lis)) {
            listeners.remove(lis)
        }
    }

    fun play(playing: Boolean) {
        play()
    }

    fun play() {
        val play = am!!.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        if (play == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            RunOnUiThread{
                Controller.listener.invoke(Controller.Type.PLAY)
            }
        }
    }

    fun seekTo(duration: Int) {
        if (isReady()) {
            player.seekTo(duration)
        }
    }
    private fun start(path: String) {
        if (playing)return
        this.path = path
        Thread(Runnable {
            player.reset()
            try {
                player.setDataSource(path)
                player.prepare()
                Thread.sleep(500)
                if (item.isloca){
                    bufferingupdate.invoke(player,100)
                }
            } catch (e: Throwable) {
                playing = false
                e.printStackTrace()
            }
        }).start()
    }

    fun start() {
        player.start()
    }
    fun start(item: MusicItem) {
        playing = false
        if (!NetWork().avalibe(applicationContext)&&!item.isloca)return
        this.item = item
        GetMusicAbsPath(item, GetMusicFileName.Quality.M4AH, { path ->
            start(path)
            println(path)
            for (lis in 0 until listeners.size) {
                if (lis >= listeners.size) break
                listeners[lis].invoke(item)
            }
        }).start()
    }

    fun isReady(): Boolean {
        return playing
    }

    fun stop() {
        player.stop()
        am!!.abandonAudioFocus(afChangeListener)
    }

    fun pause(never: Boolean) {
        pause()
    }

    fun pause() {
        Controller.listener.invoke(Controller.Type.PAUSE)
        am!!.abandonAudioFocus(afChangeListener)
    }

    override fun onBind(p1: Intent): IBinder? {
        // TODO: Implement this method
        return null
    }

    private fun addListener() {
        afChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                if (player.isPlaying) {
                    pause()
                }
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                if (player == null) {

                } else if (!player.isPlaying) {
                    if (!canplay) return@OnAudioFocusChangeListener
                   // play()
                }
                // Resume playback
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                if (player.isPlaying) {
                    pause()
                }
                //am.abandonAudioFocus(this);
                // Stop playback
            } else if (focusChange == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                if (!player.isPlaying) {
                    if (!canplay) return@OnAudioFocusChangeListener
                   // play()
                }
            } else if (focusChange == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
                if (player.isPlaying) {
                    pause()
                }
            }
        }
    }
}
