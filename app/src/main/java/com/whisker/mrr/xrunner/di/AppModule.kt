package com.whisker.mrr.xrunner.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.whisker.mrr.domain.common.scheduler.*
import com.whisker.mrr.xrunner.App
import com.whisker.mrr.firebase.datasource.*
import com.whisker.mrr.firebase.repository.*
import com.whisker.mrr.domain.interactor.*
import com.whisker.mrr.domain.manager.MusicManager
import com.whisker.mrr.domain.repository.*
import com.whisker.mrr.domain.source.*
import com.whisker.mrr.infrastructure.NetworkStateReceiver
import com.whisker.mrr.infrastructure.source.LocationDataSource
import com.whisker.mrr.infrastructure.source.SnapshotLocalDataSource
import com.whisker.mrr.music.MusicDataManager
import com.whisker.mrr.music.repository.MusicDataRepository
import com.whisker.mrr.xrunner.utils.XRunnerConstants
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Singleton

// TODO: move some stuff to separate modules
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
    fun provideDatabaseSource(database: FirebaseDatabase) : RouteSource {
        return RouteDatabaseSource(database.reference)
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
    fun provideChallengeRepository(database: FirebaseDatabase) : ChallengeRepository {
        return ChallengeDataRepository(database.reference)
    }

    @Provides
    @Singleton
    fun provideCreateAccountInteractor(loginRepository: LoginRepository, userRepository: UserRepository) : CreateAccountInteractor {
        return CreateAccountInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), loginRepository, userRepository)
    }

    @Provides
    @Singleton
    fun provideGetLastKnownLocationInteractor(locationSource: LocationSource) : GetLastKnownLocationInteractor {
        return GetLastKnownLocationInteractor(IOMaybeTransformer(AndroidSchedulers.mainThread()), locationSource)
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
    fun providePauseTrackingInteractor(locationSource: LocationSource) : PauseTrackingInteractor {
        return PauseTrackingInteractor(locationSource)
    }

    @Provides
    @Singleton
    fun provideResumeTrackingInteractor(locationSource: LocationSource) : ResumeTrackingInteractor {
        return ResumeTrackingInteractor(locationSource)
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
    fun provideStartTrackingInteractor(locationSource: LocationSource) : StartTrackingInteractor {
        return StartTrackingInteractor(IOFlowableTransformer(AndroidSchedulers.mainThread()), locationSource)
    }

    @Provides
    @Singleton
    fun provideStopTrackingInteractor(locationSource: LocationSource) : StopTrackingInteractor {
        return StopTrackingInteractor(locationSource)
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
    fun provideSaveChallengeInteractor(authSource: AuthSource, challengeRepository: ChallengeRepository) : SaveChallengeInteractor {
        return SaveChallengeInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), authSource, challengeRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateChallengeInteractor(authSource: AuthSource, challengeRepository: ChallengeRepository) : UpdateChallengesInteractor {
        return UpdateChallengesInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), authSource, challengeRepository)
    }

    @Provides
    @Singleton
    fun provideGetChallengeInteractor(authSource: AuthSource, challengeRepository: ChallengeRepository) : GetChallengesInteractor {
        return GetChallengesInteractor(IOFlowableTransformer(AndroidSchedulers.mainThread()), authSource, challengeRepository)
    }

    @Provides
    @Singleton
    fun provideGetActiveChallengeInteractor(authSource: AuthSource, challengeRepository: ChallengeRepository) : GetActiveChallengesInteractor {
        return GetActiveChallengesInteractor(IOSingleTransformer(AndroidSchedulers.mainThread()), authSource, challengeRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateUserStatsInteractor(authSource: AuthSource, userRepository: UserRepository) : UpdateUserStatsInteractor {
        return UpdateUserStatsInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), authSource, userRepository)
    }

    @Provides
    @Singleton
    fun provideMusicRepository(context: Context) : MusicRepository {
        return MusicDataRepository(context)
    }

    @Provides
    @Singleton
    fun provideMusicManager(context: Context) : MusicManager {
        return MusicDataManager(context)
    }

    @Provides
    @Singleton
    fun provideGetSongsInteractor(musicRepository: MusicRepository) : GetSongsInteractor {
        return GetSongsInteractor(IOSingleTransformer(AndroidSchedulers.mainThread()), musicRepository)
    }

    @Provides
    @Singleton
    fun provideGetAlbumsInteractor(musicRepository: MusicRepository) : GetAlbumsInteractor {
        return GetAlbumsInteractor(IOSingleTransformer(AndroidSchedulers.mainThread()), musicRepository)
    }

    @Provides
    @Singleton
    fun provideSetSongsInteractor(musicManager: MusicManager) : SetSongsInteractor {
        return SetSongsInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), musicManager)
    }

    @Provides
    @Singleton
    fun providePlayMusicInteractor(musicManager: MusicManager) : PlayMusicInteractor {
        return PlayMusicInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), musicManager)
    }

    @Provides
    @Singleton
    fun provideNextSongInteractor(musicManager: MusicManager) : NextSongInteractor {
        return NextSongInteractor(IOSingleTransformer(AndroidSchedulers.mainThread()), musicManager)
    }

    @Provides
    @Singleton
    fun providePreviousSongInteractor(musicManager: MusicManager) : PreviousSongInteractor {
        return PreviousSongInteractor(IOSingleTransformer(AndroidSchedulers.mainThread()), musicManager)
    }

    @Provides
    @Singleton
    fun providePauseMusicInteractor(musicManager: MusicManager) : PauseMusicInteractor {
        return PauseMusicInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), musicManager)
    }

    @Provides
    @Singleton
    fun provideStopMusicInteractor(musicManager: MusicManager) : StopMusicInteractor {
        return StopMusicInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), musicManager)
    }
}
