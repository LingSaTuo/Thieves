package com.lingsatuo.thieves

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.lingsatuo.adapter.MPlaylistAdapter
import com.lingsatuo.getqqmusic.MusicGroup

class MainActivityInitView(private var mainActivity: MainActivity) {
    fun initView() {
        val rv = mainActivity.findViewById<RecyclerView>(R.id.myplaylist_rv)
        val adapter = MPlaylistAdapter(mainActivity)
        rv.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL)
        rv.adapter = adapter
        val list = ArrayList<MusicGroup>()
        val local = MusicGroup()
        local.title = "本地歌曲"
        local.amount = "-"
        local.icon = "http://musicone-1253269015.coscd.myqcloud.com/local.png"
        list.add(local)
        adapter.setData(list)
        adapter.setOnItemClickListener { i, view ->
            val localactivity = Intent(mainActivity,LocalActivity::class.java)
            localactivity.putExtra("MUSICGROUP",adapter.getItem(i))
            mainActivity.startActivity(localactivity)
        }
    }
}