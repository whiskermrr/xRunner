package com.whisker.mrr.xrunner.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object {
        const val EEE_MMM_d_yyyy = "EEE, MMM d, yyyy, HH:mm"

        fun formatDate(date: Long, format: String) : String {
            val dateObj = Date(date)
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            return sdf.format(dateObj)
        }
    }
}