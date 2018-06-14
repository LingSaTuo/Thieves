package com.lingsatuo.thieves

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.support.v7.widget.AppCompatSeekBar
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.lingsatuo.donttouchme.are_u_understand.do_not_touch_me_yet.plz_go_back.uh_huh.what_are_u_doing.fucking_deep.are_u_gay.why_dont_u_go_back.I_LOVE_U
import com.lingsatuo.getqqmusic.GetItemInfo
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.service.MusicService
import com.lingsatuo.utils.MusicDownPop
import com.lingsatuo.utils.PlayerActivityShowList
import com.lingsatuo.utils.SharePop
import com.lingsatuo.utils.SongInfoShowPop
import com.lingsatuo.widget.XTextView
import jp.wasabeef.glide.transformations.BlurTransformation
import java.text.DecimalFormat
import java.util.*


class PlayerActivityInitView(private var playerActivity: PlayerActivity) {
    private var seekBar: AppCompatSeekBar? = null
    private var ontouch = false
    private var dont_touch_me:(View)->Unit={v->
        I_LOVE_U.onClick(playerActivity,v)
    }
    private val statelistener: (Controller.Type) -> Unit = { type ->
        val item = MusicService.instance?.item
        if (item != null) {
            playerActivity.findViewById<XTextView>(R.id.title).text = item.title
            playerActivity.findViewById<XTextView>(R.id.subtitle).text = item.getSingers()
        }
        if (type == Controller.Type.PLAY) {
            playerActivity.findViewById<ImageView>(R.id.player_activity_play_pause).setImageResource(R.mipmap.topause)
        } else if (type == Controller.Type.PAUSE) {
            playerActivity.findViewById<ImageView>(R.id.player_activity_play_pause).setImageResource(R.mipmap.toplay)
        } else {
            playerActivity.findViewById<ImageView>(R.id.player_activity_play_pause).setImageResource(R.mipmap.topause)
        }
        setAlbumIcon()
    }

    private val mediaplayerbufferingupdate: (MediaPlayer, Int) -> Unit= { m, i->
        playerActivity.findViewById<AppCompatSeekBar>(R.id.play_activity_seekbar)?.secondaryProgress = (i/100f*m.duration).toInt()
    }
    private val listener: (MusicItem) -> Unit = { item ->
        setAlbumIcon()
    }
    val df = DecimalFormat("####00")
    private val timer = Timer()
    fun initView() {
        playerActivity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        val cardview = playerActivity.findViewById<CardView>(R.id.play_card_root)
        cardview.layoutParams.width = (playerActivity.windowManager.defaultDisplay.width * 0.8f).toInt()
        cardview.layoutParams.height = cardview.layoutParams.width
        val add_like_more = playerActivity.findViewById<LinearLayout>(R.id.add_like_more)
        add_like_more.layoutParams.width = cardview.layoutParams.width
        setAlbumIcon()
        seekBar = playerActivity.findViewById(R.id.play_activity_seekbar)
        seekBar!!.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                ontouch = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                ontouch = false
                MusicService.player.seekTo(seekBar?.progress?:0)
            }

        })
        bandProgress()
        addListener()
        MusicService.instance?.addListener(listener)
        if (MusicService.player.isPlaying) {
            playerActivity.findViewById<ImageView>(R.id.player_activity_play_pause).setImageResource(R.mipmap.topause)
        }
        Controller.addStateChangeListener(statelistener)
        Controller.addBufferingUpdateListener(mediaplayerbufferingupdate)
    }

    private fun setAlbumIcon() {
        if (MusicService.instance?.item != null) {
            val item = MusicService.instance?.item
            if (item != null) {
                playerActivity.findViewById<XTextView>(R.id.title).text = item.title
                GetItemInfo(item, { e ->
                    run {
                        if (e == null) {
                            setIcon(item)
                        } else {
                            e.printStackTrace()
                        }
                    }
                }).start()
            }
        }
    }

    private fun setIcon(item: MusicItem) {
        Glide.with(playerActivity)
                .load(item.albumicon)
                .asBitmap()
                .placeholder(R.mipmap.back2)
                .error(R.mipmap.back2)
                .priority(Priority.HIGH)
                .into(playerActivity.findViewById(R.id.player_activity_album_icon))
        Glide.with(playerActivity)
                .load(item.albumicon)
                .bitmapTransform(BlurTransformation(playerActivity, 25), CenterCrop(playerActivity))
                .priority(Priority.HIGH)
                .into(playerActivity.findViewById(R.id.player_activity_background_image))
    }
    private fun bandProgress() {
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
                        playerActivity.findViewById<XTextView>(R.id.play_activity_seek_current).text = current
                        playerActivity.findViewById<XTextView>(R.id.play_activity_seek_duration).text = time
                        if (!ontouch){
                            seekBar?.max = l
                            try {
                                seekBar?.setProgress(t, true)
                            }catch (e:Throwable){
                                seekBar?.progress = t
                            }
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

    fun close() {
        timer.cancel()
        Controller.removeStateListener(statelistener)
        MusicService.instance?.removeListener(listener)
        Controller.removeBufferingUpdateListener(mediaplayerbufferingupdate)
    }

    private fun addListener() {
        val download = playerActivity.findViewById<ImageView>(R.id.player_activity_download)
        download.setOnClickListener {
            if (MusicService.instance?.item != null) {
                if (MusicService.instance?.item!!.title != "")
                    MusicDownPop(playerActivity, MusicService.instance?.item!!).show(playerActivity.findViewById<CardView>(R.id.play_card_root))
            }
        }
        playerActivity.findViewById<ImageView>(R.id.player_activity_play_pause).setOnClickListener {
            Controller.listener.invoke(if (MusicService.player.isPlaying) Controller.Type.PAUSE else Controller.Type.PLAY)
        }
        playerActivity.findViewById<ImageView>(R.id.player_activity_last).setOnClickListener {
            Controller.listener.invoke(Controller.Type.LAST)
        }
        playerActivity.findViewById<ImageView>(R.id.player_activity_next).setOnClickListener {
            Controller.listener.invoke(Controller.Type.NEXT)
        }
        playerActivity.findViewById<ImageView>(R.id.player_activity_show_list).setOnClickListener {
            PlayerActivityShowList(playerActivity).show(playerActivity.findViewById<CardView>(R.id.play_card_root))
        }
        playerActivity.findViewById<ImageView>(R.id.play_activity_more).setOnClickListener{
            val item = MusicService.instance?.item
            if (item?.title != "")
            SongInfoShowPop(playerActivity,item!!).show(playerActivity.findViewById<CardView>(R.id.play_card_root))
        }
        playerActivity.findViewById<ImageView>(R.id.play_activity_share).setOnClickListener{v->
            SharePop(playerActivity,v).show()
        }
        dont_touch_me()
    }


























    private fun dont_touch_me(){
        playerActivity.findViewById<ImageView>(R.id.play_activity_add).setOnClickListener(dont_touch_me)
        playerActivity.findViewById<ImageView>(R.id.play_activity_menu).setOnClickListener(dont_touch_me)
        playerActivity.findViewById<ImageView>(R.id.play_activity_like).setOnClickListener(dont_touch_me)
        playerActivity.findViewById<ImageView>(R.id.play_activity_loop).setOnClickListener(dont_touch_me)
    }
}