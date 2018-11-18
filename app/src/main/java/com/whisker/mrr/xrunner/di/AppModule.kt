package com.whisker.mrr.xrunner.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.whisker.mrr.xrunner.App
import com.whisker.mrr.xrunner.data.datasource.RouteDatabaseSource
import com.whisker.mrr.xrunner.data.datasource.LocationDataSource
import com.whisker.mrr.xrunner.data.datasource.UserDataSource
import com.whisker.mrr.xrunner.data.repository.LocationDataRepository
import com.whisker.mrr.xrunner.data.repository.LoginDataRepository
import com.whisker.mrr.xrunner.domain.repository.LocationRepository
import com.whisker.mrr.xrunner.domain.repository.LoginRepository
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
        return FirebaseDatabase.getInstance()
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
    fun provideLocationRepository(
        locationDataSource: LocationDataSource,
        userDataSource: UserDataSource,
        routeDatabaseSource: RouteDatabaseSource) : LocationRepository {
        return LocationDataRepository(locationDataSource, userDataSource, routeDatabaseSource)
    }
}
