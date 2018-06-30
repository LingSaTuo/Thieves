package com.lingsatuo.thieves

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.ImageView
import com.lingsatuo.adapter.UserRvAdapter
import com.lingsatuo.app.BaseActivity
import com.lingsatuo.getqqmusic.FindUserList
import com.lingsatuo.utils.AccLoginCenter
import com.lingsatuo.utils.SaveSatate
import com.lingsatuo.widget.XTextView
import kotlinx.android.synthetic.main.selection_user_activtity.*

class SelectionUser : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selection_user_activtity)
        setRootView(findViewById(R.id.root))
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<XTextView>(R.id.title).text = "账户代理"
        val adapter = UserRvAdapter(this)
        val rv = local_list
        rv.layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
        rv.adapter = adapter
        adapter.setOnItemClickListener { i ->
            if (adapter.getItem(i).qqnum == AccLoginCenter.last.qqnum) {
                Snackbar.make(rv, "是否需要取消代理？", Snackbar.LENGTH_LONG).setAction("退出", {
                    AccLoginCenter.invoke(UserRvAdapter.User())
                    SaveSatate.setUserInfo(this, UserRvAdapter.User())
                    adapter.notifyItemChanged(i)
                }).show()
            } else {
                AccLoginCenter.invoke(adapter.getItem(i))
                SaveSatate.setUserInfo(this, adapter.getItem(i))
                finish()
            }
        }
        FindUserList(null, { list ->
            selection_loading.visibility = View.GONE
            rv.visibility = View.VISIBLE
            if (list.size == 0){
                selection_xxxx.text = "没有可用的代理列表呢"
            }
            adapter.setUserList(list)
            adapter.notifyDataSetChanged()
        }).start()
        findViewById<ImageView>(R.id.selection_login_qq).setOnClickListener { v ->

        }
    }
}