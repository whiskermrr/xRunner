package com.whisker.mrr.xrunner.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.xrunner.domain.LoginRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginViewModel
@Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val loginStatus = MutableLiveData<Boolean>()
    private val createAccountStatus = MutableLiveData<Boolean>()

    fun firebaseLogin(email: String, password: String) {
        disposables.add(
            loginRepository.login(email, password)
                .subscribe {
                    loginStatus.postValue(true)
                }
        )
    }

    fun firebaseCreateAccount(email: String, password: String) {
        disposables.add(
            loginRepository.createAccount(email, password)
                .subscribe {
                    createAccountStatus.postValue(true)
                }
        )
    }

    fun getLoginStatus() = loginStatus

    fun getCreateAccountStatus() = createAccountStatus
}
