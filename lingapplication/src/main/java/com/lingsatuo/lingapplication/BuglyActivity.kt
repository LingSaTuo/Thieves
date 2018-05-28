package com.lingsatuo.lingapplication

import android.os.Bundle
import com.lingsatuo.app.BaseActivity
import com.lingsatuo.widget.XTextView

/**
 * Created by Administrator on 2018/3/26.
 */
class BuglyActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val throwable = intent.getSerializableExtra(UncaughtExceptionHandler.ERRORKEY) as Throwable
        throwable.printStackTrace()
        setContentView(R.layout.lingsatuo_bug)
        setSupportActionBar(findViewById(R.id.toolbar))
        setRootView(findViewById(R.id.root))
        supportActionBar?.title = ""
        val mtitle = findViewById<XTextView>(R.id.title)
        mtitle.text = resources.getString(R.string.crash)
        try {
            CrashPrint(this).crashPrint(R.id.bug, throwable)
        }catch (e:Throwable){
            e.printStackTrace()
        }
    }
}