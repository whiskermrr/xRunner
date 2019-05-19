package com.whisker.mrr.firebase.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.*
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_ROUTES
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_USERS
import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.domain.source.RemoteRouteSource
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class RemoteRouteDatabaseSource
@Inject constructor(
    private val databaseReference: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
) : RemoteRouteSource {

    override fun saveRoute(route: Route) : Single<Long> {
        return Single.create { emitter ->
            var routeReference: DatabaseReference = databaseReference
            try {
                routeReference = getReference()
            } catch (e : FirebaseAuthInvalidUserException) {
                emitter.onError(e)
            }

            route.routeId = System.currentTimeMillis()
            routeReference.child(route.routeId.toString()).setValue(route).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    emitter.onSuccess(route.routeId)
                } else {
                    task.exception?.let {
                        emitter.onError(it)
                    }
                }
            }
        }
    }

    override fun saveRoutes(routes: List<Route>): Single<List<Long>> {
        val singles = mutableListOf<Single<Long>>()
        for(route in routes) {
            singles.add(saveRoute(route))
        }
        return Single.zip(singles) { args -> Arrays.asList(args) as List<Long> }
    }

    override fun getRoutes() : Single<List<Route>> {
        return Single.create { emitter ->
            databaseReference.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val routes = mutableListOf<Route>()
                    dataSnapshot.children.forEach { child ->
                        val route = child.getValue(Route::class.java)
                        route?.let {
                            routes.add(it)
                        }
                    }
                    emitter.onSuccess(routes)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onError(databaseError.toException())
                }
            })
        }
    }

    override fun removeRouteById(routeId: Long): Completable {
        return Completable.create { emitter ->
            var routeReference: DatabaseReference = databaseReference
            try {
                routeReference = getReference().child(routeId.toString())
            } catch (e: FirebaseAuthInvalidUserException) {
                emitter.onError(e)
            }
            routeReference.removeValue().addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    emitter.onComplete()
                } else {
                    task.exception?.let {
                        emitter.onError(it)
                    }
                }
            }.addOnFailureListener {
                emitter.onError(it)
            }
        }
    }

    @Throws(FirebaseAuthInvalidUserException::class)
    private fun getReference() : DatabaseReference {
        var userUID = ""
        firebaseAuth.currentUser?.uid?.let { userUID = it } ?: kotlin.run { throw FirebaseAuthInvalidUserException("", "") }
        return databaseReference
            .child(REFERENCE_USERS)
            .child(userUID)
            .child(REFERENCE_ROUTES)
    }
}
