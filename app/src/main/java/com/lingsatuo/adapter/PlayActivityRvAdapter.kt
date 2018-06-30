package com.lingsatuo.adapter

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.service.MusicService
import com.lingsatuo.thieves.R
import com.lingsatuo.utils.StartMV

class PlayActivityRvAdapter(var context: Context) : RecyclerView.Adapter<ItemViewII>() {
    private var mdata = ArrayList<MusicItem>()
    private var hand = View(context)
    private var listener: (Int, View) -> Unit = { _, _ -> }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0
        else 1
    }

    fun setData(mdata: ArrayList<MusicItem>) {
        this.mdata.clear()
        this.mdata.add(MusicItem())
        this.mdata.addAll(mdata)
    }

    fun setHand(view: View) {
        hand = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewII {
        return if (viewType != 0) ItemViewII(LayoutInflater.from(context).inflate(R.layout.playlist_content_listitem, parent, false), listener)
        else ItemViewII(hand, listener)
    }

    //当view划出屏幕时清除icon控件，避免封面错位
    override fun onViewRecycled(holder: ItemViewII) {
        if (holder.getIcon()!=null)
        Glide.clear(holder.getIcon())
        super.onViewRecycled(holder)
    }

    fun setOnItemClickListener(listener: (Int, View) -> Unit) {
        this.listener = listener
    }

    fun getItem(postion: Int): MusicItem {
        return mdata[postion]
    }

    override fun getItemCount() = mdata.size

    override fun onBindViewHolder(holder: ItemViewII, position: Int) {
        if (position == 0) return
        holder.setMusicGroup(getItem(position))
    }
}

class ItemViewII(private var view: View, listener: (Int, View) -> Unit) : RecyclerView.ViewHolder(view) {

    fun setMusicGroup(musicitem: MusicItem) {
        setTitle(musicitem.title)
        setSubTitle(musicitem.getSingers() + " - " + musicitem.album)
        view.findViewById<ImageView>(R.id.list_item_album_icon).setImageResource(R.mipmap.album_d)
        setIcon(musicitem.icon)
        if (MusicService.instance?.item?.singmid?.equals(musicitem.singmid) == true) {
            view.findViewById<ImageView>(R.id.playlist_item_more).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.playlist_item_more).setImageResource(R.mipmap.playing)
        } else
            view.findViewById<ImageView>(R.id.playlist_item_more).setImageResource(R.mipmap.more)

        view.findViewById<ImageView>(R.id.playlist_item_mv).visibility = if (musicitem.mvItem != null) {
            View.VISIBLE
        } else {
            View.GONE
        }
        view.findViewById<ImageView>(R.id.playlist_item_mv).setOnClickListener {
            StartMV.start(view.context,musicitem)
        }
    }

    fun setIcon(icon: String) {
        Glide.with(view.context)
                .load(icon)
                .asBitmap()
                .placeholder(R.mipmap.album_d)
                .priority(Priority.HIGH)
                .error(R.mipmap.album_d)
                .into(getIcon())
    }

    fun getIcon() = view.findViewById<ImageView>(R.id.list_item_album_icon)
    fun setTitle(title: String) {
        view.findViewById<TextView>(R.id.playlist_item_title).text = title
    }

    fun setSubTitle(amount: String) {
        view.findViewById<TextView>(R.id.playlist_item_subtitle).text = amount
    }

    init {
        view.findViewById<LinearLayout>(R.id.play_activity_list_item)?.setOnClickListener {
            listener.invoke(position, view)
        }
        view.findViewById<AppCompatImageView>(R.id.playlist_item_more)?.setOnClickListener {
            listener.invoke(position, view.findViewById<AppCompatImageView>(R.id.playlist_item_more)!!)
        }
    }
}
