package com.lingsatuo.thieves

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.lingsatuo.adapter.MPlaylistActivityRvAdapter
import com.lingsatuo.getqqmusic.*
import com.lingsatuo.service.MusicService
import com.lingsatuo.utils.MusicMoreInfoPop
import com.lingsatuo.widget.XTextView

class PlaylistActivityInitView(private var playlistActivity: PlaylistActivity) {
    private val adapter = MPlaylistActivityRvAdapter(playlistActivity)
    private lateinit var rv: RecyclerView
    private var l = ArrayList<MusicItem>()
    private val listener: (Throwable?, ArrayList<MusicItem>) -> Unit = { a, b ->
        run {
            if (a != null) {
                playlistActivity.findViewById<XTextView>(R.id.playlist_avtivity_loading_error).text = a.toString()
                a.printStackTrace()
            }
            if (b.size < 1) return@run
            l = b
            playlistActivity.findViewById<LinearLayout>(R.id.play_list_loading).visibility = View.GONE
            rv.visibility = View.VISIBLE
            adapter.setData(b)
            adapter.notifyDataSetChanged()
        }
    }

    fun setView() {
        val group = playlistActivity.intent.getSerializableExtra("GROUP") as MusicGroup
        rv = playlistActivity.findViewById(R.id.playlist_rv)
        playlistActivity.findViewById<XTextView>(R.id.title).text = group.title
        rv.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        rv.adapter = adapter
        val head = LayoutInflater.from(playlistActivity).inflate(R.layout.playlist_rv_head, null, false)
        adapter.setOnItemClickListener { i, view ->
            val item = adapter.getItem(i)
            when (view.id) {
                R.id.playlist_item_more -> {
                    MusicMoreInfoPop(playlistActivity,item).show()
                }
                else -> {
                    if (item.singmid == MusicService.instance?.item?.singmid) {
                        val intent = Intent(playlistActivity, PlayerActivity::class.java)
                        playlistActivity.startActivity(intent)
                    } else {
                        Controller.index = i - 1
                        Controller.list.clear()
                        Controller.list.addAll(l)
                        MusicService.instance?.start(item)
                    }
                }
            }
            adapter.notifyDataSetChanged()
        }
        if (!playlistActivity.isDestroyed)
            Glide.with(playlistActivity)
                    .load(group.icon)
                    .asBitmap()
                    .placeholder(R.mipmap.loading)
                    .priority(Priority.HIGH)
                    .into(head.findViewById(R.id.play_list_head))
        MusicItemGet(listener, group).start()
    }
}