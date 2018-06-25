package com.lingsatuo.thieves

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.getqqmusic.RunOnUiThread
import com.lingsatuo.service.MusicService
import java.text.DecimalFormat
import java.util.*

object Controller {
    init {
        bandProgress()
    }

    private var error = 0
    private var errortime = 0L

    var list = ArrayList<MusicItem>()
    private val df = DecimalFormat("####00")
    var index = 0
    private val statelistener = ArrayList<(Type) -> Unit>()
    private val bufferingUpdateListener = ArrayList<(MediaPlayer, Int) -> Unit>()
    private val bandProgress = ArrayList<(Int, Int, String, String) -> Unit>()
    val listener: (Type) -> Unit = { type ->
        run {
            when (type) {
                Controller.Type.PLAY -> {
                    if (!mediaPlayer.isPlaying) {
                        if (MusicService.instance?.path != null) {
                            MusicService.instance?.start()
                            for (indexw in 0 until statelistener.size) {
                                if (indexw < statelistener.size) {
                                    statelistener[indexw].invoke(Type.PLAY)
                                }
                            }
                        }
                    }
                }
                Type.PAUSE -> {
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.pause()
                        for (indexw in 0 until statelistener.size) {
                            if (indexw < statelistener.size) {
                                statelistener[indexw].invoke(Type.PAUSE)
                            }
                        }
                    }
                }
                Controller.Type.LAST -> {
                    index--
                    if (index < 0) index = list.size - 1
                    if (list.isEmpty()) return@run
                    MusicService.instance?.start(list[index])
                    for (indexw in 0 until statelistener.size) {
                        if (indexw < statelistener.size) {
                            statelistener[indexw].invoke(Type.LAST)
                        }
                    }
                }
                Controller.Type.NEXT -> {
                    index++
                    if (index >= list.size) index = 0
                    if (list.isEmpty()) return@run
                    MusicService.instance?.start(list[index])
                    for (indexw in 0 until statelistener.size) {
                        if (indexw < statelistener.size) {
                            statelistener[indexw].invoke(Type.NEXT)
                        }
                    }
                }
            }
        }
    }
    private var mediaPlayer: MediaPlayer = MediaPlayer()

    private fun canPlay(): Boolean {
        if (System.currentTimeMillis() - errortime <= 100) {
            error++
            errortime = System.currentTimeMillis()
        } else {
            error = 0
        }
        if (error > 2) {
            return false
        }
        return true
    }

    fun addStateChangeListener(listener: (Type) -> Unit) {
        if (!statelistener.contains(listener)) {
            statelistener.add(listener)
        }
    }

    fun removeStateListener(listener: (Type) -> Unit) {
        if (statelistener.contains(listener)) {
            statelistener.remove(listener)
        }
    }

    fun addBufferingUpdateListener(listener: (MediaPlayer, Int) -> Unit) {
        if (!bufferingUpdateListener.contains(listener)) {
            bufferingUpdateListener.add(listener)
        }
    }

    fun clear() {
        this.list.clear()
        this.bandProgress.clear()
        this.statelistener.clear()
        this.bufferingUpdateListener.clear()
    }

    fun removeBufferingUpdateListener(listener: (MediaPlayer, Int) -> Unit) {
        if (bufferingUpdateListener.contains(listener)) {
            bufferingUpdateListener.remove(listener)
        }
    }

    fun setMediaPlayer(mediaPlayer: MediaPlayer) {
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        this.mediaPlayer = mediaPlayer
        mediaPlayer.setOnCompletionListener {
            if (canPlay()) {
                listener.invoke(Controller.Type.NEXT)
            }
        }
        mediaPlayer.setOnErrorListener(null)
        mediaPlayer.setOnBufferingUpdateListener { mp, percent ->
            if (percent < 20) return@setOnBufferingUpdateListener
            for (index in 0 until bufferingUpdateListener.size) {
                RunOnUiThread {
                    if (index < bufferingUpdateListener.size) {
                        bufferingUpdateListener[index].invoke(mp, percent)
                    }
                }
            }
        }
    }

    fun bandProgress(band: (Int, Int, String, String) -> Unit) {
        if (!bandProgress.contains(band))
            bandProgress.add(band)
    }

    fun removeBandProgress(band: (Int, Int, String, String) -> Unit) {
        if (bandProgress.contains(band))
            bandProgress.remove(band)
    }

    private fun bandProgress() {
        val timer = Timer()
        val task: TimerTask
        val handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                if (MusicService.player.isPlaying) {
                    try {
                        val t = MusicService.player.currentPosition
                        val l = MusicService.player.duration
                        val current = df.format(t / 60000) + " : " + df.format((t - (t / 60000 * 60000)) / 1000f)
                        val r = l - t
                        val m = (r / 1000 / 60)
                        val time = "- ${df.format(m)} : ${df.format((r - (m * 1000 * 60)) / 1000f)}"
                        for (a in 0 until bandProgress.size) {
                            if (a >= bandProgress.size) break
                            bandProgress[a].invoke(t, l, current, time)
                        }
                    } catch (e: Throwable) {
                    }
                }
                super.handleMessage(msg)
            }
        }
        task = object : TimerTask() {
            override fun run() {
                val message = Message()
                message.what = 1
                handler.sendMessage(message)
            }
        }
        timer.schedule(task, 500, 500)
    }


    enum class Type {
        PLAY, NEXT, LAST, PAUSE
    }
}