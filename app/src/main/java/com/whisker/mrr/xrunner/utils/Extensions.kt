package com.whisker.mrr.xrunner.utils

import android.util.Log
import android.widget.ImageView
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

fun Any.TAG() = this::class.java.simpleName

fun ImageView.loadSnapshot(fileName: String) {
    if(FileUtils.fileExists(context, fileName)) {
        Log.e("IMAGE VIEW", "File Exists")
        val image = FileUtils.getFile(context, fileName)
        Picasso.get().load(image).into(this)
    } else {
        val imageReference = FirebaseStorage.getInstance().reference.child("snapshots/$fileName")
        imageReference.downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(this)
        }
    }

}