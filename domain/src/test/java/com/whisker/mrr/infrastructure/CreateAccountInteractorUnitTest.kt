package com.whisker.mrr.infrastructure

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.whisker.mrr.domain.common.scheduler.IOCompletableTransformer
import com.whisker.mrr.domain.interactor.CreateAccountInteractor
import com.whisker.mrr.domain.repository.LoginRepository
import com.whisker.mrr.domain.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class CreateAccountInteractorUnitTest {

    private val loginRepository = mock<LoginRepository>()
    private val userRepository = mock<UserRepository>()

    private val transformer: IOCompletableTransformer = IOCompletableTransformer(Schedulers.trampoline())

    private val createAccountInteractor by lazy {
        CreateAccountInteractor(transformer, loginRepository, userRepository)
    }

    @Test
    fun createAccountSuccess() {
        val userId = "123"
        val singleUserId = Single.just(userId)
        doReturn(singleUserId).`when`(loginRepository).createAccount(anyString(), anyString())
        doReturn(Completable.complete()).`when`(userRepository).createUserStats(userId)

        val response = Completable.complete()
        whenever(createAccountInteractor.createAccount(anyString(), anyString()))
            .thenReturn(response)

        createAccountInteractor.createAccount("test@test.com", "12345678")
            .test()
            .assertComplete()
    }

    @Test
    fun createAccountFailed() {
        whenever(createAccountInteractor.createAccount(anyString(), anyString()))
            .thenReturn(Completable.error(Throwable("Error")))

        createAccountInteractor.createAccount("test@test.com", "12345678")
            .test()
            .assertError(Throwable("Error"))
    }
}