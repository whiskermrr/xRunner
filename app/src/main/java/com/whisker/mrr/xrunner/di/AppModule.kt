package com.whisker.mrr.xrunner.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.whisker.mrr.xrunner.App
import com.whisker.mrr.xrunner.data.datasource.*
import com.whisker.mrr.xrunner.data.repository.*
import com.whisker.mrr.xrunner.domain.interactor.*
import com.whisker.mrr.xrunner.domain.repository.*
import com.whisker.mrr.xrunner.domain.source.*
import com.whisker.mrr.xrunner.infrastructure.NetworkStateReceiver
import com.whisker.mrr.xrunner.domain.common.scheduler.ComputationCompletableTransformer
import com.whisker.mrr.xrunner.domain.common.scheduler.IOCompletableTransformer
import com.whisker.mrr.xrunner.domain.common.scheduler.IOFlowableTransformer
import com.whisker.mrr.xrunner.domain.common.scheduler.IOSingleTransformer
import com.whisker.mrr.xrunner.utils.XRunnerConstants
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
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
        FirebaseDatabase.getInstance().reference.keepSynced(true)
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage() : FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideLoginRepository(authDataSource: AuthSource) : LoginRepository {
        return LoginDataRepository(authDataSource)
    }

    @Provides
    @Singleton
    fun provideUserDataSource(firebaseAuth: FirebaseAuth) : AuthSource {
        return AuthDataSource(firebaseAuth)
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
    fun provideAchievementsRepository(database: FirebaseDatabase) : AchievementsRepository {
        return AchievementsDataRepository(database.reference)
    }

    @Provides
    @Singleton
    fun provideCreateAccountInteractor(loginRepository: LoginRepository, userRepository: UserRepository) : CreateAccountInteractor {
        return CreateAccountInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), loginRepository, userRepository)
    }

    @Provides
    @Singleton
    fun provideGetLastKnownLocationInteractor(locationRepository: LocationRepository) : GetLastKnownLocationInteractor {
        return GetLastKnownLocationInteractor(IOSingleTransformer(AndroidSchedulers.mainThread()), locationRepository)
    }

    @Provides
    @Singleton
    fun provideGetRouteListInteractor(routeRepository: RouteRepository, authSource: AuthSource) : GetRouteListInteractor {
        return GetRouteListInteractor(IOFlowableTransformer(AndroidSchedulers.mainThread()), routeRepository, authSource)
    }

    @Provides
    @Singleton
    fun provideLoginInteractor(loginRepository: LoginRepository) : LoginInteractor {
        return LoginInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), loginRepository)
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
    fun provideSaveRouteInteractor(routeRepository: RouteRepository, authSource: AuthSource) : SaveRouteInteractor {
        return SaveRouteInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), routeRepository, authSource)
    }

    @Provides
    @Singleton
    fun provideSaveSnapshotInteractor(routeRepository: RouteRepository) : SaveSnapshotInteractor {
        return SaveSnapshotInteractor(ComputationCompletableTransformer(AndroidSchedulers.mainThread()), routeRepository)
    }

    @Provides
    @Singleton
    fun provideStartTrackingInteractor(locationRepository: LocationRepository) : StartTrackingInteractor {
        return StartTrackingInteractor(IOFlowableTransformer(AndroidSchedulers.mainThread()), locationRepository)
    }

    @Provides
    @Singleton
    fun provideStopTrackingInteractor(locationRepository: LocationRepository) : StopTrackingInteractor {
        return StopTrackingInteractor(locationRepository)
    }

    @Provides
    @Singleton
    fun provideRemoveRouteInteractor(routeRepository: RouteRepository, authSource: AuthSource) : RemoveRouteInteractor {
        return RemoveRouteInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), routeRepository, authSource)
    }

    @Provides
    @Singleton
    fun provideGetUserStatsInteractor(userRepository: UserRepository, authSource: AuthSource) : GetUserStatsInteractor {
        return GetUserStatsInteractor(IOSingleTransformer(AndroidSchedulers.mainThread()), userRepository, authSource)
    }

    @Provides
    @Singleton
    fun provideUpdateUserStatsInteractor(userRepository: UserRepository, authSource: AuthSource) : UpdateUserStatsInteractor {
        return UpdateUserStatsInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), userRepository, authSource)
    }

    @Provides
    @Singleton
    fun provideSaveAchievementInteractor(authSource: AuthSource, achievementsRepository: AchievementsRepository) : SaveAchievementInteractor {
        return SaveAchievementInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), authSource, achievementsRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateAchievementsInteractor(authSource: AuthSource, achievementsRepository: AchievementsRepository) : UpdateAchievementsInteractor {
        return UpdateAchievementsInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), authSource, achievementsRepository)
    }

    @Provides
    @Singleton
    fun provideGetAchievementsInteractor(authSource: AuthSource, achievementsRepository: AchievementsRepository) : GetAchievementsInteractor {
        return GetAchievementsInteractor(IOSingleTransformer(AndroidSchedulers.mainThread()), authSource, achievementsRepository)
    }
}
