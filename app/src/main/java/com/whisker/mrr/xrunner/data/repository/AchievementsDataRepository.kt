package com.whisker.mrr.xrunner.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
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
        val reference = databaseReference
            .child(REFERENCE_USERS)
            .child(userId)
            .child(REFERENCE_ACHIEVEMENTS)
            .orderByValue()
    }

    override fun getAchievements(userId: String): Single<List<Achievement>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getActiveAchievements(userId: String) : Single<List<Achievement>> {

    }

    private fun getActiveAchievements(reference: DatabaseReference) : Single<List<Achievement>> {
        return Single.create { emitter ->
            reference.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()) {

                    }
                }

                override fun onCancelled(dataSnapshot: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        }
    }
}