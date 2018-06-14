package com.lingsatuo.thieves

import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.lingsatuo.adapter.SingerViewPagerAdapter
import com.lingsatuo.fragment.SingerInfoFragment
import com.lingsatuo.fragment.SingerListFragment
import com.lingsatuo.getqqmusic.MusicItem

class SingerInfoInitView(private var singerInfoActivity : SingerInfoActivity) {
    private val singeritem = singerInfoActivity.intent.getSerializableExtra("item") as MusicItem.Singer
    fun setView(){
        val tab = singerInfoActivity.findViewById<TabLayout>(R.id.singerinfo_content_tab)
        val viewpager = singerInfoActivity.findViewById<ViewPager>(R.id.singerinfo_content_viewpage)
        tab.setupWithViewPager(viewpager)
        tab.setTabTextColors(singerInfoActivity.resources.getColor(R.color.subbutton_textColor),singerInfoActivity.resources.getColor(R.color.colorAccent))
        val adapter = SingerViewPagerAdapter(singerInfoActivity.supportFragmentManager)
        viewpager.adapter = adapter
        val songlist = SingerListFragment.getInstance("单曲",0)
        songlist.setActivity(singerInfoActivity)
        adapter.addFragment(songlist)
        val singer_info = SingerInfoFragment.getInstance("歌手信息",1)
        singer_info.setActivity(singerInfoActivity)
        adapter.addFragment(singer_info)
        adapter.notifyDataSetChanged()
        singerInfoActivity.findViewById<CollapsingToolbarLayout>(R.id.singer_info_ctl).title = singeritem.singer
        Glide.with(singerInfoActivity)
                .load(singeritem.getSingerIcon())
                .asBitmap()
                .placeholder(R.mipmap.singer_dark)
                .priority(Priority.HIGH)
                .into(singerInfoActivity.findViewById(R.id.singer_info_header_icon))
    }
}