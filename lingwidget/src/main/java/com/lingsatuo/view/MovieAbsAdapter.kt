package com.lingsatuo.view

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by Administrator on 2018/4/2.
 */
abstract class MovieAbsAdapter(var layout:LinearLayout) {
    private var itemlistener:(View,Int)->Unit = {v,i->}
    var last:View?=null
    fun notifyDataSetChanged(){
        layout.removeAllViews()
        for (i in 0 until getCount()){
            val view = getView(i)
            if (i==0&&last==null){
                setColor(view)
                last = view
            }
            view.setOnClickListener {v->
                if (last!=null) {
                    last!!.findViewById<LinearLayout>(R.id.selection_bg).setBackgroundResource(R.drawable.selection_item_bg)
                    last!!.findViewById<TextView>(R.id.selection_title).setTextColor(Color.parseColor("#383737"))
                }
                last = view
                setColor(v)
                itemlistener.invoke(v,i)
            }
            layout.addView(getView(i))
        }
    }
    private fun setColor(view:View){
        val title = view.findViewById<TextView>(R.id.selection_title)
        title.setTextColor(Color.parseColor("#FF4081"))
        val bg = view.findViewById<LinearLayout>(R.id.selection_bg)
        bg.setBackgroundResource(R.drawable.selection_item_bg_clicked)
    }
    fun setOnItemClickListener(itemlistener:(View,Int)->Unit){
        this.itemlistener = itemlistener
    }
    abstract fun getCount():Int
    abstract fun getView(postion:Int):View
}