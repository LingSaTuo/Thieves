package com.lingsatuo.utils

import android.os.Process
import android.support.v7.app.AlertDialog
import com.lingsatuo.app.BaseActivity
import org.json.JSONObject
import java.util.regex.Pattern

object ContinueUtils {
    fun chaeckCanUse(activity: BaseActivity) {
        if (activity.isDestroyed)
        Thread(Runnable {
            println("------------------------------")
            val str = NetWork().getBytes("https://share.weiyun.com/1322e08412c11d519cadc105baa02d3e")
            val html = String(str)
            println(html)
            val json = get(html)

        }).start()
    }

    fun get(json: String): JSONObject {
        val patterns = Pattern.compile("callbackling=\\{[.*\\S\\s]+\\}")
        val matcher = patterns.matcher(json)
        if (matcher.find()) {
            val str = matcher.group()
            val patterns2 = Pattern.compile("\\{[.*\\S\\s]+\\}")
            val matcher2 = patterns2.matcher(str)
            if (matcher2.find()) {
                return JSONObject(matcher2.group())
            }
        }
        return JSONObject()
    }

    fun showDialog(activity: BaseActivity, title: String, tag: String, msg: String, canuse: Boolean) {
        if (SaveSatate.read(activity, tag) == tag) return
        val ad = AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(canuse)
                .setPositiveButton("取消", null)
                .show()
        if (!canuse) {
            ad.setCanceledOnTouchOutside(false)
            ad.setCancelable(false)
            ad.setOnDismissListener {
                Process.killProcess(Process.myPid())
            }
        }
    }
}