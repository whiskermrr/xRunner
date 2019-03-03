package com.whisker.mrr.domain.common

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

inline fun <reified T> T.TAG(): String = T::class.java.simpleName

inline fun<T: Any, R: Any> Collection<T?>.whenAllNotNull(list: (List<T>) -> R) {
    if(this.all { it != null }) {
        list(this.filterNotNull())
    }
}

inline fun<A, B, R> whenBothNotNull(a: A?, b: B?, block: (A, B) -> R) {
    if(a != null && b != null) {
        block(a, b)
    }
}

fun Date.getFirstDayOfTheMonthInMillis() : Long {
    val calendar = Calendar.getInstance(TimeZone.getDefault())
    calendar.timeInMillis = this.time
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis
}

fun Date.formatDate(format: String) : String {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    return sdf.format(this)
}

fun String.toLongDate(format: String) : Long? {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    return try {
        val date = sdf.parse(this)
        date.time
    } catch (e: Exception) {
        null
    }
}

fun Date.daysBetween(date: Date) : Int {
    return ((this.time - date.time) / (1000 * 60 * 60 * 24)).toInt() + 1
}