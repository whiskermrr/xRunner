package com.whisker.mrr.xrunner.domain.interactor

import android.graphics.Bitmap
import com.whisker.mrr.xrunner.domain.repository.RouteRepository
import com.whisker.mrr.xrunner.domain.usecase.CompletableUseCase
import com.whisker.mrr.xrunner.domain.whenBothNotNull
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import java.lang.IllegalArgumentException

class SaveSnapshotInteractor(transformer: CompletableTransformer, private val routeRepository: RouteRepository)
: CompletableUseCase(transformer){

    companion object {
        private const val PARAM_BITMAP = "param_bitmap"
        private const val PARAM_FILE_NAME = "param_file_name"
    }

    fun saveSnapshot(bitmap: Bitmap, fileName: String) : Completable {
        val data = HashMap<String, Any>()
        data[PARAM_BITMAP] = bitmap
        data[PARAM_FILE_NAME] = fileName
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val bitmapData = data?.get(PARAM_BITMAP)
        val fileNameData = data?.get(PARAM_FILE_NAME)

        whenBothNotNull(bitmapData, fileNameData) { bitmap, fileName ->
            return routeRepository.saveSnapshot(bitmap as Bitmap, fileName as String)
        }

        return Completable.error(IllegalArgumentException("Parameters @bitmap and @fileName must be provided."))
    }
}