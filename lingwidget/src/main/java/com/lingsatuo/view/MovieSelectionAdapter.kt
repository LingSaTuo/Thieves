package com.lingsatuo.view

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by Administrator on 2018/4/2.
 */
class MovieSelectionAdapter(layout:LinearLayout):MovieAbsAdapter(layout) {
    private var list = ArrayList<View>()

    fun clear(){
        list.clear()
        layout.removeAllViews()
    }
    fun addData(title:String){
        val inflater = LayoutInflater.from(layout.context)
        val v = inflater.inflate(R.layout.selection_item,layout,false)
        val tit = v.findViewById<TextView>(R.id.selection_title)
        tit.text = title
        list.add(v)
    }
    override fun getCount(): Int = list.size

    override fun getView(postion: Int): View = list.get(postion)
}