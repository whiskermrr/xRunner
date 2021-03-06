package com.whisker.mrr.infrastructure.synchronization.exception

import java.util.*

class WorkFailedException(workId: UUID) : WorkException("work with id $workId failed")

class WorkCancelledException(workId: UUID) : WorkException("work with id $workId cancelled")

open class WorkException(message: String) : Throwable(message)