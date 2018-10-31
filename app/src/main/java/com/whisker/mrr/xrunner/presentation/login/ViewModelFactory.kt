package com.whisker.mrr.xrunner.presentation.login

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import domain.LoginRepository

class ViewModelFactory(private val loginRepository: LoginRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(loginRepository) as T
    }
}