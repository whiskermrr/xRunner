package com.whisker.mrr.infrastructure.source

import android.content.Context
import android.content.SharedPreferences
import com.whisker.mrr.data.source.SnapshotLocalSource
import java.io.FileOutputStream
import javax.inject.Inject

class SnapshotLocalDataSource
@Inject constructor(private val context: Context, private val sharedPreferences: SharedPreferences) :
    SnapshotLocalSource {

    companion object {
        const val EXTRA_SNAPSHOT_NAMES_SET = "extra_snapshot_names_set"
    }

    override fun saveSnapshotLocal(bitmap: ByteArray, fileName: String) : String {
        saveFile(bitmap, fileName)

        val snapshotNames = sharedPreferences.getStringSet(EXTRA_SNAPSHOT_NAMES_SET, mutableSetOf())
        snapshotNames!!.add(fileName)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putStringSet(EXTRA_SNAPSHOT_NAMES_SET, snapshotNames)
        editor.apply()

        return context.getFileStreamPath(fileName).absolutePath
    }

    override fun markSnapshotAsSent(fileName: String) {
        val snapshotNames = sharedPreferences.getStringSet(EXTRA_SNAPSHOT_NAMES_SET, mutableSetOf())
        snapshotNames?.remove(fileName)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putStringSet(EXTRA_SNAPSHOT_NAMES_SET, snapshotNames)
        editor.apply()
    }

    override fun getNotSentSnapshotsPaths(): List<Pair<String, String>> {
        val snapshotsPaths = sharedPreferences.getStringSet(EXTRA_SNAPSHOT_NAMES_SET, mutableSetOf())
        val files = mutableListOf<Pair<String, String>>()
        snapshotsPaths?.let {
            for(path in snapshotsPaths) {
                val fileName = path.substringAfterLast('\\')
                files.add(Pair(fileName, path))
            }
        }
        return files
    }

    override fun replaceNotSentSnapshotsPaths(newPaths: Set<String>) {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putStringSet(EXTRA_SNAPSHOT_NAMES_SET, newPaths)
        editor.apply()
    }

    private fun saveFile(bitmap: ByteArray, fileName: String) {
        val outputStream: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        outputStream.write(bitmap)
        outputStream.flush()
        outputStream.close()
    }
}