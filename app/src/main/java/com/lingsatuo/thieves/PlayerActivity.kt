package com.lingsatuo.thieves

import android.os.Bundle
import com.lingsatuo.app.BaseActivity

class PlayerActivity : BaseActivity() {
    private lateinit var pai: PlayerActivityInitView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
        setRootView(findViewById(R.id.app_bar))
        initView()
    }

    private fun initView() {
        pai = PlayerActivityInitView(this)
        pai.initView()
    }

    override fun onBackPressed() {
        pai.close()
        super.onBackPressed()
    }
}