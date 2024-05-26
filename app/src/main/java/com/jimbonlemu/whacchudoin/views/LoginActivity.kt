package com.jimbonlemu.whacchudoin.views

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import com.jimbonlemu.whacchudoin.R
import com.jimbonlemu.whacchudoin.core.CoreActivity
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import com.jimbonlemu.whacchudoin.databinding.ActivityLoginBinding
import com.jimbonlemu.whacchudoin.view_models.AuthViewModel
import org.koin.android.ext.android.inject

class LoginActivity : CoreActivity<ActivityLoginBinding>() {

    private val authViewModel: AuthViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animationPlay()
        initAction()
        initObservers()
    }

    override fun setupBinding(layoutInflater: LayoutInflater): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)


    private fun initAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = edLoginEmail.text?.trim().toString()
                val password = edLoginPassword.text?.trim().toString()

                if (email.isEmpty()) {
                    edLoginEmail.error = getString(R.string.email_empty_error)
                }
                if (password.isEmpty()) {
                    edLoginPassword.error = getString(R.string.password_empty_error)
                }
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    authViewModel.login(email, password)
                }
            }
            btnRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun initObservers() {
        binding.apply {
            authViewModel.loginResult.observe(this@LoginActivity) { result ->
                when (result) {
                    is ResponseState.Loading -> {
                        btnLogin.text = getString(R.string.loading)
                        isFormFieldEnabled(false)
                    }

                    is ResponseState.Success -> {
                        isFormFieldEnabled(true)
                        Toast.makeText(this@LoginActivity,
                            getString(R.string.success_login), Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    }

                    is ResponseState.Error -> {
                        isFormFieldEnabled(true)
                        btnLogin.isEnabled = true
                        btnLogin.text = getString(R.string.login)
                        Toast.makeText(this@LoginActivity, result.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> binding.root.isGone = true
                }
            }
        }
    }

    private fun isFormFieldEnabled(isEnable: Boolean) {
        binding.apply {
            if (isEnable) {
                btnLogin.isEnabled = true
                btnRegister.isEnabled = true
                edLoginEmail.isEnabled = true
                edLoginPassword.isEnabled = true
            } else {
                btnLogin.isEnabled = false
                btnRegister.isEnabled = false
                edLoginEmail.isEnabled = false
                edLoginPassword.isEnabled = false
            }
        }
    }

    private fun animationPlay(){
        binding.apply {
            AnimatorSet().apply {
                playSequentially(
                    ObjectAnimator.ofFloat(loginLogo, View.ALPHA, 1f).setDuration(500),
                    ObjectAnimator.ofFloat(headlineLogin, View.ALPHA, 1f).setDuration(500),
                    ObjectAnimator.ofFloat(edLoginEmail, View.ALPHA, 1f).setDuration(500),
                    ObjectAnimator.ofFloat(edLoginPassword, View.ALPHA, 1f).setDuration(500),
                    ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f).setDuration(500),
                    ObjectAnimator.ofFloat(btnRegister, View.ALPHA, 1f).setDuration(500),
                )
                start()
            }
        }
    }

}