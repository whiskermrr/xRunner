package com.whisker.mrr.xrunner.utils

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

class BitmapUtils {

    companion object {
        fun convertBitmapToByteArray(bitmap: Bitmap, format: Bitmap.CompressFormat) : ByteArray {
            val byteArray = ByteArrayOutputStream()
            bitmap.compress(format, 100, byteArray)
            return byteArray.toByteArray()
        }
    }
}