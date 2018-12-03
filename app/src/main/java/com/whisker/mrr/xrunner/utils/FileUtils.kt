package com.whisker.mrr.xrunner.utils

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FileUtils {

    companion object {
        @Throws(IOException::class)
        fun saveFile(context: Context, data: ByteArray?, fileName: String) {
            val outputStream: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            outputStream.write(data)
            outputStream.flush()
            outputStream.close()
        }

        fun deleteFile(filePath: String) {
            val file = File(filePath)
            if (file.exists()) {
                if (file.delete()) {
                    Log.v("FileTools", "File deleted!")
                } else {
                    Log.v("FileTools", "File NOT deleted!")
                }
            } else {
                Log.v("FileTools", "File does not exist!")
            }
        }

        fun getFile(context: Context, fileName: String) : File {
            val filePath = context.getFileStreamPath(fileName).absolutePath
            return File(filePath)
        }
    }
}