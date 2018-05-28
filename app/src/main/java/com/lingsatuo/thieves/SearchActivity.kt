package com.lingsatuo.thieves

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.lingsatuo.adapter.MSearchActivityRvAdapter
import com.lingsatuo.app.BaseActivity
import com.lingsatuo.getqqmusic.GetHotKey
import com.lingsatuo.getqqmusic.GetSearchList
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.service.MusicService
import com.lingsatuo.utils.LoadingMorePop
import com.lingsatuo.widget.XTextView

class SearchActivity : BaseActivity() {
    private lateinit var rv: RecyclerView
    private var list = ArrayList<MusicItem>()
    private var page = 1
    private var key = ""
    private var lastpage = false
    private var load: LoadingMorePop? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
        super.setStatusBarTransparent()
        rv = findViewById(R.id.search_list)
        findViewById<View>(R.id.app_bar).setPadding(0, getHeight(), 0, 0)
        rv.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        val floatsearch = findViewById<FloatingSearchView>(R.id.searchview)
        floatsearch.setPadding(getHeight(), getHeight(), getHeight(), 0)
        floatsearch.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
            override fun onSearchAction(currentQuery: String?) {
                loading()
                list.clear()
                lastpage = false
                page = 1
                if (currentQuery == null || currentQuery.isEmpty()) {
                    GetHotKey({ key ->
                        GetSearchList(key, page, { e, l ->
                            this@SearchActivity.key = key
                            finished(e, l)
                        }).start()
                    }).start()
                } else {
                    GetSearchList(currentQuery, page, { e, l ->
                        this@SearchActivity.key = currentQuery
                        finished(e, l)
                    }).start()
                }
            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
            }

        })
        val adapter = MSearchActivityRvAdapter(this)
        rv.adapter = adapter
        adapter.setOnLoadingMoreListener {
            if (lastpage)return@setOnLoadingMoreListener
            page++
            load = LoadingMorePop(this)
            load!!.show(rv)
            GetSearchList(key, page, { e, l ->
                finished(e, l)
            }).start()
        }
        adapter.setOnItemClickListener { i, view ->
            val item = adapter.getItem(i)
            if (item.singmid == MusicService.instance?.item?.singmid) {
                val intent = Intent(this, PlayerActivity::class.java)
                startActivity(intent)
            } else {
                Controller.index = i
                Controller.list = list
                MusicService.instance?.start(item)
            }
        }
    }

    private fun loading() {
        findViewById<LinearLayout>(R.id.search_loading).visibility = View.VISIBLE
        findViewById<RecyclerView>(R.id.search_list).visibility = View.GONE
    }

    private fun finished(e: Throwable?, list: ArrayList<MusicItem>) {
        load?.dissmiss()
        val adapter = (rv.adapter as MSearchActivityRvAdapter)
        adapter.loadingFinish()
        load = null
        if (e != null) {
            findViewById<XTextView>(R.id.search_error).text = e.toString()
        } else {
            findViewById<LinearLayout>(R.id.search_loading).visibility = View.GONE
            findViewById<RecyclerView>(R.id.search_list).visibility = View.VISIBLE
            if (list.size == 0){
                lastpage = true
            }
            this.list.addAll(list)
            adapter.setData(this.list)
            adapter.notifyDataSetChanged()
        }
    }
}