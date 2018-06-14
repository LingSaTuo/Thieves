package com.lingsatuo.utils

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.thieves.R
import com.lingsatuo.thieves.SingerInfoActivity
import com.lingsatuo.widget.XTextView

class SongInfoShowPop (private var activity : Activity,private var musicitem:MusicItem) {
    private var pop: PopupWindow
    private val view: View = LayoutInflater.from(activity).inflate(R.layout.songinfo, null, false)

    init {
        pop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        pop.setBackgroundDrawable(ColorDrawable(0x00ffffff))
        pop.isOutsideTouchable = true
        pop.animationStyle = R.style.down_load_menu
    }

    fun show(v: View) {
        pop.showAtLocation(v, Gravity.BOTTOM or Gravity.CENTER, 0, 0)
        view.findViewById<XTextView>(R.id.songinfo_title).text = musicitem.title
        view.findViewById<XTextView>(R.id.songinfo_singers).text = musicitem.getSingers()
        view.findViewById<XTextView>(R.id.songinfo_album).text = musicitem.album
        view.findViewById<LinearLayout>(R.id.songinfo_singers_).setOnClickListener {
            if (musicitem.singer.size>1){
                showSingers()
            }else{
                val intent = Intent(activity,SingerInfoActivity::class.java)
                intent.putExtra("item",musicitem.singer[0])
                activity.startActivity(intent)
            }
            pop.dismiss()
        }
        view.findViewById<LinearLayout>(R.id.songinfo_album_).setOnClickListener {

        }
    }

    private fun showSingers(){
        val rv = RecyclerView(activity)
        AlertDialog.Builder(activity)
                .setView(rv)
                .setTitle("哪一个？")
                .show()
        val adapter = ListAcapter(musicitem)
        rv.setPadding(0,dip2px(10),0,dip2px(10))
        rv.adapter = adapter
        rv.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        adapter.notifyDataSetChanged()
    }


    inner class ListAcapter(private var musicitem: MusicItem):RecyclerView.Adapter<ItemView>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
            return ItemView(LayoutInflater.from(activity).inflate(R.layout.singer_list,parent,false))
        }
        override fun getItemCount(): Int = musicitem.singer.size

        override fun onBindViewHolder(holder: ItemView, position: Int) {
            holder.setInfo(musicitem.singer[position])
        }

        override fun onViewRecycled(holder:ItemView) {
            Glide.clear(holder.getIcon())
            super.onViewRecycled(holder)
        }
    }
    inner class ItemView(private var view: View) : RecyclerView.ViewHolder(view){
        fun getIcon() = view.findViewById<ImageView>(R.id.singer_list_icon)!!
        fun setInfo(singer:MusicItem.Singer){
            Glide.with(activity)
                    .load(singer.getSingerIcon())
                    .asBitmap()
                    .placeholder(R.mipmap.singer_dark)
                    .priority(Priority.HIGH)
                    .into(getIcon())
            view.findViewById<XTextView>(R.id.singer_list_title).text = singer.singer
            view.findViewById<LinearLayout>(R.id.singer_item).setOnClickListener {
                val intent = Intent(activity,SingerInfoActivity::class.java)
                intent.putExtra("item",singer)
                activity.startActivity(intent)
            }
        }
    }

    private fun dip2px(int: Int): Int {
        val scale = activity.resources.displayMetrics.density
        return (int * scale + 0.5f).toInt()
    }
}