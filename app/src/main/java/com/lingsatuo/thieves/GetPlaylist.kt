package com.lingsatuo.thieves

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.widget.Toast
import com.lingsatuo.adapter.MPlaylistAdapter
import com.lingsatuo.getqqmusic.PlaylistRec
import com.lingsatuo.service.MusicService
import com.lingsatuo.widget.XWebView

class GetPlaylist(private var mainActivity: MainActivity) {
    fun getList(){

        val rv = mainActivity.findViewById<RecyclerView>(R.id.playlist_rv)
        val adapter = MPlaylistAdapter(mainActivity)
        rv.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL)
        rv.adapter = adapter
        adapter.setOnItemClickListener { i, view ->
            val musicgroup = adapter.getItem(i)
            if(musicgroup.href == "about:blank;")return@setOnItemClickListener
            val intent = Intent(mainActivity,PlaylistActivity::class.java)
            intent.putExtra("GROUP",musicgroup)
            mainActivity.startActivity(intent)
        }
        PlaylistRec({ e, list ->
            e?.printStackTrace()
            if (e == null) {
                adapter.setData(list)
                adapter.notifyDataSetChanged()
            }
        }).start()
    }
}