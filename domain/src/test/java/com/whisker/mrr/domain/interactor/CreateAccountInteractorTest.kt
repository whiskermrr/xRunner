package com.whisker.mrr.domain.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.whisker.mrr.domain.common.exception.NoConnectivityException
import com.whisker.mrr.domain.common.scheduler.IOCompletableTransformer
import com.whisker.mrr.domain.repository.LoginRepository
import com.whisker.mrr.domain.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class CreateAccountInteractorTest {

    var mockLoginRepository = mock<LoginRepository>()

    var mockUserRepository = mock<UserRepository>()

    lateinit var transformer: CompletableTransformer

    lateinit var createAccountInteractor: CreateAccountInteractor

    @Before
    fun setUp() {
        transformer = IOCompletableTransformer(Schedulers.trampoline())
        createAccountInteractor = CreateAccountInteractor(transformer, mockLoginRepository, mockUserRepository)
    }

    @Test
    fun createAccountSuccess() {
        val userId = "123"
        val singleUserId = Single.just(userId)
        whenever(mockLoginRepository.createAccount(anyString(), anyString()))
            .thenReturn(singleUserId)

        whenever(mockUserRepository.createUserStats(anyString()))
            .thenReturn(Completable.complete())

        createAccountInteractor.createAccount("test@test.com", "12345678")
            .test()
            .await()
            .assertComplete()
    }

    @Test
    fun createAccountFailed() {
        val error = NoConnectivityException()
        whenever(mockLoginRepository.createAccount(anyString(), anyString()))
            .thenReturn(Single.error(error))

        createAccountInteractor.createAccount("test@test", "12345678")
            .test()
            .await()
            .assertError(error)

        verifyZeroInteractions(mockUserRepository)
    }

    @After
    fun tearDown() {
    }
}