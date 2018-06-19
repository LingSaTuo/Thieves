package com.lingsatuo.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.ByteArrayOutputStream

object BitmapCompress {
    fun WeChatBitmapToByteArray(bmp: Bitmap): Bitmap {
        val output = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output)
        val zoom = Math.sqrt((32 * 1024 / output.toByteArray().size.toFloat()).toDouble()).toFloat()
        val matrix = Matrix()
        matrix.setScale(zoom, zoom)
        var resultBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
        output.reset()
        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
        while (output.toByteArray().size > 32 * 1024) {
            matrix.setScale(0.9f, 0.9f)
            resultBitmap = Bitmap.createBitmap(
                    resultBitmap, 0, 0,
                    resultBitmap.width, resultBitmap.height, matrix, true)
            output.reset()
            resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
        }
        return resultBitmap
    }
}