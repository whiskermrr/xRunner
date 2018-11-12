package com.whisker.mrr.xrunner.utils

import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import java.util.*
import kotlin.concurrent.timerTask

class RunnerTimer {

    private val runTime = MutableLiveData<String>()
    private lateinit var timer: Timer
    private var startTime: Long = 0L
    private var pauseTime: Long = 0L
    private var elapsedTime: Long  = 0L

    fun startTimer() {
        startTime = SystemClock.elapsedRealtime()
        start()
    }

    fun start() {
        timer = Timer()
        timer.scheduleAtFixedRate(timerTask {
            elapsedTime = SystemClock.elapsedRealtime() - startTime
            val seconds = elapsedTime / 1000 % 60
            val minutes = elapsedTime / (1000 * 60) % 60
            val hours = elapsedTime / (1000 * 60 * 60) % 24

            val stringTime = if(hours == 0L) {
                String.format("%02d:%02d", minutes, seconds)
            } else {
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }
            runTime.postValue(stringTime)
        }, 1000, 1000)
    }

    fun stop() {
        timer.cancel()
    }

    fun pause() {
        timer.cancel()
        pauseTime = SystemClock.elapsedRealtime()
    }

    fun resume() {
        startTime = startTime + SystemClock.elapsedRealtime() - pauseTime
        start()
    }

    fun getStartTime() = startTime

    fun getElapsedTime() = elapsedTime

    fun getTime() = runTime
}