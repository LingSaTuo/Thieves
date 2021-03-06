package com.lingsatuo.utils

import android.content.Context
import java.io.InputStream
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.io.BufferedReader
import java.io.InputStreamReader


class NetWork {
    fun getBytes(url: String): ByteArray {
        try {
            val inputStream = getInputStream(url)
            val bos = ByteArrayOutputStream()
            var buffer = ByteArray(1024)
            var len = 0
            while (true) {
                len = inputStream.read(buffer)
                if (len == -1) break
                bos.write(buffer, 0, len)
            }
            return bos.toByteArray()
        } catch (e: Throwable) {
            e.printStackTrace()
            return "".toByteArray()
        }
    }

    private fun getInputStream(url: String): InputStream {
        val url = URL(url)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.connectTimeout = 3000
        return conn.inputStream
    }

    fun avalibe(context: Context): Boolean {
        val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val net_info = connectivityManager.allNetworkInfo
        if (net_info != null) {
            for (i in net_info.indices) {
                if (net_info[i].state === NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        }
        return false
    }
    fun get(href:String,cookie:String):String{
        val url = URL(href)
        val huc = url.openConnection() as HttpURLConnection
        huc.doOutput = true
        huc.doInput = true
        huc.useCaches = false
        huc.requestMethod = "GET"
        huc.setRequestProperty("Cookie",cookie)
        huc.connect()
        val iis = huc.inputStream
        val buffered = BufferedReader(InputStreamReader(iis, "UTF-8"))
        var s = StringBuffer()
        while (true) {
            val line = buffered.readLine() ?: break
            s.append(line)
        }
        buffered.close()
        iis.close()
        huc.disconnect()
        return s.toString()
    }
}