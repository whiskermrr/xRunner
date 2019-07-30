package com.whisker.mrr.xrunner.di

import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.whisker.mrr.room.DbRunner
import com.whisker.mrr.data.repository.ChallengeDataRepository
import com.whisker.mrr.data.repository.RouteDataRepository
import com.whisker.mrr.data.repository.UserDataRepository
import com.whisker.mrr.room.source.LocalChallengeDataSource
import com.whisker.mrr.room.source.LocalRouteDataSource
import com.whisker.mrr.room.source.LocalUserDataSource
import com.whisker.mrr.domain.common.scheduler.*
import com.whisker.mrr.xrunner.App
import com.whisker.mrr.domain.interactor.*
import com.whisker.mrr.domain.manager.LocationManager
import com.whisker.mrr.domain.manager.MusicManager
import com.whisker.mrr.domain.repository.*
import com.whisker.mrr.data.source.*
import com.whisker.mrr.domain.manager.SynchronizationManager
import com.whisker.mrr.firebase.datasource.*
import com.whisker.mrr.firebase.repository.LoginDataRepository
import com.whisker.mrr.infrastructure.NetworkStateReceiver
import com.whisker.mrr.infrastructure.di.WorkManagerModule
import com.whisker.mrr.infrastructure.source.LocationDataManager
import com.whisker.mrr.infrastructure.source.SnapshotLocalDataSource
import com.whisker.mrr.infrastructure.synchronization.SynchronizationDataManager
import com.whisker.mrr.music.MusicDataManager
import com.whisker.mrr.music.repository.MusicDataRepository
import com.whisker.mrr.room.dao.*
import com.whisker.mrr.xrunner.utils.XRunnerConstants
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Singleton

// TODO: move some stuff to separate modules
@Module(includes = [ViewModelModule::class, WorkManagerModule::class])
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
    fun provideWorkManager() : WorkManager {
        return WorkManager.getInstance()
    }

    @Provides
    @Singleton
    fun provideSynchronizationManager(workManager: WorkManager) : SynchronizationManager {
        return SynchronizationDataManager(workManager)
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(context: Context) : DbRunner {
        return DbRunner.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideChallengeDao(db: DbRunner) : ChallengeDao {
        return db.challengeDao()
    }

    @Provides
    @Singleton
    fun provideChallengeProgressDao(db: DbRunner) : ChallengeProgressDao {
        return db.challengeProgressDao()
    }

    @Provides
    @Singleton
    fun provideRouteDao(db: DbRunner) : RouteDao {
        return db.routeDao()
    }

    @Provides
    @Singleton
    fun provideUserStatsDao(db: DbRunner) : UserStatsDao {
        return db.userStatsDao()
    }

    @Provides
    @Singleton
    fun provideUserStatsProgressDao(db: DbRunner) : UserStatsProgressDao {
        return db.userStatsProgressDao()
    }

    @Provides
    @Singleton
    fun providePreferencesDao(db: DbRunner) : PreferencesDao {
        return db.preferencesDao()
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
    fun provideLocationDataSource(context: Context) : LocationManager {
        return LocationDataManager(context)
    }

    @Provides
    @Singleton
    fun provideRemoteRouteSource(database: FirebaseDatabase, firebaseAuth: FirebaseAuth) : RemoteRouteSource {
        return RemoteRouteDataSource(database.reference, firebaseAuth)
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
    fun provideLocalRouteSource(routeDao: RouteDao) : LocalRouteSource {
        return LocalRouteDataSource(routeDao)
    }

    @Provides
    @Singleton
    fun provideLocalChallengeSource(challengeDao: ChallengeDao, challengeProgressDao: ChallengeProgressDao) : LocalChallengeSource {
        return LocalChallengeDataSource(challengeDao, challengeProgressDao)
    }

    @Provides
    @Singleton
    fun provideLocalUserSource(userStatsDao: UserStatsDao, userStatsProgressDao: UserStatsProgressDao) : LocalUserSource {
        return LocalUserDataSource(userStatsDao, userStatsProgressDao)
    }

    @Provides
    @Singleton
    fun provideRouteRepository(
        localRouteSource: LocalRouteSource,
        remoteRouteSource: RemoteRouteSource,
        snapshotRemoteDataSource: SnapshotRemoteSource,
        snapshotLocalDataSource: SnapshotLocalSource
    ) : RouteRepository {
        return RouteDataRepository(localRouteSource, remoteRouteSource, snapshotRemoteDataSource, snapshotLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideChallengeRepository(localChallengeSource: LocalChallengeSource, remoteChallengeSource: RemoteChallengeSource) : ChallengeRepository {
        return ChallengeDataRepository(localChallengeSource, remoteChallengeSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(localUserSource: LocalUserSource, remoteUserSource: RemoteUserSource) : UserRepository {
        return UserDataRepository(localUserSource, remoteUserSource)
    }


    @Provides
    @Singleton
    fun provideRemoteUserSource(database: FirebaseDatabase,  firebaseAuth: FirebaseAuth) : RemoteUserSource {
        return RemoteUserDataSource(database.reference, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideRemoteChallengeSource(database: FirebaseDatabase,  firebaseAuth: FirebaseAuth) : RemoteChallengeSource {
        return RemoteChallengeDataSource(database.reference, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideCreateAccountInteractor(loginRepository: LoginRepository, userRepository: UserRepository) : CreateAccountInteractor {
        return CreateAccountInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), loginRepository, userRepository)
    }

    @Provides
    @Singleton
    fun provideGetLastKnownLocationInteractor(locationManager: LocationManager) : GetLastKnownLocationInteractor {
        return GetLastKnownLocationInteractor(IOMaybeTransformer(AndroidSchedulers.mainThread()), locationManager)
    }

    @Provides
    @Singleton
    fun provideGetRouteListInteractor(routeRepository: RouteRepository) : GetRouteListInteractor {
        return GetRouteListInteractor(IOFlowableTransformer(AndroidSchedulers.mainThread()), routeRepository)
    }

    @Provides
    @Singleton
    fun provideLoginInteractor(loginRepository: LoginRepository, userRepository: UserRepository) : LoginInteractor {
        return LoginInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), loginRepository, userRepository)
    }

    @Provides
    @Singleton
    fun providePauseTrackingInteractor(locationManager: LocationManager) : PauseTrackingInteractor {
        return PauseTrackingInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), locationManager)
    }

    @Provides
    @Singleton
    fun provideResumeTrackingInteractor(locationManager: LocationManager) : ResumeTrackingInteractor {
        return ResumeTrackingInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), locationManager)
    }

    @Provides
    @Singleton
    fun provideSaveRouteInteractor(routeRepository: RouteRepository) : SaveRouteInteractor {
        return SaveRouteInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), routeRepository)
    }

    @Provides
    @Singleton
    fun provideSaveSnapshotInteractor(routeRepository: RouteRepository) : SaveSnapshotInteractor {
        return SaveSnapshotInteractor(ComputationCompletableTransformer(AndroidSchedulers.mainThread()), routeRepository)
    }

    @Provides
    @Singleton
    fun provideStartTrackingInteractor(locationManager: LocationManager) : StartTrackingInteractor {
        return StartTrackingInteractor(IOFlowableTransformer(AndroidSchedulers.mainThread()), locationManager)
    }

    @Provides
    @Singleton
    fun provideStopTrackingInteractor(locationManager: LocationManager) : StopTrackingInteractor {
        return StopTrackingInteractor(locationManager)
    }

    @Provides
    @Singleton
    fun provideRemoveRouteInteractor(routeRepository: RouteRepository) : RemoveRouteInteractor {
        return RemoveRouteInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), routeRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserStatsInteractor(userRepository: UserRepository) : GetUserStatsInteractor {
        return GetUserStatsInteractor(IOFlowableTransformer(AndroidSchedulers.mainThread()), userRepository)
    }

    @Provides
    @Singleton
    fun provideSaveChallengeInteractor(challengeRepository: ChallengeRepository) : SaveChallengeInteractor {
        return SaveChallengeInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), challengeRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateChallengeInteractor(challengeRepository: ChallengeRepository) : UpdateChallengesInteractor {
        return UpdateChallengesInteractor(IOSingleTransformer(AndroidSchedulers.mainThread()), challengeRepository)
    }

    @Provides
    @Singleton
    fun provideGetChallengeInteractor(challengeRepository: ChallengeRepository) : GetChallengesInteractor {
        return GetChallengesInteractor(IOFlowableTransformer(AndroidSchedulers.mainThread()), challengeRepository)
    }

    @Provides
    @Singleton
    fun provideGetActiveChallengeInteractor(challengeRepository: ChallengeRepository) : GetActiveChallengesInteractor {
        return GetActiveChallengesInteractor(IOSingleTransformer(AndroidSchedulers.mainThread()), challengeRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateUserStatsInteractor(userRepository: UserRepository) : UpdateUserStatsInteractor {
        return UpdateUserStatsInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), userRepository)
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
    fun provideGetArtistsInteractor(musicRepository: MusicRepository) : GetArtistsInteractor {
        return GetArtistsInteractor(IOSingleTransformer(AndroidSchedulers.mainThread()), musicRepository)
    }

    @Provides
    @Singleton
    fun provideGetSongsByArtistIdInteractor(musicRepository: MusicRepository) : GetSongsByArtistIdInteractor {
        return GetSongsByArtistIdInteractor(IOSingleTransformer(AndroidSchedulers.mainThread()), musicRepository)
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
        return NextSongInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), musicManager)
    }

    @Provides
    @Singleton
    fun providePreviousSongInteractor(musicManager: MusicManager) : PreviousSongInteractor {
        return PreviousSongInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), musicManager)
    }

    @Provides
    @Singleton
    fun providePauseMusicInteractor(musicManager: MusicManager) : PauseMusicInteractor {
        return PauseMusicInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), musicManager)
    }

    @Provides
    @Singleton
    fun provideStopMusicInteractor(musicManager: MusicManager) : StopMusicInteractor {
        return StopMusicInteractor(musicManager)
    }

    @Provides
    @Singleton
    fun provideIsMusicPlayingInteractor(musicManager: MusicManager) : IsMusicPlayingInteractor {
        return IsMusicPlayingInteractor(IOFlowableTransformer(AndroidSchedulers.mainThread()), musicManager)
    }

    @Provides
    @Singleton
    fun provideGetCurrentSongInteractor(musicManager: MusicManager) : GetCurrentSongInteractor {
        return GetCurrentSongInteractor(IOFlowableTransformer(AndroidSchedulers.mainThread()), musicManager)
    }
}
