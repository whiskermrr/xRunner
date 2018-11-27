package com.whisker.mrr.xrunner.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.whisker.mrr.xrunner.App
import com.whisker.mrr.xrunner.data.datasource.*
import com.whisker.mrr.xrunner.data.repository.LocationDataRepository
import com.whisker.mrr.xrunner.data.repository.LoginDataRepository
import com.whisker.mrr.xrunner.data.repository.RouteDataRepository
import com.whisker.mrr.xrunner.domain.repository.LocationRepository
import com.whisker.mrr.xrunner.domain.repository.LoginRepository
import com.whisker.mrr.xrunner.domain.repository.RouteRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideContext(app: App) : Context = app.applicationContext

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabase() : FirebaseDatabase {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore() : FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideLoginRepository(firebaseAuth: FirebaseAuth) : LoginRepository {
        return LoginDataRepository(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideUserDataSource(firebaseAuth: FirebaseAuth) : UserDataSource {
        return UserDataSource(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideLocationDataSource(context: Context) : LocationDataSource {
        return LocationDataSource(context)
    }

    @Provides
    @Singleton
    fun provideDatabaseSource(firebaseDatabase: FirebaseDatabase) : RouteDatabaseSource {
        return RouteDatabaseSource(firebaseDatabase)
    }

    @Provides
    @Singleton
    fun provideSnapshotLocalSource(context: Context) : SnapshotLocalSource {
        return SnapshotLocalSource(context)
    }

    @Provides
    @Singleton
    fun provideSnapshotRemoteSource(firebaseStorage: FirebaseStorage) : SnapshotRemoteSource {
        return SnapshotRemoteSource(firebaseStorage)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(locationDataSource: LocationDataSource) : LocationRepository {
        return LocationDataRepository(locationDataSource)
    }

    @Provides
    @Singleton
    fun provideRouteRepository(
                userDataSource: UserDataSource,
                routeDatabaseSource: RouteDatabaseSource,
                snapshotRemoteSource: SnapshotRemoteSource,
                snapshotLocalSource: SnapshotLocalSource
    ) : RouteRepository {
        return RouteDataRepository(userDataSource, routeDatabaseSource, snapshotRemoteSource, snapshotLocalSource)
    }
}
