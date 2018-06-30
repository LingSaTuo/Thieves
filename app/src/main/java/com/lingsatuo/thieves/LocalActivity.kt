package com.lingsatuo.thieves

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.lingsatuo.adapter.MPlaylistActivityRvAdapter
import com.lingsatuo.app.BaseActivity
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.getqqmusic.RunOnUiThread
import com.lingsatuo.service.MusicService
import com.lingsatuo.songscan.Song2MusicItem
import com.lingsatuo.songscan.SongScan
import com.lingsatuo.widget.XTextView
import kotlinx.android.synthetic.main.local_activity.*

class LocalActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.local_activity)
        setRootView(findViewById(R.id.root))
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<XTextView>(R.id.title).text = "本地歌曲"
        val adapter = MPlaylistActivityRvAdapter(this@LocalActivity)
        local_list.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        local_list.adapter = adapter
        Thread({
            loading()
        }).start()

    }

    private fun loading() {
        val basedata = SongScan.scan(this@LocalActivity).getAlbum()
        val list = ArrayList<MusicItem>()
        for (l in basedata.values) {
            for (song in l) {
                list.add(Song2MusicItem(song).turn())
            }
        }
        (local_list.adapter as MPlaylistActivityRvAdapter).setData(list)
        (local_list.adapter as MPlaylistActivityRvAdapter).setOnItemClickListener { i, view ->
            val item = (local_list.adapter as MPlaylistActivityRvAdapter).getItem(i)
            if (item.singmid == MusicService.instance?.item?.singmid) {
                RunOnUiThread{
                    val intent = Intent(this, PlayerActivity::class.java)
                    this.startActivity(intent)
                }
            } else {
                Controller.list.clear()
                Controller.list.addAll(list)
                Controller.index = i - 1
                MusicService.instance?.start((local_list.adapter as MPlaylistActivityRvAdapter).getItem(i))
            }
            RunOnUiThread{
                (local_list.adapter as MPlaylistActivityRvAdapter).notifyDataSetChanged()
            }
        }
        RunOnUiThread{
            local_list.visibility = View.VISIBLE
            loading_local.visibility = View.GONE
            (local_list.adapter as MPlaylistActivityRvAdapter).notifyDataSetChanged()
        }
    }
}