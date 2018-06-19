package com.lingsatuo.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingsatuo.adapter.MSearchActivityRvAdapter
import com.lingsatuo.getqqmusic.GetSinerSongList
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.service.MusicService
import com.lingsatuo.thieves.Controller
import com.lingsatuo.thieves.PlayerActivity
import com.lingsatuo.utils.LoadingMorePop

class SingerListFragment : Fragment() {
    private var activity: Activity? = null
    private var lastpage = false
    private var page = 1
    private var load: LoadingMorePop? = null
    private var list = ArrayList<MusicItem>()

    companion object {
        fun getInstance(title: String, type: Int): SingerListFragment {
            val mBundle = MBundle(title, type)
            val fragment = SingerListFragment()
            val bundle = Bundle()
            bundle.putSerializable("bundle", mBundle)
            fragment.arguments = bundle
            return fragment
        }
    }

    fun setActivity(activity: Activity?) {
        this.activity = activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity == null) {
            Log.e(toString(),"Activity is Null")
            return null
        }
        Log.e(toString(),"Activity is non-null")
        val rv = RecyclerView(activity)
        val adapter = MSearchActivityRvAdapter(activity!!)
        rv.adapter = adapter
        rv.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        val singer = activity!!.intent.getSerializableExtra("item") as MusicItem.Singer
        Log.e(toString(),singer.singermid)
        GetSinerSongList(singer, page, { e, list ->
            if (e != null) e.printStackTrace()
            else {
                for (item in list)item.href
                adapter.getData().addAll(list)
                adapter.notifyDataSetChanged()
            }
        }).start()
        adapter.setOnItemClickListener { i, view ->
            val item = adapter.getItem(i)
            if (item.singmid == MusicService.instance?.item?.singmid) {
                val intent = Intent(activity, PlayerActivity::class.java)
                startActivity(intent)
            } else {
                Controller.index = i-1
                Controller.list.clear()
                for (index in 1 until adapter.getData().size){
                    Controller.list.add(adapter.getData()[index])
                }
                MusicService.instance?.start(item)
                adapter.notifyDataSetChanged()
            }
        }
        adapter.setOnLoadingMoreListener {
            if (lastpage) return@setOnLoadingMoreListener
            page+=30
            load = LoadingMorePop(activity!!)
            load!!.show(rv)
            GetSinerSongList(singer, page, { e, list ->
                if (e != null) e.printStackTrace()
                else {
                    load?.dissmiss()
                    adapter.loadingFinish()
                    if (list.size==0){
                        lastpage = true
                        return@GetSinerSongList
                    }
                    this.list.addAll(list)
                    adapter.setData(this.list)
                    adapter.notifyDataSetChanged()
                }
            }).start()
        }
        return rv
    }
}