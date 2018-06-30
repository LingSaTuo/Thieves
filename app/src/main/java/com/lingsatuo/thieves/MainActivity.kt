package com.lingsatuo.thieves

import android.Manifest
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.lingsatuo.adapter.MPlaylistAdapter
import com.lingsatuo.adapter.UserRvAdapter
import com.lingsatuo.app.BaseActivity
import com.lingsatuo.getqqmusic.FindUserList
import com.lingsatuo.getqqmusic.GetItemInfo
import com.lingsatuo.getqqmusic.RunOnUiThread
import com.lingsatuo.service.MusicService
import com.lingsatuo.utils.AccLoginCenter
import com.lingsatuo.utils.ContinueUtils
import com.lingsatuo.utils.SaveSatate
import com.lingsatuo.widget.MusicProgressBarH
import com.lingsatuo.widget.XTextView
import com.tbruyelle.rxpermissions2.RxPermissions
import de.hdodenhof.circleimageview.CircleImageView


class MainActivity : BaseActivity() {
    private lateinit var drawer: DrawerLayout
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
    private val loginlistener: (UserRvAdapter.User) -> Unit = { user ->
        val adapter = findViewById<RecyclerView>(R.id.myplaylist_rv).adapter as MPlaylistAdapter
        adapter.setData(MainActivityInitView.getLocalGroup())
        if (user.qqnum != "") {
            adapter.addData(user.list)
            if(user.list.size==0)Toast.makeText(this,"私密歌单",Toast.LENGTH_LONG).show()
            drawer.findViewById<XTextView>(R.id.user_name).text = user.name
        } else {
            drawer.findViewById<XTextView>(R.id.user_name).text = "点击设置代理"
        }
        adapter.notifyDataSetChanged()
        if (!isDestroyed)
        Glide.with(this)
                .load(user.usericon)
                .asBitmap()
                .placeholder(R.mipmap.user_icon)
                .error(R.mipmap.user_icon)
                .priority(Priority.HIGH)
                .into(drawer.findViewById<CircleImageView>(R.id.login))
    }

    lateinit var mbph: MusicProgressBarH
    private val bandProgress: (Int, Int, String, String) -> Unit = { c, d, n, r ->
        mbph.setMax(d)
        mbph.setProgress(c)
    }
    private val mediaplayerbufferingupdate: (MediaPlayer, Int) -> Unit = { m, i ->
        if (m.isPlaying)
            findViewById<MusicProgressBarH>(R.id.card_progress)?.setSecondaryProgress((i / 100f * m.duration).toInt())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        mbph = findViewById(R.id.card_progress)
        ContinueUtils.chaeckCanUse(this)
        if (MusicService.instance == null) {
            val intent = Intent(this, MusicService::class.java)
            startService(intent)
        }
        setActivity()
        setSupportActionBar(findViewById(R.id.toolbar))
        setRootView(findViewById(R.id.root))
        findViewById<XTextView>(R.id.title).text = "请下载QQ音乐，支持正版"
        setDrawLayout()
        initView()
        addListener()
        MainActivityInitView(this).initView()
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
            Controller.removeBandProgress(bandProgress)
            AccLoginCenter.removeListener(loginlistener)
            super.onBackPressed()
        }
    }

    private fun setDrawLayout() {
        val wm = this.windowManager
        val width = wm.defaultDisplay.width
        val root = findViewById<CardView>(R.id.main_draw_root)
        root.layoutParams.width = (width * 0.85f).toInt()
        drawer = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()
        drawer.findViewById<LinearLayout>(R.id.clear_cache).setOnClickListener {
            Thread({
                Glide.get(this).clearDiskCache()
                RunOnUiThread {
                    Glide.get(this).clearMemory()
                    Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
                }
            }).start()
        }
        drawer.findViewById<CircleImageView>(R.id.login).setOnClickListener { v ->
            startActivity(Intent(this, SelectionUser::class.java))
            drawer.closeDrawers()
        }
        drawer.findViewById<LinearLayout>(R.id.about).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
        drawer.findViewById<LinearLayout>(R.id.join_us).setOnClickListener {
            val uri = Uri.parse("https://jq.qq.com/?_wv=1027&k=5xUSCP4")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
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
        AccLoginCenter.addOnLoginListener(loginlistener)
        GetPlaylist(this).getList()
        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_WIFI_STATE)
                .subscribe({ get ->
                    if (get) {
                        login()
                    }
                })
    }

    private fun login() {
        val user = SaveSatate.readUserInfo(this)
        if (user.qqnum != "") {
            FindUserList(user, { list ->
                if (list.size==1)
                AccLoginCenter.invoke(list[0])
            }).start()
        }
    }

    private fun setAlbum(path: String) {
        val img = findViewById<ImageView>(R.id.main_play_card_icon)
        if (img != null&&!isDestroyed) {
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

    private fun setActivity() {
        Controller.setMediaPlayer(MusicService.player)
        MusicService.instance?.listeners?.clear()
        Controller.clear()
        Controller.bandProgress(bandProgress)
    }
}
