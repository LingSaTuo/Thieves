package com.lingsatuo.utils

import java.io.InputStream
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL




class NetWork {
    fun getBytes(url:String):ByteArray{
        val inputStream = getInputStream(url)
        val bos = ByteArrayOutputStream()
        var buffer = ByteArray(1024)
        var len = 0
        while (true) {
            len = inputStream.read(buffer)
            if (len==-1)break
            bos.write(buffer,0,len)
        }
        return bos.toByteArray()
    }
    private fun getInputStream(url: String):InputStream{
        val url = URL(url)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.connectTimeout = 3000
        return conn.inputStream
    }
}