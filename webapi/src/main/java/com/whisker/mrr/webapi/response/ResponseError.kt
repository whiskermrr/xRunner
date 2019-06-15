package com.whisker.mrr.webapi.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class ResponseError(
    @SerializedName("Messages") val messages: Array<String>,
    @SerializedName("ErrorCode")  val errorCode: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ResponseError

        if (!Arrays.equals(messages, other.messages)) return false
        if (errorCode != other.errorCode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(messages)
        result = 31 * result + errorCode
        return result
    }
}