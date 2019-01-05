package com.whisker.mrr.xrunner.data.repository

import com.google.firebase.database.DatabaseReference
import com.whisker.mrr.xrunner.data.datasource.common.DataConstants.REFERENCE_ACHIEVEMENTS
import com.whisker.mrr.xrunner.data.datasource.common.DataConstants.REFERENCE_USERS
import com.whisker.mrr.xrunner.domain.model.Achievement
import com.whisker.mrr.xrunner.domain.model.RouteStatsEntity
import com.whisker.mrr.xrunner.domain.repository.AchievementsRepository
import io.reactivex.Completable
import io.reactivex.Single

class AchievementsDataRepository(private val databaseReference: DatabaseReference) : AchievementsRepository {

    override fun saveAchievement(userId: String, achievement: Achievement): Completable {
        val reference = databaseReference
            .child(REFERENCE_USERS)
            .child(userId)
            .child(REFERENCE_ACHIEVEMENTS)

        achievement.id = reference.push().key!!

        return Completable.create { emitter ->
            reference.child(achievement.id).setValue(achievement).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    emitter.onComplete()
                } else if(task.exception != null) {
                    emitter.onError(task.exception!!)
                }
            }
        }
    }

    override fun updateAchievements(userId: String, stats: RouteStatsEntity): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAchievements(userId: String): Single<List<Achievement>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}