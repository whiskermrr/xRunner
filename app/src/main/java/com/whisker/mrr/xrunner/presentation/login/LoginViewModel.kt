package com.whisker.mrr.xrunner.presentation.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import domain.LoginRepository
import io.reactivex.disposables.CompositeDisposable

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

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
