package com.whisker.mrr.xrunner.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.widget.ImageView
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun Any.TAG() = javaClass.simpleName

fun ImageView.loadSnapshot(fileName: String) {
    if(context.fileExists(fileName)) {
        val image = context.getFile(fileName)
        Picasso.get().load(image).into(this)
    } else {
        val imageReference = FirebaseStorage.getInstance().reference.child("snapshots/$fileName")
        imageReference.downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(this)
        }
    }
}

fun Bitmap.toByteArray(format: Bitmap.CompressFormat) : ByteArray {
    val byteArray = ByteArrayOutputStream()
    this.compress(format, 100, byteArray)
    return byteArray.toByteArray()
}

fun Context.saveFile(data: ByteArray?, fileName: String) {
    val outputStream: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
    outputStream.write(data)
    outputStream.flush()
    outputStream.close()
}
fun Context.getFile(fileName: String) : File {
    val filePath = getFileStreamPath(fileName).absolutePath
    return File(filePath)
}

fun Context.fileExists(fileName: String) : Boolean {
    val file = getFileStreamPath(fileName)
    return file.exists()
}

fun Activity.getScreenWidth() : Int {
    val size = Point()
    windowManager.defaultDisplay.getSize(size)
    return size.x
}
