package com.lingsatuo.lingapplication

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.text.Html
import java.util.regex.Pattern


/**
 * Created by Administrator on 2018/3/26.
 */
class CrashPrint(private var activity: BuglyActivity) {

    fun crashPrint(id: Int, throwable: Throwable) {
        val textView = activity.findViewById<AppCompatTextView>(id)
        textView.setTextIsSelectable(true)
        printError(textView, throwable)
    }

    private fun printError(textView: AppCompatTextView, throwable: Throwable) {
        textView.append(Html.fromHtml("<font color=\"#E51C23\">" + throwable.message + "</font>"))
        textView.append("\n")
        print(textView, throwable)

    }

    private fun print(textView: AppCompatTextView, throwable: Throwable) {
        for (stackTraceElement in throwable.stackTrace) {
            val className = stackTraceElement.className
            val methodName = stackTraceElement.methodName
            val fileName = stackTraceElement.fileName
            val line = stackTraceElement.lineNumber.toString() + ""
            textView.append("\t\t\t\t\t\t")
            textView.append(Html.fromHtml("<font  color=\"#B75A03\">at</font>"))
            if (!isInApp(textView.context, className)) {
                textView.append("\t" + (className?:"Unknwon"))
                textView.append(".${methodName?:"Unknown"}")
                textView.append("(")
                textView.append(fileName?:"Unknown")
                textView.append(":")
                textView.append(line)
            } else {
                textView.append(Html.fromHtml("<i><u><font color=\"#05156C\">\t${className?:"Unknwon"}</font></u></i>"))
                textView.append(Html.fromHtml("<i><u><font color=\"#05156C\">.${methodName?:"Unknwon"}</font></u></i>"))
                textView.append("(")
                textView.append(Html.fromHtml("<font color=\"#0614AE\">${fileName?:"Unknwon"}</font>"))
                textView.append(":")
                textView.append(Html.fromHtml("<u><font color=\"#5677FC\">$line</font></u>"))
            }
            textView.append(")")
            textView.append("\n")
        }
        val cause = throwable.cause
        if (cause != null) {
            appendCause(textView, cause)
        }
    }

    private fun appendCause(textView: AppCompatTextView, throwable: Throwable) {
        textView.append("\nCause by :")
        textView.append(Html.fromHtml("<font color=\"#E51C23\">" + "" + throwable.toString() + "</font>"))
        textView.append("\n")
        print(textView, throwable)
        val casue = throwable.cause
        if (casue != null) {
            printError(textView, casue)
        }
    }

    private fun isInApp(context: Context, className: String): Boolean {
        val pattern = Pattern.compile("\\w+.\\w")
        val matcher = pattern.matcher(className)
        if (matcher.find()) {
            return context.packageName.startsWith(matcher.group())
        }
        return false
    }
}