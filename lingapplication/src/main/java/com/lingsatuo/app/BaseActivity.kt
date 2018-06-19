package com.lingsatuo.app

import android.app.ProgressDialog
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import com.lingsatuo.lingapplication.LingBaseTop

/**
 * Created by Administrator on 2018/3/26.
 */
open class BaseActivity : AppCompatActivity() {
    override fun setSupportActionBar(toolbar: Toolbar?) {
        if (toolbar == null) return
        super.setSupportActionBar(toolbar)
        supportActionBar?.title = ""
    }
    fun setRootView(v:View){
        if (Build.VERSION.SDK_INT >= 19) {
            v.setPadding(0, getHeight(), 0, 0)
        }
        setStatusBarTransparent()
    }

    protected open fun setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }else{
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            val view = findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT).getChildAt(0)
            if (view != null) {
                ViewCompat.setFitsSystemWindows(view, false)
                ViewCompat.requestApplyInsets(view)
            }
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val content = findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
            val view = content.getChildAt(0)
            if (view != null) {
                if ("marginAdded" == view.tag) {
                    val para = view.layoutParams as FrameLayout.LayoutParams
                    para.topMargin -= getHeight()
                    view.layoutParams = para
                    view.tag = null
                }
                ViewCompat.setFitsSystemWindows(view, false)
            }
        }
    }

    fun dip2px(int: Int): Int {
        val scale = resources.displayMetrics.density
        return (int * scale + 0.5f).toInt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addActivity(this)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
    fun addActivity(activity: AppCompatActivity) {
        LingBaseTop.addActivity(activity)
    }

    protected open fun getHeight(): Int {
        val id = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (id > 0) resources.getDimensionPixelSize(id) else 0
    }
}