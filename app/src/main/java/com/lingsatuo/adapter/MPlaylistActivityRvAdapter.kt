package com.lingsatuo.adapter

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
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

open class MPlaylistActivityRvAdapter(var context: Activity) : RecyclerView.Adapter<ItemViewI>() {
    private var mdata = ArrayList<MusicItem>()
    private var hand = View(context)
    private var listener: (Int, View) -> Unit = { _, _ -> }
    private var loadingmore: () -> Unit = {}
    private var loadingfinish = true
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0
        else if (position == mdata.size - 1) 2
        else 1
    }

    fun getData() = mdata
    fun setData(mdata: ArrayList<MusicItem>) {
        this.mdata.clear()
        this.mdata.add(MusicItem())
        this.mdata.addAll(mdata)
    }

    fun setHand(view: View) {
        hand = view
    }

    fun setOnLoadingMoreListener(loadingmore: () -> Unit) {
        this.loadingmore = loadingmore
    }

    fun loadingFinish() {
        loadingfinish = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewI {
        return if (viewType != 0) {
            return ItemViewI(LayoutInflater.from(context).inflate(R.layout.playlist_content_listitem, parent, false), listener)
        } else ItemViewI(hand, listener)
    }

    //当view划出屏幕时清除icon控件，避免封面错位
    override fun onViewRecycled(holder: ItemViewI) {
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

    override fun onBindViewHolder(holder: ItemViewI, position: Int) {
        if (position == 0) return
        if (position == mdata.size-1){
            if (loadingfinish) {
                loadingfinish = false
                loadingmore.invoke()
            }
        }
        holder.setMusicGroup(getItem(position))
    }
}

class ItemViewI(private var view: View, listener: (Int, View) -> Unit) : RecyclerView.ViewHolder(view) {

    fun setMusicGroup(musicitem: MusicItem) {
        setTitle(musicitem.title)
        setSubTitle(musicitem.getSingers() + " - " + musicitem.album)
        view.findViewById<ImageView>(R.id.list_item_album_icon).setImageResource(R.mipmap.album_d)
        setIcon(musicitem.icon)
        if (MusicService.instance?.item?.singmid == musicitem.singmid){
            view.findViewById<TextView>(R.id.playlist_item_title).setTextColor(view.context.resources.getColor(R.color.colorAccent))
            view.findViewById<TextView>(R.id.playlist_item_subtitle).setTextColor(view.context.resources.getColor(R.color.sub_colorAcc))
            view.findViewById<ImageView>(R.id.playlist_item_more).imageTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context,R.color.colorAccent))
        }else{
            view.findViewById<TextView>(R.id.playlist_item_title).setTextColor(view.context.resources.getColor(R.color.button_textColor))
            view.findViewById<TextView>(R.id.playlist_item_subtitle).setTextColor(view.context.resources.getColor(R.color.subbutton_textColor))
            view.findViewById<ImageView>(R.id.playlist_item_more).imageTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context,R.color.button_textColor))
        }
    }

    fun setIcon(uri: String) {
        Glide.with(view.context)
                .load(uri)
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
    }
}
