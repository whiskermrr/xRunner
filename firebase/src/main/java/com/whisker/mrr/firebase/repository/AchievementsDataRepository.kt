package com.whisker.mrr.firebase.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_ACHIEVEMENTS
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_USERS
import com.whisker.mrr.domain.model.Achievement
import com.whisker.mrr.domain.repository.AchievementsRepository
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*
import kotlin.collections.HashMap

class AchievementsDataRepository(private val databaseReference: DatabaseReference) : AchievementsRepository {

    companion object {
        const val DB_REFERENCE_IS_FINISHED = "isFinished"
        const val DB_REFERENCE_PROGRESS = "progress"
    }

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
        val reference = getReference(userId)
        val completableList = mutableListOf<Completable>()
        for(achievement in achievements) {
            completableList.add(
                Completable.create { emitter ->
                    val map = HashMap<String, Any>()
                    map[DB_REFERENCE_PROGRESS] = achievement.progress
                    map[DB_REFERENCE_IS_FINISHED] = achievement.isFinished
                    reference.child(achievement.id).updateChildren(map).addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            emitter.onComplete()
                        } else {
                            task.exception?.let {
                                emitter.onError(it)
                            }
                        }
                    }
                }
            )
        }

        return Completable.concat(completableList)
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
                                if(!this.isFinished)
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