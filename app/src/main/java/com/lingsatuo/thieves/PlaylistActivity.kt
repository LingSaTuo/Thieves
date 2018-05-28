package com.lingsatuo.thieves

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import com.lingsatuo.app.BaseActivity
import com.lingsatuo.getqqmusic.*
import com.lingsatuo.widget.XTextView
import com.lingsatuo.widget.XWebView

class PlaylistActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.playerlist_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
        setRootView(findViewById(R.id.root))
        PlaylistActivityInitView(this).setView()
    }
}