package com.lingsatuo.thieves

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.lingsatuo.adapter.MPlaylistActivityRvAdapter
import com.lingsatuo.app.BaseActivity
import com.lingsatuo.getqqmusic.GetTop100
import com.lingsatuo.getqqmusic.MusicGroup
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.service.MusicService
import com.lingsatuo.widget.XTextView

class Top100 : BaseActivity (){
    var list = ArrayList<MusicItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.playerlist_activity)
        setRootView(findViewById(R.id.root))
        setSupportActionBar(findViewById(R.id.toolbar))
        val adapter = MPlaylistActivityRvAdapter(this)
        val rv = findViewById<RecyclerView>(R.id.playlist_rv)
        val group = intent.getSerializableExtra("GROUP") as MusicGroup
        findViewById<XTextView>(R.id.title).text = group.title
        rv.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        rv.adapter = adapter
        GetTop100({e,l->
            e?.printStackTrace()
            findViewById<LinearLayout>(R.id.play_list_loading).visibility = View.GONE
            rv.visibility = View.VISIBLE
            adapter.setData(l)
            list.clear()
            list.addAll(l)
            adapter.notifyDataSetChanged()
        }).start()
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
    }
}