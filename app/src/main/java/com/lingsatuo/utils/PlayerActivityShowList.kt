package com.lingsatuo.utils

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.lingsatuo.adapter.PlayActivityRvAdapter
import com.lingsatuo.thieves.Controller
import com.lingsatuo.thieves.PlayerActivity
import com.lingsatuo.thieves.R

class PlayerActivityShowList(private var playerActivity: PlayerActivity) {
    private var pop: PopupWindow
    private val view: View = LayoutInflater.from(playerActivity).inflate(R.layout.player_activity_show_list, null, false)

    init {
        pop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        pop.isOutsideTouchable = true
        pop.setBackgroundDrawable(ColorDrawable(0x00ffffff))
        pop.animationStyle = R.style.down_load_menu
    }

    fun show(v: View) {
        pop.showAtLocation(v, Gravity.BOTTOM, 0, 0)
        view.layoutParams.height = (playerActivity.windowManager.defaultDisplay.height * 0.65f).toInt()
        val adapter = PlayActivityRvAdapter(playerActivity)
        adapter.setData(Controller.list)
        val rv = view.findViewById<RecyclerView>(R.id.play_activity_list)
        rv.layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        rv.adapter = adapter
        adapter.setOnItemClickListener { i, view ->
            Controller.index = i - 2
            Controller.listener.invoke(Controller.Type.NEXT)
            adapter.notifyDataSetChanged()
        }
    }
}