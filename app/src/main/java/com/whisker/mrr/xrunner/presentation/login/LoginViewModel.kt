package com.whisker.mrr.xrunner.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.xrunner.domain.interactor.CreateAccountInteractor
import com.whisker.mrr.xrunner.domain.interactor.LoginInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel
@Inject constructor(private val loginInteractor: LoginInteractor,
                    private val createAccountInteractor: CreateAccountInteractor) : ViewModel() {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val loginStatus = MutableLiveData<Boolean>()
    private val createAccountStatus = MutableLiveData<Boolean>()

    fun firebaseLogin(email: String, password: String) {
        disposables.add(
            loginInteractor.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    loginStatus.postValue(true)
                }, {
                    loginStatus.postValue(false)
                })
        )
    }

    fun firebaseCreateAccount(email: String, password: String) {
        disposables.add(
            createAccountInteractor.createAccount(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    createAccountStatus.postValue(true)
                }, {
                    createAccountStatus.postValue(false)
                })
        )
    }

    fun getLoginStatus() = loginStatus
    fun getCreateAccountStatus() = createAccountStatus
}
