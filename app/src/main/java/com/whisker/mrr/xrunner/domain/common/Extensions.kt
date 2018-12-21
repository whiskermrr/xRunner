package com.whisker.mrr.xrunner.domain.common

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