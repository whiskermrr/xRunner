package com.whisker.mrr.xrunner.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.whisker.mrr.xrunner.data.datasource.common.DataConstants.REFERENCE_ACHIEVEMENTS
import com.whisker.mrr.xrunner.data.datasource.common.DataConstants.REFERENCE_USERS
import com.whisker.mrr.xrunner.domain.model.Achievement
import com.whisker.mrr.xrunner.domain.repository.AchievementsRepository
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

class AchievementsDataRepository(private val databaseReference: DatabaseReference) : AchievementsRepository {

    override fun saveAchievement(userId: String, achievement: Achievement): Completable {
        val reference = getReference(userId)
        achievement.id = reference.push().key!!

        return Completable.create { emitter ->
            reference.child(achievement.id).setValue(achievement).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    emitter.onComplete()
                } else {
                    task.exception?.let {
                        emitter.onError(it)
                    }
                }
            }
        }
    }

    override fun updateAchievements(userId: String, achievements: List<Achievement>): Completable {
        // TODO: logic
        return Completable.complete()
    }

    override fun getAchievements(userId: String): Single<List<Achievement>> {
        return Single.create { emitter ->
            getReference(userId).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val achievements = mutableListOf<Achievement>()
                    dataSnapshot.children.forEach { child ->
                        child.getValue(Achievement::class.java)?.let {
                            achievements.add(it)
                        }
                    }
                    emitter.onSuccess(achievements)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onError(databaseError.toException())
                }
            })
        }
    }

    override fun getActiveAchievements(userId: String) : Single<List<Achievement>> {
        return Single.create { emitter ->
            getReference(userId).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val currentTime = Date().time
                    val activeAchievements = mutableListOf<Achievement>()
                    dataSnapshot.children.forEach { child ->
                        child.getValue(Achievement::class.java)?.let { achievement ->
                            achievement.deadline?.let {
                                if(it > currentTime && !achievement.isFinished)
                                    activeAchievements.add(achievement)
                            } ?: achievement.run {
                                activeAchievements.add(this)
                            }
                        }
                    }
                    emitter.onSuccess(activeAchievements)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onError(databaseError.toException())
                }
            })
        }
    }

    private fun getReference(userId: String) : DatabaseReference {
        return databaseReference
            .child(REFERENCE_USERS)
            .child(userId)
            .child(REFERENCE_ACHIEVEMENTS)
    }
}