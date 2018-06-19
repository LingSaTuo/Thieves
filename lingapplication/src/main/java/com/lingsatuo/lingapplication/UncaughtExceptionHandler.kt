package com.lingsatuo.lingapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Process

/**
 * Created by Administrator on 2018/3/26.
 */
@SuppressLint("StaticFieldLeak")
class UncaughtExceptionHandler private constructor() : Thread.UncaughtExceptionHandler {
    companion object {
        private var contxet:Context?=null
        const val ERRORKEY = "BugCrash"
        private var handler: UncaughtExceptionHandler? = null
        fun getInstance(contextt: Context): UncaughtExceptionHandler {
            this.contxet = contextt
            handler = handler ?: UncaughtExceptionHandler()
            return handler!!
        }
    }

    override fun uncaughtException(p0: Thread?, p1: Throwable?) {
        p1?.printStackTrace()
        val bug = Intent(contxet,BuglyActivity::class.java)
        bug.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        bug.putExtra(ERRORKEY,p1)
        contxet?.startActivity(bug)
        LingBaseTop.exitApp()
        Process.killProcess(Process.myPid())
    }
    fun init(){
        if (handler!=null)Thread.setDefaultUncaughtExceptionHandler(handler)
    }
}