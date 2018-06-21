package com.lingsatuo.musiclrc

import com.lingsatuo.view.LrcLineBean
import java.io.File
import java.io.FileInputStream
import java.util.ArrayList
import java.util.regex.Pattern

object LrcFactory {
    class Builder(private var lrcpath: String) {
        private val list = ArrayList<LrcLineBean>()
        fun build() {
            val file = File(lrcpath)
            val buffers = ByteArray(file.length().toInt())
            val inputStream = FileInputStream(file)
            inputStream.read(buffers)
            inputStream.close()
            val lines = String(buffers, charset("UTF-8"))
            readLine(lines)
        }

        fun getRoot() = list
        fun readLine(str: String) {
            list.clear()
            val lines = str.split("\n")
            for (index in 0 until lines.size) {
                val bean = LrcLineBean()
                bean.lrc = getLine(lines[index])
                bean.start = getTime(lines[index])
                if (index+1<lines.size) bean.end = getTime(lines[index+1])
                list.add(bean)
            }
        }

        private fun getTime(str: String): Long {
            val patterns = Pattern.compile("(?<=\\[)(\\d+:\\d+\\.\\d+)(?=\\])")
            val matcher = patterns.matcher(str)
            if (matcher.find()) {
                val ts = matcher.group()
                val m = ts.split(":")[0]
                val s = ts.split(":")[1].split(".")[0]
                val ms = ts.split(":")[1].split(".")[1]
                return m.toLong() * 60 * 1000 + s.toLong() * 1000 + ms.toLong() * 10
            }
            return 0L
        }

        private fun getLine(str: String): String {
            val lines = str.split("]")
            if (lines.size < 2) {
                val patterns = Pattern.compile("(?<=\\[)\\S*(?=\\])")
                val matcher = patterns.matcher(str)
                if (matcher.find()) {
                    return matcher.group().split(":")[1].replace("&apos;","'")
                }
            } else {
                return lines[1].replace("&apos;","'")
            }
            return "--------"
        }
    }
}