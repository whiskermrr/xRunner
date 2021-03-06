package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.repository.LoginRepository
import com.whisker.mrr.domain.usecase.CompletableUseCase
import com.whisker.mrr.domain.common.whenBothNotNull
import com.whisker.mrr.domain.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import java.lang.IllegalArgumentException

class CreateAccountInteractor(
    transformer: CompletableTransformer,
    private val loginRepository: LoginRepository,
    private val userRepository: UserRepository
) : CompletableUseCase(transformer) {

    companion object {
        private const val PARAM_EMAIL = "param_email"
        private const val PARAM_PASSWORD = "param_password"
    }

    fun createAccount(email: String, password: String) : Completable {
        val data = HashMap<String, String>()
        data[PARAM_EMAIL] = email
        data[PARAM_PASSWORD] = password
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val emailData = data?.get(PARAM_EMAIL)
        val passwordData = data?.get(PARAM_PASSWORD)

        whenBothNotNull(emailData, passwordData) { email, password ->
            return loginRepository.createAccount(email.toString(), password.toString())
                .flatMapCompletable { userId ->
                    userRepository.createUserStats(userId)
                }
        }

        return Completable.error(IllegalArgumentException("@email and @password must be provided."))
    }
}