package com.jimbonlemu.whacchudoin.views

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.jimbonlemu.whacchudoin.R
import com.jimbonlemu.whacchudoin.core.CoreActivity
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import com.jimbonlemu.whacchudoin.databinding.ActivityRegisterBinding
import com.jimbonlemu.whacchudoin.view_models.AuthViewModel
import org.koin.android.ext.android.inject

class RegisterActivity : CoreActivity<ActivityRegisterBinding>() {
    private val authViewModel: AuthViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animationPlay()
        setupObservers()
        formValidation()
    }

    private fun formValidation() {
        binding.apply {
            btnRegister.setOnClickListener {
                val nameField = edRegisterName.text.trim().toString()
                val emailField = edRegisterEmail.text?.trim().toString()
                val passwordField = edRegisterPassword.text?.trim().toString()
                val passwordConfirmationField = edConfirmRegisterPassword.text?.trim().toString()
                if (nameField.isEmpty()) {
                    getToast(getString(R.string.name_empty_error))
                }
                if (emailField.isEmpty()) {
                    getToast(getString(R.string.email_empty_error))
                }
                if (passwordField.isEmpty()) {
                    getToast(getString(R.string.password_empty_error))
                }
                if (passwordConfirmationField.isEmpty()) {
                    getToast(getString(R.string.password_confirm_empty_error))
                }
                if (passwordField != passwordConfirmationField) {
                    getToast(getString(R.string.password_field_and_confirmation_error))
                }
                if (nameField.isNotEmpty() && emailField.isNotEmpty() && passwordField.isNotEmpty() && passwordConfirmationField.isNotEmpty() && passwordField == passwordConfirmationField) {
                    authViewModel.register(
                        name = nameField,
                        email = emailField,
                        password = passwordField
                    )
                }
            }

            btnLogin.setOnClickListener {
                startActivity(
                    Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
                finish()
            }
        }
    }

    private fun setupObservers() {
        binding.apply {
            authViewModel.registerResult.observe(this@RegisterActivity) { response ->
                when (response) {
                    is ResponseState.Loading -> {
                        isComponentEnabled(false)
                    }

                    is ResponseState.Success -> {
                        edRegisterName.text.clear()
                        edRegisterEmail.text?.clear()
                        edRegisterPassword.text?.clear()
                        edConfirmRegisterPassword.text?.clear()
                        isComponentEnabled(true)
                        getToast(getString(R.string.success_register))
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    }

                    is ResponseState.Error -> {
                        isComponentEnabled(true)
                        getToast(response.errorMessage)
                    }
                }
            }
        }
    }

    private fun isComponentEnabled(isEnabled: Boolean) {
        binding.apply {
            if (isEnabled) {
                btnLogin.isEnabled = true
                btnRegister.text = getString(R.string.register)
                edRegisterName.isEnabled = true
                edRegisterEmail.isEnabled = true
                edRegisterPassword.isEnabled = true
                edConfirmRegisterPassword.isEnabled = true
            } else {
                btnLogin.isEnabled = false
                btnRegister.text = getString(R.string.loading)
                edRegisterName.isEnabled = false
                edRegisterEmail.isEnabled = false
                edRegisterPassword.isEnabled = false
                edConfirmRegisterPassword.isEnabled = false
            }
        }
    }

    private fun getToast(msg: String) {
        Toast.makeText(this@RegisterActivity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun animationPlay(){
        binding.apply {
            AnimatorSet().apply {
                playSequentially(
                    ObjectAnimator.ofFloat(registerLogo, View.ALPHA, 1f).setDuration(500),
                    ObjectAnimator.ofFloat(headlineRegister, View.ALPHA, 1f).setDuration(500),
                    ObjectAnimator.ofFloat(edRegisterName, View.ALPHA, 1f).setDuration(500),
                    ObjectAnimator.ofFloat(edRegisterEmail, View.ALPHA, 1f).setDuration(500),
                    ObjectAnimator.ofFloat(edRegisterPassword, View.ALPHA, 1f).setDuration(500),
                    ObjectAnimator.ofFloat(edConfirmRegisterPassword, View.ALPHA, 1f).setDuration(500),
                    ObjectAnimator.ofFloat(btnRegister, View.ALPHA, 1f).setDuration(500),
                    ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f).setDuration(500),
                )
                start()
            }
        }
    }

    override fun setupBinding(layoutInflater: LayoutInflater): ActivityRegisterBinding =
        ActivityRegisterBinding.inflate(layoutInflater)
}