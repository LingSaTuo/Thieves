package com.lingsatuo.donttouchme.are_u_understand.do_not_touch_me_yet.plz_go_back.uh_huh.what_are_u_doing.fucking_deep.are_u_gay.why_dont_u_go_back

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Process
import android.view.View
import android.widget.Toast

object I_LOVE_U {
    private var index = 0
    private var id = 0
    private var toast: Toast?=null
    @SuppressLint("ShowToast")
    fun onClick(activity: Activity, view: View){
        if (view.id!=id){
            index = 0
            id = view.id
        }
        if (toast==null) {
            toast = Toast.makeText(activity,"",Toast.LENGTH_SHORT)
        }
        show(index++)
    }
    private fun show(dex:Int){
        var text = ""
        when(dex){
            0->{
                text = "还没有做相应的功能,很多都没做"
            }
            1->{
                text = "真的还没做"
            }
            2->{
                text = "爱信不信"
            }
            3->{
                text = "要是做了肯定给你用"
            }
            4->{
                text = "我生气了"
            }
            5->{
                text = "哎呀，老哥，真的没有做"
            }
            6->{
                text = "你随意，我没做就是没做"
            }
            7->{
                text = "再点我也不说话了"
            }
            8->{
            return
            }
            9->{
                return
            }
            10->{
                return
            }
            11->{
                text = "wox，别点了，挺浪费内存的"
            }
            12->{
                text = "你再点一下我就要死了"
            }
            13->{
                text = "？？？"
            }
            14->{
                text = "我敲立马！！！"
            }
            15->{
              Process.killProcess(Process.myPid())
            }
        }
        toast?.setText(text)
        toast?.duration = Toast.LENGTH_SHORT
        toast?.show()
    }
}