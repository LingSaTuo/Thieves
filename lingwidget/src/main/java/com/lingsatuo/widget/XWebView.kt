package com.lingsatuo.widget


import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.*
import java.io.IOException


/**
 * Created by Administrator on 2018/3/30.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class XWebView : WebView {
    companion object {
        val KEY = "PLAYER_URL"
    }

    private var mheight = 0
    private var source = ""
    private var finished = false
    private var listener: () -> Unit = {}
    private var errorListener: (Throwable) -> Unit = { e -> }
    private var newConfig: Configuration? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(context, attributeSet, def) {
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.savePassword = true
        settings.saveFormData = true
        settings.allowFileAccess = true
        settings.useWideViewPort = true
        settings.setSupportZoom(true)
        settings.userAgentString = "\tMozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0"

        scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.builtInZoomControls = false
        settings.pluginState = WebSettings.PluginState.ON
        addJavascriptInterface(InJavaScriptLocalObj(), "local_obj")
        val webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                if (description != null) {
                    this@XWebView.post {
                        errorListener.invoke(IOException("连接超时  :$description"))
                    }
                }
            }
        }
        setWebViewClient(webViewClient)
        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (progress == 100 && !finished) {
                    finished = true
                    Thread.sleep(2000)
                    val js = "window.local_obj.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');"
                    this@XWebView.post {
                        run {
                            try {
                                this@XWebView.load("javascript:$js")
                            }catch (e:Throwable){
                                errorListener.invoke(e)
                            }
                        }
                    }
                }
                super.onProgressChanged(view, newProgress)
            }
        }
    }

    fun addFinishListener(listener: () -> Unit) {
        this.listener = listener
    }
    fun load(url: String?){
        post {
            super.loadUrl(url)
        }
    }
    override fun loadUrl(url: String?) {
        finished = false
        post {
            run {
                super.loadUrl(url)
            }
        }
    }

    fun setOnErrorListener(listener: (e: Throwable) -> Unit) {
        this.errorListener = listener
    }

    fun fullScreen() {
        //loadUrl("javascript:_fullscreen")
    }

    fun getSource() = source
    internal inner class InJavaScriptLocalObj {
        @JavascriptInterface
        fun showSource(html: String) {
            if (newConfig?.orientation != Configuration.ORIENTATION_LANDSCAPE)
                mheight = this@XWebView.measuredHeight
            source = html
            listener.invoke()
            load("about:blank;")
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mheight != 0 && measuredHeight > mheight && newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setMeasuredDimension(measuredWidth, mheight)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        this.newConfig = newConfig
    }

}