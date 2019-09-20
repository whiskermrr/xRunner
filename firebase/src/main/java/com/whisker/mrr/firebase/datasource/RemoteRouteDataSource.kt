package com.whisker.mrr.firebase.datasource

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.*
import com.whisker.mrr.domain.common.bus.RxBus
import com.whisker.mrr.domain.common.bus.event.NetworkStateEvent
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_ROUTES
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_USERS
import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.data.source.RemoteRouteSource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RemoteRouteDataSource
@Inject constructor(
    private val databaseReference: DatabaseReference,
    firebaseAuth: FirebaseAuth
) : BaseSource(firebaseAuth), RemoteRouteSource {

    init {
        RxBus.subscribeSticky(NetworkStateEvent::class.java.name, this, Consumer { event ->
            if(event is NetworkStateEvent) {
                isNetworkAvailable = event.isNetworkAvailable
            }
        })
    }

    override fun saveRoute(route: Route) : Completable {
        return checkConnection().andThen(
            Completable.create { emitter ->
                var routeReference: DatabaseReference = databaseReference
                try {
                    routeReference = getReference()
                } catch (e : FirebaseAuthInvalidUserException) {
                    emitter.onError(e)
                }
                if(route.routeId < 0) {
                    route.routeId = System.currentTimeMillis()
                    routeReference.child(route.routeId.toString()).setValue(route).addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            emitter.onComplete()
                        } else {
                            task.exception?.let {
                                emitter.onError(it)
                            }
                        }
                    }
                } else {
                    routeReference.child(route.routeId.toString()).removeValue().addOnCompleteListener { task ->
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

            }.observeOn(Schedulers.io())
        )
    }

    override fun saveRoutes(routes: List<Route>): Completable {
        if(routes.isEmpty()) return Completable.complete()

        return checkConnection().andThen(Flowable.fromIterable(routes))
            .flatMapCompletable { saveRoute(it) }
    }

    override fun getRoutes() : Single<List<Route>> {
        return checkConnection().andThen(
            Single.create<List<Route>> { emitter ->
                var routeReference: DatabaseReference = databaseReference
                try {
                    routeReference = getReference()
                } catch (e : FirebaseAuthInvalidUserException) {
                    emitter.onError(e)
                }
                routeReference.addListenerForSingleValueEvent(object: ValueEventListener {
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
            }.observeOn(Schedulers.io()).onErrorReturn { emptyList() }
        )
    }

    override fun removeRouteById(routeId: Long): Completable {
        return checkConnection().andThen {
            Completable.create { emitter ->
                var routeReference: DatabaseReference = databaseReference
                try {
                    Log.e("MRRR", routeId.toString())
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
            }.observeOn(Schedulers.io())
        }
    }

    @Throws(FirebaseAuthInvalidUserException::class)
    private fun getReference() : DatabaseReference {
        return databaseReference
            .child(REFERENCE_USERS)
            .child(getUserID())
            .child(REFERENCE_ROUTES)
    }
}
