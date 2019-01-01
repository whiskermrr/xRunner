package com.whisker.mrr.xrunner.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.whisker.mrr.xrunner.App
import com.whisker.mrr.xrunner.data.datasource.*
import com.whisker.mrr.xrunner.data.repository.LocationDataRepository
import com.whisker.mrr.xrunner.data.repository.LoginDataRepository
import com.whisker.mrr.xrunner.data.repository.RouteDataRepository
import com.whisker.mrr.xrunner.data.repository.UserDataRepository
import com.whisker.mrr.xrunner.domain.interactor.*
import com.whisker.mrr.xrunner.domain.repository.LocationRepository
import com.whisker.mrr.xrunner.domain.repository.LoginRepository
import com.whisker.mrr.xrunner.domain.repository.RouteRepository
import com.whisker.mrr.xrunner.domain.repository.UserRepository
import com.whisker.mrr.xrunner.domain.source.*
import com.whisker.mrr.xrunner.infrastructure.NetworkStateReceiver
import com.whisker.mrr.xrunner.presentation.common.ComputationCompletableTransformer
import com.whisker.mrr.xrunner.presentation.common.IOCompletableTransformer
import com.whisker.mrr.xrunner.presentation.common.IOFlowableTransformer
import com.whisker.mrr.xrunner.presentation.common.IOSingleTransformer
import com.whisker.mrr.xrunner.utils.XRunnerConstants
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
    fun provideSharedPreferences(context: Context) : SharedPreferences {
        return context.getSharedPreferences(XRunnerConstants.XRUNNER_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideNetworkStateReceiver() : NetworkStateReceiver {
        return NetworkStateReceiver()
    }

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
    fun provideFirebaseStorage() : FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideLoginRepository(userDataSource: UserSource) : LoginRepository {
        return LoginDataRepository(userDataSource)
    }

    @Provides
    @Singleton
    fun provideUserDataSource(firebaseAuth: FirebaseAuth) : UserSource {
        return UserDataSource(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideLocationDataSource(context: Context) : LocationSource {
        return LocationDataSource(context)
    }

    @Provides
    @Singleton
    fun provideDatabaseSource(firebaseDatabase: FirebaseDatabase) : RouteSource {
        return RouteDatabaseSource(firebaseDatabase)
    }

    @Provides
    @Singleton
    fun provideSnapshotLocalSource(context: Context, sharedPreferences: SharedPreferences) : SnapshotLocalSource {
        return SnapshotLocalDataSource(context, sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideSnapshotRemoteSource(firebaseStorage: FirebaseStorage) : SnapshotRemoteSource {
        return SnapshotRemoteDataSource(firebaseStorage)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(locationDataSource: LocationSource) : LocationRepository {
        return LocationDataRepository(locationDataSource)
    }

    @Provides
    @Singleton
    fun provideRouteRepository(
        routeDatabaseSource: RouteSource,
        snapshotRemoteDataSource: SnapshotRemoteSource,
        snapshotLocalDataSource: SnapshotLocalSource
    ) : RouteRepository {
        return RouteDataRepository(routeDatabaseSource, snapshotRemoteDataSource, snapshotLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(database: FirebaseDatabase) : UserRepository {
        return UserDataRepository(database.reference)
    }

    @Provides
    @Singleton
    fun provideCreateAccountInteractor(loginRepository: LoginRepository, userRepository: UserRepository) : CreateAccountInteractor {
        return CreateAccountInteractor(IOCompletableTransformer(), loginRepository, userRepository)
    }

    @Provides
    @Singleton
    fun provideGetLastKnownLocationInteractor(locationRepository: LocationRepository) : GetLastKnownLocationInteractor {
        return GetLastKnownLocationInteractor(IOSingleTransformer(), locationRepository)
    }

    @Provides
    @Singleton
    fun provideGetRouteListInteractor(routeRepository: RouteRepository, userSource: UserSource) : GetRouteListInteractor {
        return GetRouteListInteractor(IOFlowableTransformer(), routeRepository, userSource)
    }

    @Provides
    @Singleton
    fun provideLoginInteractor(loginRepository: LoginRepository) : LoginInteractor {
        return LoginInteractor(IOCompletableTransformer(), loginRepository)
    }

    @Provides
    @Singleton
    fun providePauseTrackingInteractor(locationRepository: LocationRepository) : PauseTrackingInteractor {
        return PauseTrackingInteractor(locationRepository)
    }

    @Provides
    @Singleton
    fun provideResumeTrackingInteractor(locationRepository: LocationRepository) : ResumeTrackingInteractor {
        return ResumeTrackingInteractor(locationRepository)
    }

    @Provides
    @Singleton
    fun provideSaveRouteInteractor(routeRepository: RouteRepository, userSource: UserSource, userRepository: UserRepository) : SaveRouteInteractor {
        return SaveRouteInteractor(IOCompletableTransformer(), routeRepository, userSource, userRepository)
    }

    @Provides
    @Singleton
    fun provideSaveSnapshotInteractor(routeRepository: RouteRepository) : SaveSnapshotInteractor {
        return SaveSnapshotInteractor(ComputationCompletableTransformer(), routeRepository)
    }

    @Provides
    @Singleton
    fun provideStartTrackingInteractor(locationRepository: LocationRepository) : StartTrackingInteractor {
        return StartTrackingInteractor(IOFlowableTransformer(), locationRepository)
    }

    @Provides
    @Singleton
    fun provideStopTrackingInteractor(locationRepository: LocationRepository) : StopTrackingInteractor {
        return StopTrackingInteractor(locationRepository)
    }

    @Provides
    @Singleton
    fun provideRemoveRouteInteractor(routeRepository: RouteRepository, userSource: UserSource) : RemoveRouteInteractor {
        return RemoveRouteInteractor(IOCompletableTransformer(), routeRepository, userSource)
    }

    @Provides
    @Singleton
    fun provideGetUserStatsInteractor(userRepository: UserRepository, userSource: UserSource) : GetUserStatsInteractor {
        return GetUserStatsInteractor(IOSingleTransformer(), userRepository, userSource)
    }
}
