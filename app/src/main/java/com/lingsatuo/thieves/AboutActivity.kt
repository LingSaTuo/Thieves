package com.lingsatuo.thieves

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import com.lingsatuo.app.BaseActivity
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.widget.Toast


class AboutActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
        setRootView(findViewById(R.id.root))
        findViewById<LinearLayout>(R.id.xl).setOnClickListener {
            val uri = Uri.parse("http://music.163.com/m/user/home?id=279314460")
            val intent = Intent(ACTION_VIEW, uri)
            startActivity(intent)
        }
        findViewById<LinearLayout>(R.id.wwsw).setOnClickListener {
            val uri = Uri.parse("https://github.com/xiaoxinwangluo")
            val intent = Intent(ACTION_VIEW, uri)
            startActivity(intent)
        }
        findViewById<LinearLayout>(R.id.me).setOnClickListener {
            Toast.makeText(this,"海不会不蓝，我不会不在，除了.....",Toast.LENGTH_LONG).show()
        }
    }
}