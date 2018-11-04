package com.whisker.mrr.xrunner.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.xrunner.domain.repository.LoginRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel
@Inject constructor(private val loginDataRepository: LoginRepository) : ViewModel() {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val loginStatus = MutableLiveData<Boolean>()
    private val createAccountStatus = MutableLiveData<Boolean>()

    fun firebaseLogin(email: String, password: String) {
        disposables.add(
            loginDataRepository.login(email, password)
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
            loginDataRepository.createAccount(email, password)
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
