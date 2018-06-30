package com.lingsatuo.thieves

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.support.v7.app.AlertDialog
import android.support.v7.widget.*
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.lingsatuo.adapter.PlayActivityRvAdapter
import com.lingsatuo.floatwindow.FloatWindow
import com.lingsatuo.getqqmusic.GetItemInfo
import com.lingsatuo.getqqmusic.GetLrcQQMusic
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.getqqmusic.mv.GetMvAbsPath
import com.lingsatuo.service.MusicService
import com.lingsatuo.utils.*
import com.lingsatuo.view.MusicLrcView
import com.lingsatuo.widget.MusicSeekBar
import com.lingsatuo.widget.XTextView
import jp.wasabeef.glide.transformations.BlurTransformation


class PlayerActivityInitView(private var playerActivity: PlayerActivity) {
    private var seekBar: MusicSeekBar? = null
    private var ontouch = false
    private var size = 0;
    private var dont_touch_me: (View) -> Unit = { v ->
       val intent = Intent(playerActivity,LetUsSingSong::class.java)
        playerActivity.startActivity(intent)
    }

    private val bandprogress: (Int, Int, String, String) -> Unit = { c, d, n, r ->
        seekBar?.setMax(d)
        playerActivity.findViewById<MusicLrcView>(R.id.player_header_lrcview).setCurrTime(c.toLong())

//        playerActivity.findViewById<XTextView>(R.id.player_header_seekbar).text = n
//        playerActivity.findViewById<XTextView>(R.id.play_activity_seek_duration).text = r
        if (!ontouch) {
            seekBar?.setProgress(c)
        }
    }
    private val statelistener: (Controller.Type) -> Unit = { type ->
        val item = MusicService.instance?.item
        if (item != null) {
            playerActivity.findViewById<XTextView>(R.id.player_header_title).text = item.title
            playerActivity.findViewById<XTextView>(R.id.player_header_subtitle).text = item.getSingers()
            if (item.mvItem != null)
                playerActivity.findViewById<ImageView>(R.id.play_activity_mv).visibility = View.VISIBLE
            else
                playerActivity.findViewById<ImageView>(R.id.play_activity_mv).visibility = View.GONE
        }
        when (type) {
            Controller.Type.PAUSE -> {
                playerActivity.findViewById<ImageView>(R.id.player_header_item_play_pause).setImageResource(R.mipmap.toplay)
            }
            else -> {
                playerActivity.findViewById<ImageView>(R.id.player_header_item_play_pause).setImageResource(R.mipmap.topause)
                GetLrcQQMusic(MusicService.instance?.item!!, { lec, e ->
                    playerActivity.findViewById<MusicLrcView>(R.id.player_header_lrcview).setLrc(lec)
                }).start()
            }
        }
        (playerActivity.findViewById<RecyclerView>(R.id.player_activity_list).adapter as PlayActivityRvAdapter)
                .notifyDataSetChanged()
        //setAlbumIcon()
    }

    private val mediaplayerbufferingupdate: (MediaPlayer, Int) -> Unit = { m, i ->
        if (m.isPlaying)
            playerActivity.findViewById<MusicSeekBar>(R.id.player_header_seekbar)?.setSecondaryProgress((i / 100f * m.duration).toInt())
    }
    private val listener: (MusicItem) -> Unit = { item ->
        setAlbumIcon()
    }

    fun initView() {
        playerActivity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
       // playerActivity.findViewById<ImageView>(R.id.player_activity_background_image).foreground = ColorDrawable(R.color.player_alpha)
        seekBar = playerActivity.findViewById(R.id.player_header_seekbar)
        setAlbumIcon()
        val adapter = PlayActivityRvAdapter(playerActivity)
        if (Controller.list.size > 0) {
            playerActivity.findViewById<ImageView>(R.id.player_activity_nodata).visibility = View.GONE
            playerActivity.findViewById<RecyclerView>(R.id.player_activity_list).visibility = View.VISIBLE
        } else {
        }
        adapter.setData(Controller.list)
        this.size = Controller.list.size
        val rv = playerActivity.findViewById<RecyclerView>(R.id.player_activity_list)
        rv.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        rv.adapter = adapter
        adapter.setOnItemClickListener { i, view ->
            if (this.size != Controller.list.size) {
                adapter.setData(Controller.list)
                this.size = Controller.list.size
            }
            when (view.id) {
                R.id.playlist_item_more -> {
                    MusicMoreInfoPop(playerActivity,adapter.getItem(i)).show()
                }
                else -> {
                    Controller.index = i - 2
                    Controller.listener.invoke(Controller.Type.NEXT)
                }
            }
            adapter.notifyDataSetChanged()
        }
        Controller.bandProgress(bandprogress)
        addListener()
        MusicService.instance?.addListener(listener)
        if (MusicService.player.isPlaying) {
            playerActivity.findViewById<ImageView>(R.id.player_header_item_play_pause).setImageResource(R.mipmap.topause)
        }
        Controller.addStateChangeListener(statelistener)
        Controller.addBufferingUpdateListener(mediaplayerbufferingupdate)
    }

    private fun setAlbumIcon() {
        if (MusicService.instance?.item != null) {
            val item = MusicService.instance?.item
            playerActivity.findViewById<XTextView>(R.id.player_header_title).text = if (item?.title == "") "MusicOne" else item?.title
            playerActivity.findViewById<XTextView>(R.id.player_header_subtitle).text = if (item?.singer?.size == 0) "King丶ST" else item?.getSingers()
            if (item?.isloca == false) {
                if (item.mvItem != null) {
                    playerActivity.findViewById<ImageView>(R.id.play_activity_mv).visibility = View.VISIBLE
                    playerActivity.findViewById<ImageView>(R.id.play_activity_mv).setOnClickListener { v ->
                        StartMV.start(playerActivity,item)
                    }
                }
            }
            if (item != null) {
                playerActivity.findViewById<ImageView>(R.id.play_activity_clone).visibility = View.VISIBLE
                playerActivity.findViewById<ImageView>(R.id.play_activity_clone).setOnClickListener { v ->
                    if (item.title != "")
                        MusicDownPop(playerActivity, item).showDialog()
                }
                GetItemInfo(item, { e ->
                    run {
                        if (e == null) {
                            setIcon(item)
                        } else {
                            e.printStackTrace()
                        }
                    }
                }).start()
                GetLrcQQMusic(MusicService.instance?.item!!, { lec, e ->
                    playerActivity.findViewById<MusicLrcView>(R.id.player_header_lrcview).setTextSize(30f)
                    playerActivity.findViewById<MusicLrcView>(R.id.player_header_lrcview).setLrc(lec)
                }).start()
            }
        }
    }

    private fun setIcon(item: MusicItem) {
        if (playerActivity.isDestroyed)return
        Glide.with(playerActivity)
                .load(item.albumicon)
                .asBitmap()
                .placeholder(R.mipmap.back2)
                .error(R.mipmap.back2)
                .priority(Priority.HIGH)
                .into(playerActivity.findViewById(R.id.player_header_icon))
        Glide.with(playerActivity)
                .load(item.albumicon)
                .bitmapTransform(BlurTransformation(playerActivity, 25), CenterCrop(playerActivity))
                .priority(Priority.HIGH)
                .into(playerActivity.findViewById(R.id.player_activity_background_image))
    }

    fun close() {
        Controller.removeBandProgress(bandprogress)
        Controller.removeStateListener(statelistener)
        MusicService.instance?.removeListener(listener)
        Controller.removeBufferingUpdateListener(mediaplayerbufferingupdate)
    }

    private fun addListener() {
        playerActivity.findViewById<ImageView>(R.id.player_header_item_play_pause).setOnClickListener {
            Controller.listener.invoke(if (MusicService.player.isPlaying) Controller.Type.PAUSE else Controller.Type.PLAY)
        }
        playerActivity.findViewById<ImageView>(R.id.player_header_item_last).setOnClickListener {
            Controller.listener.invoke(Controller.Type.LAST)
        }
        playerActivity.findViewById<ImageView>(R.id.player_header_item_next).setOnClickListener {
            Controller.listener.invoke(Controller.Type.NEXT)
        }
        playerActivity.findViewById<XTextView>(R.id.player_header_subtitle).setOnClickListener { v ->
            val item = MusicService.instance?.item
            if (item?.isloca == true) return@setOnClickListener
            if (item?.title != "")
                SongInfoShowPop(playerActivity, item!!).show()
        }
        playerActivity.findViewById<ImageView>(R.id.play_activity_share).setOnClickListener { v ->
            SharePop(playerActivity, v).show()
        }
        playerActivity.findViewById<ImageView>(R.id.player_activity_nodata).setOnClickListener {
            //            Toast.makeText(playerActivity,"有人告诉我，我的通宵付出不应该索求回报，我想，我是时候移除捐款了",Toast.LENGTH_LONG).show()
        }
        playerActivity.findViewById<ImageView>(R.id.player_header_icon).setOnClickListener {
            FloatWindow(playerActivity).show()
        }
        dont_touch_me()
    }


    private fun dont_touch_me() {
        playerActivity.findViewById<ImageView>(R.id.play_activity_menu).setOnClickListener(dont_touch_me)
        playerActivity.findViewById<MusicSeekBar>(R.id.player_header_seekbar).setOnSeekBarChangeListener(object : MusicSeekBar.OnSeekChangeListener {
            override fun Befor(musicSeekBar: MusicSeekBar) {
            }

            override fun ChangIng(musicSeekBar: MusicSeekBar) {
                ontouch = true
            }

            override fun Done(musicSeekBar: MusicSeekBar) {
                ontouch = false
                MusicService.player.seekTo(musicSeekBar.getProgress())
            }
        })
    }
}