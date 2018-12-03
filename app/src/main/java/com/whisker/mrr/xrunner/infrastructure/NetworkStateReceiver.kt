package com.whisker.mrr.xrunner.infrastructure

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import com.whisker.mrr.xrunner.domain.bus.RxBus
import com.whisker.mrr.xrunner.domain.bus.event.NetworkStateEvent

class NetworkStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        val isNetworkAvailable = (networkInfo != null && networkInfo.isConnected)
        RxBus.publishSticky(NetworkStateEvent(isNetworkAvailable))
        if(isNetworkAvailable) {
            startSyncService(context)
        }
    }

    private fun startSyncService(context: Context) {
        val syncIntent = Intent(SnapshotSyncService.ACTION_SYNC_SNAPSHOTS)
        syncIntent.setPackage(context.packageName)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(syncIntent)
        } else {
            context.startService(syncIntent)
        }
    }
}