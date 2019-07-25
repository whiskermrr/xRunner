package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.usecase.CompletableUseCase
import com.whisker.mrr.domain.common.whenBothNotNull
import com.whisker.mrr.domain.repository.SnapshotRepository
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import java.lang.IllegalArgumentException

class SaveSnapshotInteractor(
    transformer: CompletableTransformer,
    private val snapshotRepository: SnapshotRepository
) : CompletableUseCase(transformer){

    companion object {
        private const val PARAM_BITMAP = "param_bitmap"
        private const val PARAM_FILE_NAME = "param_file_name"
    }

    fun saveSnapshot(bitmap: ByteArray, fileName: String) : Completable {
        val data = HashMap<String, Any>()
        data[PARAM_BITMAP] = bitmap
        data[PARAM_FILE_NAME] = fileName
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val bitmapData = data?.get(PARAM_BITMAP)
        val fileNameData = data?.get(PARAM_FILE_NAME)

        whenBothNotNull(bitmapData, fileNameData) { bitmap, fileName ->
            return snapshotRepository.saveSnapshot(bitmap as ByteArray, fileName as String)
        }

        return Completable.error(IllegalArgumentException("Parameters @bitmap and @fileName must be provided."))
    }
}