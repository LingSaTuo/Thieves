package com.lingsatuo.utils

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.lingsatuo.thieves.R

class MusicMoreInfoPop(private var activity : Activity) {
    private var pop: PopupWindow
    private val view: View = LayoutInflater.from(activity).inflate(R.layout.player_activity_more, null, false)

    init {
        pop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        pop.isOutsideTouchable = false
        pop.animationStyle = R.style.down_load_menu
    }

    fun show(v: View) {
        pop.showAtLocation(v, Gravity.BOTTOM or Gravity.CENTER, 0, 0)
    }

    fun dissmiss() {
        pop.dismiss()
    }
}