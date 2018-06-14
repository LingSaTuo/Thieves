package com.lingsatuo.thieves

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.view.View
import com.lingsatuo.Typeface
import com.lingsatuo.app.BaseActivity

class SingerInfoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.singer_info_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
        super.setStatusBarTransparent()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        findViewById<Toolbar>(R.id.toolbar).setPadding(0, getHeight(), 0, 0)
        findViewById<Toolbar>(R.id.toolbar).layoutParams.height = findViewById<Toolbar>(R.id.toolbar).layoutParams.height + getHeight()

        findViewById<CollapsingToolbarLayout>(R.id.singer_info_ctl).setCollapsedTitleTextColor(resources.getColor(R.color.button_textColor_Dark))
        findViewById<CollapsingToolbarLayout>(R.id.singer_info_ctl).setCollapsedTitleTypeface(Typeface.getType(this))

        findViewById<CollapsingToolbarLayout>(R.id.singer_info_ctl).setExpandedTitleColor(resources.getColor(R.color.button_textColor_Dark))
        findViewById<CollapsingToolbarLayout>(R.id.singer_info_ctl).setExpandedTitleTypeface(Typeface.getType(this))
        supportActionBar?.title = "MusicOne"
        SingerInfoInitView(this).setView()
    }
}