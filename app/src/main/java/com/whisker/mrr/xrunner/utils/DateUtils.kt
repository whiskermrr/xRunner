package com.whisker.mrr.xrunner.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object {
        const val EEE_MMM_d_yyyy = "EEE, MMM d, yyyy, HH:mm"
        const val MMM_yyyy = "MMM yyyy"

        fun formatDate(date: Long, format: String) : String {
            val dateObj = Date(date)
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            return sdf.format(dateObj)
        }

        fun getFirstDayOfTheMonthInMillis(time: Long) : Long {
            val calendar = Calendar.getInstance(TimeZone.getDefault())
            calendar.timeInMillis = time
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            return calendar.timeInMillis
        }
    }
}