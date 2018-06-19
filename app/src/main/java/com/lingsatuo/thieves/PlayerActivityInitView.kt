package com.lingsatuo.thieves

import android.media.MediaPlayer
import android.support.v7.widget.*
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.lingsatuo.adapter.PlayActivityRvAdapter
import com.lingsatuo.donttouchme.are_u_understand.do_not_touch_me_yet.plz_go_back.uh_huh.what_are_u_doing.fucking_deep.are_u_gay.why_dont_u_go_back.I_LOVE_U
import com.lingsatuo.getqqmusic.GetItemInfo
import com.lingsatuo.getqqmusic.GetLrcQQMusic
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.service.MusicService
import com.lingsatuo.utils.*
import com.lingsatuo.view.KoolLrcView
import com.lingsatuo.widget.MusicSeekBar
import com.lingsatuo.widget.XTextView
import jp.wasabeef.glide.transformations.BlurTransformation


class PlayerActivityInitView(private var playerActivity: PlayerActivity) {
    private var seekBar: MusicSeekBar? = null
    private var ontouch = false
    private var dont_touch_me: (View) -> Unit = { v ->
        I_LOVE_U.onClick(playerActivity, v)
    }

    private val bandprogress: (Int, Int, String, String) -> Unit = { c, d, n, r ->
        seekBar?.setMax(d)
//        playerActivity.findViewById<KoolLrcView>(R.id.player_header_lrcview).setTimeMillisecond(c)

//        playerActivity.findViewById<XTextView>(R.id.player_header_seekbar).text = n
//        playerActivity.findViewById<XTextView>(R.id.play_activity_seek_duration).text = r
        if (!ontouch) {
            seekBar?.setProgress(c)
        }
    }
    private val statelistener: (Controller.Type) -> Unit = { type ->
        val item = MusicService.instance?.item
        if (item != null) {
            playerActivity.findViewById<XTextView>(R.id.player_header_item_title).text = item.title
            playerActivity.findViewById<XTextView>(R.id.player_header_item_subtitle).text = item.getSingers()
            playerActivity.findViewById<XTextView>(R.id.player_header_title).text = item.title
            playerActivity.findViewById<XTextView>(R.id.player_header_subtitle).text = item.getSingers()
        }
        when (type) {
            Controller.Type.PAUSE -> {
                playerActivity.findViewById<ImageView>(R.id.player_header_item_play_pause).setImageResource(R.mipmap.toplay)
            }
            else -> {
                playerActivity.findViewById<ImageView>(R.id.player_header_item_play_pause).setImageResource(R.mipmap.topause)
//                    GetLrcQQMusic(MusicService.instance?.item!!,{lec,e->
//                        playerActivity.findViewById<KoolLrcView>(R.id.player_header_lrcview).setLrcStr(lec)
//                    }).start()
            }
        }
        (playerActivity.findViewById<RecyclerView>(R.id.player_activity_list).adapter as PlayActivityRvAdapter)
                .notifyDataSetChanged()
        setAlbumIcon()
    }

    private val mediaplayerbufferingupdate: (MediaPlayer, Int) -> Unit = { m, i ->
        playerActivity.findViewById<MusicSeekBar>(R.id.player_header_seekbar)?.setSecondaryProgress((i / 100f * m.duration).toInt())
    }
    private val listener: (MusicItem) -> Unit = { item ->
        setAlbumIcon()
    }

    fun initView() {
        playerActivity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        seekBar = playerActivity.findViewById(R.id.player_header_seekbar)
        setAlbumIcon()
        val adapter = PlayActivityRvAdapter(playerActivity)
        if (Controller.list.size>0){
            playerActivity.findViewById<ImageView>(R.id.player_activity_nodata).visibility = View.GONE
            playerActivity.findViewById<RecyclerView>(R.id.player_activity_list).visibility = View.VISIBLE
        }else{
            Glide.with(playerActivity)
                    .load("http://musicone-1253269015.coscd.myqcloud.com/nodata.jpg")
                    .asBitmap()
                    .placeholder(R.mipmap.donate)
                    .error(R.mipmap.donate)
                    .priority(Priority.HIGH)
                    .into(playerActivity.findViewById(R.id.player_activity_nodata))
        }
        adapter.setData(Controller.list)
        val rv = playerActivity.findViewById<RecyclerView>(R.id.player_activity_list)
        rv.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        rv.adapter = adapter
        adapter.setOnItemClickListener { i, view ->
            when (view.id) {
                R.id.playlist_item_more -> {
                    if (MusicService.instance?.item != null) {
                        if (MusicService.instance?.item!!.title != "")
                            MusicDownPop(playerActivity, adapter.getItem(i)).show(view)
                    }
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

            playerActivity.findViewById<XTextView>(R.id.player_header_item_title).text = if (item?.title == "") "MusicOne" else item?.title
            playerActivity.findViewById<XTextView>(R.id.player_header_item_subtitle).text = if (item?.singer?.size == 0) "King丶ST" else item?.getSingers()
            playerActivity.findViewById<XTextView>(R.id.player_header_title).text = if (item?.title == "") "MusicOne" else item?.title
            playerActivity.findViewById<XTextView>(R.id.player_header_subtitle).text = if (item?.singer?.size == 0) "King丶ST" else item?.getSingers()
            if (item?.isloca==false){
//                GetLrcQQMusic(MusicService.instance?.item!!,{lec,e->
//                    playerActivity.findViewById<KoolLrcView>(R.id.player_header_lrcview).setLrcStr(lec)
//                }).start()
            }
            if (item != null) {
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
//        val download = playerActivity.findViewById<ImageView>(R.id.player_activity_download)
//        download.setOnClickListener {
//            if (MusicService.instance?.item != null) {
//                if (MusicService.instance?.item!!.title != "")
//                    MusicDownPop(playerActivity, MusicService.instance?.item!!).show(playerActivity.findViewById<CardView>(R.id.play_card_root))
//            }
//        }
        playerActivity.findViewById<ImageView>(R.id.player_header_item_play_pause).setOnClickListener {
            Controller.listener.invoke(if (MusicService.player.isPlaying) Controller.Type.PAUSE else Controller.Type.PLAY)
        }
        playerActivity.findViewById<ImageView>(R.id.player_header_item_last).setOnClickListener {
            Controller.listener.invoke(Controller.Type.LAST)
        }
        playerActivity.findViewById<ImageView>(R.id.player_header_item_next).setOnClickListener {
            Controller.listener.invoke(Controller.Type.NEXT)
        }
//        playerActivity.findViewById<ImageView>(R.id.player_activity_show_list).setOnClickListener {
//            PlayerActivityShowList(playerActivity).show(playerActivity.findViewById<CardView>(R.id.play_card_root))
//        }
        playerActivity.findViewById<XTextView>(R.id.player_header_subtitle).setOnClickListener { v->
            val item = MusicService.instance?.item
            if (item?.isloca == true)return@setOnClickListener
            if (item?.title != "")
                SongInfoShowPop(playerActivity, item!!).show(v)
        }
        playerActivity.findViewById<ImageView>(R.id.play_activity_share).setOnClickListener { v ->
            SharePop(playerActivity, v).show()
        }
        playerActivity.findViewById<ImageView>(R.id.player_activity_nodata).setOnClickListener {
            Donate(playerActivity).pay("FKX08426X0QE8C6BQSWE0E")
        }
//        playerActivity.findViewById<ImageView>(R.id.player_activity_album_icon).setOnClickListener {
//            FloatWindow(playerActivity).show()
//        }
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