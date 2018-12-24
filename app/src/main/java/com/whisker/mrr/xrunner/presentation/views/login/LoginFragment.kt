package com.whisker.mrr.xrunner.presentation.views.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    private lateinit var viewModel: LoginViewModel

    private val loginObserver = Observer<Boolean> {
        if(it) {
            mainActivity.loggedIn()
        } else {
            Toast.makeText(context, "Failed to login", Toast.LENGTH_SHORT).show()
            enableButtons()
        }
    }

    private val createAccountObserver = Observer<Boolean> {
        if(it) {
            Toast.makeText(context, "Account Created", Toast.LENGTH_SHORT).show()
            enableButtons()
        } else {
            Toast.makeText(context, "Failed to create account", Toast.LENGTH_SHORT).show()
            enableButtons()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        viewModel.getCreateAccountStatus().observe(this, createAccountObserver)
        viewModel.getLoginStatus().observe(this, loginObserver)

        btLogin.setOnClickListener {
            viewModel.firebaseLogin(etEmail.text.toString(), etPassword.text.toString())
            disableButtons()
        }
        btCreateAccount.setOnClickListener {
            viewModel.firebaseCreateAccount(etEmail.text.toString(), etPassword.text.toString())
            disableButtons()
        }
    }

    private fun disableButtons() {
        btLogin.isEnabled = false
        btCreateAccount.isEnabled = false
    }

    private fun enableButtons() {
        btLogin.isEnabled = true
        btCreateAccount.isEnabled = true
    }
}
