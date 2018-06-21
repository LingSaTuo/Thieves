package com.lingsatuo.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.lingsatuo.getqqmusic.MusicGroup
import com.lingsatuo.thieves.R

class MPlaylistAdapter(var context: Context):RecyclerView.Adapter<ItemView>(){
    private var mdata = ArrayList<MusicGroup>()
    private var listener :(Int,View)->Unit = {_,_->}
    init {
        for(a in 0..5){
            val music = MusicGroup()
            music.title = "Loading..."
            music.href = "about:blank;"
            music.amount = "0万"
            music.disstid="00000000"
            music.icon = "about:blank;"
            mdata.add(music)
        }
    }
    fun setData(mdata:ArrayList<MusicGroup>){
        this.mdata = mdata
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView = ItemView(LayoutInflater.from(context).inflate(R.layout.rec_listitem,parent,false),listener)

    //当view划出屏幕时清除icon控件，避免封面错位
    override fun onViewRecycled(holder: ItemView) {
        if (holder.getIcon()!=null)
         Glide.clear(holder.getIcon())
        super.onViewRecycled(holder)
    }
    fun setOnItemClickListener(listener:(Int,View)->Unit){
        this.listener = listener
    }
    fun getItem(postion:Int):MusicGroup{
        return mdata[postion]
    }

    override fun getItemCount() = mdata.size

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        holder.setMusicGroup(getItem(position))
    }
}
class ItemView(private var view: View,listener:(Int,View)->Unit): RecyclerView.ViewHolder(view) {

    fun setMusicGroup(musicGroup: MusicGroup){
        setIcon(musicGroup.icon)
        setTitle(musicGroup.title)
        setAmount(musicGroup.amount)
    }

    fun setIcon(uri:String){
        val icon = view.findViewById<ImageView>(R.id.playlist_icon)
        Glide.with(view.context)
                .load(uri)
                .asBitmap()
                .placeholder(R.mipmap.loading)
                .priority(Priority.HIGH)
                .into(icon)
    }
    fun getIcon() = view.findViewById<ImageView>(R.id.playlist_icon)
    fun setTitle(title:String){
        view.findViewById<TextView>(R.id.playlist_title).text = title
    }
    fun setAmount(amount:String){
        if(amount == "-"){
            view.findViewById<TextView>(R.id.playlist_amount).text = ""
            view.findViewById<View>(R.id.re_icon_right).visibility = View.GONE
            view.findViewById<View>(R.id.re_top).visibility = View.GONE
        }else{
            view.findViewById<TextView>(R.id.playlist_amount).text = amount
            view.findViewById<View>(R.id.re_icon_right).visibility = View.VISIBLE
            view.findViewById<View>(R.id.re_top).visibility = View.VISIBLE
        }
    }
    init {
        view.findViewById<CardView>(R.id.play_rec_list_card).setOnClickListener {
            listener.invoke(position,view)
        }
    }
}