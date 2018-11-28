package com.whisker.mrr.xrunner.infrastructure

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.whisker.mrr.xrunner.domain.bus.RxBus
import com.whisker.mrr.xrunner.domain.bus.event.NetworkStateEvent

class NetworkStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        RxBus.publish(NetworkStateEvent((networkInfo != null && networkInfo.isConnected)))
    }
}