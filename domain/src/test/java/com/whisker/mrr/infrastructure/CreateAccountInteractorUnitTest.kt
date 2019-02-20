package com.whisker.mrr.infrastructure

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.whisker.mrr.domain.interactor.CreateAccountInteractor
import com.whisker.mrr.domain.repository.LoginRepository
import com.whisker.mrr.domain.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.MockitoAnnotations
import java.lang.IllegalArgumentException

class CreateAccountInteractorUnitTest {

    private var loginRepository = mock<LoginRepository>()


    private var userRepository = mock<UserRepository>()

    private var createAccountInteractor = mock<CreateAccountInteractor>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun createAccountSuccess() {
        val userId = "123"
        val singleUserId = Single.just(userId)
        doReturn(singleUserId).`when`(loginRepository).createAccount(anyString(), anyString())
        doReturn(Completable.complete()).`when`(userRepository).createUserStats(userId)

        val response = Completable.complete()
        doReturn(response).`when`(createAccountInteractor).createAccount(anyString(), anyString())

        createAccountInteractor.createAccount("test@test.com", "12345678")
            .test()
            .assertComplete()
    }

    @Test
    fun createAccountFailed() {
        val error = Throwable("Error")
        val singleError = Single.error<Throwable>(error)
        val completableError = Completable.error(error)
        doReturn(singleError).`when`(loginRepository).createAccount(anyString(), anyString())
        doReturn(completableError).`when`(createAccountInteractor).createAccount(anyString(), anyString())

        createAccountInteractor.createAccount("test@test.com", "12345678")
            .test()
            .assertError(error)
    }

    @Test
    fun createAccountIllegalArgumentException() {
        val error = IllegalArgumentException("@email and @password must be provided.")
        val completableError = Completable.error(error)
        doReturn(completableError).`when`(createAccountInteractor).createCompletable(null)

        createAccountInteractor.createCompletable(null)
            .test()
            .assertError(error)
    }

    @Test
    fun createAccountIllegalArgumentExceptionTwo() {
        val error = IllegalArgumentException("@email and @password must be provided.")
        val completableError = Completable.error(error)
        doReturn(completableError).`when`(createAccountInteractor).completable(null)

        createAccountInteractor.completable(null)
            .test()
            .assertError(error)
    }
}