package com.lingsatuo.thieves

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.lingsatuo.adapter.MPlaylistActivityRvAdapter
import com.lingsatuo.app.BaseActivity
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.service.MusicService
import com.lingsatuo.songscan.Song2MusicItem
import com.lingsatuo.songscan.SongScan
import com.lingsatuo.widget.XTextView

class LocalActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.local_activity)
        setRootView(findViewById(R.id.root))
        setSupportActionBar(findViewById(R.id.toolbar))
        val adapter = MPlaylistActivityRvAdapter(this)
        val rv = findViewById<RecyclerView>(R.id.local_list)
        findViewById<XTextView>(R.id.title).text = "本地歌曲"
        rv.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        rv.adapter = adapter
        val basedata = SongScan.scan(this).getAlbum()
        val list = ArrayList<MusicItem>()
        for (l in basedata.values){
            for (song in l){
                list.add(Song2MusicItem(song).turn())
            }
        }
        adapter.setData(list)
        adapter.setOnItemClickListener { i, view ->
            val item = adapter.getItem(i)
            if (item.singmid == MusicService.instance?.item?.singmid) {
                val intent = Intent(this, PlayerActivity::class.java)
                this.startActivity(intent)
            } else {
                Controller.list.clear()
                Controller.list.addAll(list)
                Controller.index = i-1
                MusicService.instance?.start(adapter.getItem(i))
            }
            adapter.notifyDataSetChanged()
        }
        adapter.notifyDataSetChanged()
    }
}