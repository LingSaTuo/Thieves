package com.lingsatuo.thieves

import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.lingsatuo.app.BaseActivity
import com.lingsatuo.getqqmusic.GetLrcQQMusic
import com.lingsatuo.service.MusicService
import com.lingsatuo.widget.MusicSeekBar
import com.lingsatuo.widget.XTextView
import kotlinx.android.synthetic.main.let_s_sing_a_song_layout.*
import kotlinx.android.synthetic.main.let_s_ss_content.*

class LetUsSingSong : BaseActivity() {
    private val bandprogress: (Int, Int, String, String) -> Unit = { c, d, l, r ->
        let_s_right_lrcview.setCurrTime(c.toLong())
        if (!intouch) {
            let_us_sing_seekbar.setMax(d)
            let_us_sing_seekbar.setProgress(c)
        }
    }
    private var intouch = false
    private val bufferinglistener: (MediaPlayer, Int) -> Unit = { m, i ->
        if (m.isPlaying)
            let_us_sing_seekbar.setSecondaryProgress((i / 100f * m.duration).toInt())
    }
    private val stateChangeListener: (Controller.Type) -> Unit = { type ->
        val item = MusicService.instance?.item
        if (item != null) {
            findViewById<XTextView>(R.id.title).text = item.title
        }
        when (type) {
            Controller.Type.PAUSE -> {
                let_us_item_play_pause.setImageResource(R.mipmap.toplay)
            }
            else -> {
                let_us_item_play_pause.setImageResource(R.mipmap.topause)
                if (type!=Controller.Type.PLAY) {
                    setAblum()
                    GetLrcQQMusic(MusicService.instance?.item!!, { lec, e ->
                        let_s_right_lrcview.setLrc(lec)
                    }).start()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.let_s_sing_a_song_layout)
        setSupportActionBar(toolbar)
        setRootView(root)
        findViewById<XTextView>(R.id.title).text = MusicService.instance?.item?.title ?: "MusicOne"
        setAblum()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val height = if (windowManager.defaultDisplay.height > windowManager.defaultDisplay.width) windowManager.defaultDisplay.width else windowManager.defaultDisplay.height
        let_s_left_icon.layoutParams.height = (height * 0.5).toInt()
        let_s_left_icon.layoutParams.width = let_s_left_icon.layoutParams.height
        let_us_sing_seekbar.setColor(resources.getColor(R.color.subbutton_textColor),
                resources.getColor(R.color.button_textColor),
                resources.getColor(R.color.colorAccent),
                resources.getColor(R.color.colorAccent))
    }

    private fun setAblum() {
        val item = MusicService.instance?.item ?: return
        if (isDestroyed)return
        Glide.with(this)
                .load(item.albumicon)
                .asBitmap()
                .placeholder(R.mipmap.album_d)
                .priority(Priority.HIGH)
                .error(R.mipmap.album_d)
                .into(let_s_left_icon)
        GetLrcQQMusic(item, { lec, e ->
            let_s_right_lrcview.setTextSize(35f)
            let_s_right_lrcview.setLrc(lec)
            let_s_right_lrcview.setColor(resources.getColor(R.color.subbutton_textColor), resources.getColor(R.color.colorAccent))
        }).start()
        Controller.bandProgress(bandprogress)
        Controller.addBufferingUpdateListener(bufferinglistener)
        Controller.addStateChangeListener(stateChangeListener)
        bandclick()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Controller.removeBandProgress(bandprogress)
        Controller.removeBufferingUpdateListener(bufferinglistener)
        Controller.removeStateListener(stateChangeListener)
    }

    private fun bandclick() {
        if (MusicService.player.isPlaying) {
            let_us_item_play_pause.setImageResource(R.mipmap.topause)
        }
        let_us_item_next.setOnClickListener {
            Controller.listener.invoke(Controller.Type.NEXT)
        }
        let_us_item_play_pause.setOnClickListener {
            if (MusicService.player.isPlaying)
                Controller.listener.invoke(Controller.Type.PAUSE)
            else
                Controller.listener.invoke(Controller.Type.PLAY)
        }
        let_us_item_last.setOnClickListener {
            Controller.listener.invoke(Controller.Type.LAST)
        }
        let_us_sing_seekbar.setOnSeekBarChangeListener(object : MusicSeekBar.OnSeekChangeListener{
            override fun Befor(musicSeekBar: MusicSeekBar) {
            }

            override fun ChangIng(musicSeekBar: MusicSeekBar) {
                intouch = true
            }

            override fun Done(musicSeekBar: MusicSeekBar) {
                intouch = false
                MusicService.player.seekTo(musicSeekBar.getProgress())
            }

        })
    }
}