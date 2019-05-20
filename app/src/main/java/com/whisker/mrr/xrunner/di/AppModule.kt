package com.whisker.mrr.xrunner.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.whisker.mrr.data.database.DbRunner
import com.whisker.mrr.data.database.dao.ChallengeDao
import com.whisker.mrr.data.database.dao.PreferencesDao
import com.whisker.mrr.data.database.dao.RouteDao
import com.whisker.mrr.data.database.dao.UserStatsDao
import com.whisker.mrr.data.repository.ChallengeDataRepository
import com.whisker.mrr.data.repository.RouteDataRepository
import com.whisker.mrr.data.repository.UserDataRepository
import com.whisker.mrr.data.source.LocalChallengeDataSource
import com.whisker.mrr.data.source.LocalRouteDataSource
import com.whisker.mrr.data.source.LocalUserDataSource
import com.whisker.mrr.domain.common.scheduler.*
import com.whisker.mrr.xrunner.App
import com.whisker.mrr.domain.interactor.*
import com.whisker.mrr.domain.manager.MusicManager
import com.whisker.mrr.domain.repository.*
import com.whisker.mrr.domain.source.*
import com.whisker.mrr.firebase.datasource.*
import com.whisker.mrr.firebase.repository.LoginDataRepository
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
    fun provideLocationDataSource(context: Context) : LocationSource {
        return LocationDataSource(context)
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
    fun provideLocalChallengeSource(challengeDao: ChallengeDao) : LocalChallengeSource {
        return LocalChallengeDataSource(challengeDao)
    }

    @Provides
    @Singleton
    fun provideLocalUserSource(userStatsDao: UserStatsDao) : LocalUserSource {
        return LocalUserDataSource(userStatsDao)
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
    fun provideRemoteUserSource(database: FirebaseDatabase, firebaseAuth: FirebaseAuth) : RemoteUserSource {
        return RemoteUserDataSource(database.reference, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideRemoteChallengeSource(database: FirebaseDatabase, firebaseAuth: FirebaseAuth) : RemoteChallengeSource {
        return RemoteChallengeDataSource(database.reference, firebaseAuth)
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
    fun provideGetRouteListInteractor(routeRepository: RouteRepository) : GetRouteListInteractor {
        return GetRouteListInteractor(IOFlowableTransformer(AndroidSchedulers.mainThread()), routeRepository)
    }

    @Provides
    @Singleton
    fun provideLoginInteractor(loginRepository: LoginRepository) : LoginInteractor {
        return LoginInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), loginRepository)
    }

    @Provides
    @Singleton
    fun providePauseTrackingInteractor(locationSource: LocationSource) : PauseTrackingInteractor {
        return PauseTrackingInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), locationSource)
    }

    @Provides
    @Singleton
    fun provideResumeTrackingInteractor(locationSource: LocationSource) : ResumeTrackingInteractor {
        return ResumeTrackingInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), locationSource)
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
        return UpdateChallengesInteractor(IOCompletableTransformer(AndroidSchedulers.mainThread()), challengeRepository)
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
    fun provideGetCurrentSongInteractor(musicManager: MusicManager) : GetCurrentSongInteractor {
        return GetCurrentSongInteractor(IOFlowableTransformer(AndroidSchedulers.mainThread()), musicManager)
    }
}
