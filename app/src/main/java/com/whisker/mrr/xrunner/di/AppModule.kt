package com.whisker.mrr.xrunner.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.whisker.mrr.xrunner.App
import com.whisker.mrr.xrunner.data.datasource.LocationDataSource
import com.whisker.mrr.xrunner.data.datasource.UserDataSource
import com.whisker.mrr.xrunner.data.repository.LocationDataRepository
import com.whisker.mrr.xrunner.data.repository.LoginDataRepository
import com.whisker.mrr.xrunner.domain.LocationRepository
import com.whisker.mrr.xrunner.domain.LoginRepository
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
    fun provideLocationDataSOurce(context: Context, firebaseDatabase: FirebaseDatabase) : LocationDataSource {
        return LocationDataSource(context, firebaseDatabase)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(locationDataSource: LocationDataSource, userDataSource: UserDataSource) : LocationRepository {
        return LocationDataRepository(locationDataSource, userDataSource)
    }
}
