package com.lingsatuo.thieves

import android.Manifest
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Process
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.lingsatuo.app.BaseActivity
import com.lingsatuo.getqqmusic.GetItemInfo
import com.lingsatuo.service.MusicService
import com.lingsatuo.songscan.SongScan
import com.lingsatuo.widget.MusicProgressBarH
import com.lingsatuo.widget.XTextView
import com.tbruyelle.rxpermissions2.RxPermissions


class MainActivity : BaseActivity() {
    private val listener: (Controller.Type) -> Unit = { type ->
        val item = MusicService.instance?.item
        if (item != null) {
            setTitle(item.title)
            GetItemInfo(item, { e ->
                setAlbum(item.albumicon)
            }).start()
        }
        if (type == Controller.Type.PAUSE) {
            findViewById<ImageView>(R.id.play_pause).setImageResource(R.mipmap.toplay)
        } else {
            findViewById<ImageView>(R.id.play_pause).setImageResource(R.mipmap.topause)
        }
    }

    private val mediaplayerbufferingupdate: (MediaPlayer, Int) -> Unit= {m,i->
        findViewById<MusicProgressBarH>(R.id.card_progress)?.setSecondaryProgress((i/100f*m.duration).toInt())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (MusicService.instance == null) {
            val intent = Intent(this, MusicService::class.java)
            startService(intent)
        }
        setActivity(this)
        setSupportActionBar(findViewById(R.id.toolbar))
        setRootView(findViewById(R.id.root))
        findViewById<XTextView>(R.id.title).setText(R.string.app_name)
        setDrawLayout()
        initView()
        addListener()
        AlertDialog.Builder(this)
                .setTitle("开发版预览")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("所呈现的功能，界面不代表最终样式，并可能存在大量BUG，仅供测试\n\n请在24小时内删除本程序，并删除使用本程序所下载的媒体文件\n\n如遇到崩溃，请及时反馈给QQ:1617685714，TKS！")
                .setPositiveButton("确定",null)
                .setNeutralButton("取消",{_,_->
                    Process.killProcess(Process.myPid())
                })
                .setCancelable(false)
                .show()
                .setCanceledOnTouchOutside(false)
    }

    private fun setTitle(str: String) {
        findViewById<XTextView>(R.id.title).text = str
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            Controller.removeStateListener(listener)
            Controller.removeBufferingUpdateListener(mediaplayerbufferingupdate)
            super.onBackPressed()
        }
    }

    private fun setDrawLayout() {
        val wm = this.windowManager
        val width = wm.defaultDisplay.width
        val root = findViewById<CardView>(R.id.main_draw_root)
        root.layoutParams.width = (width * 0.85f).toInt()
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()
    }

    private fun initView() {

        val cardview = findViewById<CardView>(R.id.main_card_play_root)
        cardview.layoutParams.width = (windowManager.defaultDisplay.width * 0.7f).toInt()
        cardview.layoutParams.height = cardview.layoutParams.width
        findViewById<ImageView>(R.id.main_play_card_icon).setOnClickListener {
            val intent = Intent(this, PlayerActivity::class.java)
            startActivity(intent)
        }
        Controller.addStateChangeListener(listener)
        Controller.addBufferingUpdateListener(mediaplayerbufferingupdate)
        GetPlaylist(this).getList()


        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.READ_PHONE_STATE)
                .subscribe({get->
                    if (get){
                        val songscan = SongScan.scan(this).getAlbum()
                        for(item in songscan.entries){
                            println(item.key+"          "+item.value)
                        }
                    }
                })
    }


    fun setAlbum(path: String) {
        val img = findViewById<ImageView>(R.id.main_play_card_icon)
        if (img != null) {
            Glide.with(this)
                    .load(path)
                    .asBitmap()
                    .placeholder(R.mipmap.back2)
                    .error(R.mipmap.back2)
                    .priority(Priority.HIGH)
                    .into(img)
        }
    }

    private fun addListener() {
        findViewById<ImageView>(R.id.play_pause).setOnClickListener {
            Controller.listener.invoke(if (MusicService.player.isPlaying) Controller.Type.PAUSE else Controller.Type.PLAY)
        }
        findViewById<ImageView>(R.id.last).setOnClickListener {
            Controller.listener.invoke(Controller.Type.LAST)
        }
        findViewById<ImageView>(R.id.next).setOnClickListener {
            Controller.listener.invoke(Controller.Type.NEXT)
        }
        findViewById<ImageView>(R.id.main_search).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    fun setActivity(mainActivity: MainActivity) {
        val mpbh = mainActivity.findViewById<MusicProgressBarH>(R.id.card_progress)
        mpbh.setMediaPlayer(MusicService.player)
        mpbh.setSecondaryProgress(MusicService.player.duration / 3)
        Controller.setMediaPlayer(MusicService.player)
    }
}
