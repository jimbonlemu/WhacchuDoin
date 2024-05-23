package com.jimbonlemu.whacchudoin.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.view.isGone
import com.jimbonlemu.whacchudoin.core.CoreActivity
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import com.jimbonlemu.whacchudoin.databinding.ActivityLoginBinding
import com.jimbonlemu.whacchudoin.view_models.LoginViewModel
import org.koin.android.ext.android.inject

class LoginActivity : CoreActivity<ActivityLoginBinding>() {

    private val loginViewModel: LoginViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAction()
        initObservers()

    }

    override fun setupBinding(layoutInflater: LayoutInflater): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)


    private fun initAction() {
        binding.apply {
            loginButton.setOnClickListener {
                val email = edLoginEmail.text?.trim().toString()
                val password = edLoginPassword.text?.trim().toString()

                if (email.isEmpty()) {
                    edLoginEmail.error = "Email field not allowed to left empty"
                }
                if (password.isEmpty()) {
                    edLoginPassword.error = "Password field not allowed to left empty"
                }
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    loginViewModel.login(email, password)
                }

            }

//            registerButton.setOnClickListener {
//                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
//            }
        }
    }



    private fun initObservers() {
        binding.apply {
            loginViewModel.loginResult.observe(this@LoginActivity) { result ->
                when (result) {
                    is ResponseState.Loading -> {
                        loginButton.text = "Loading...."
                        loginButton.isEnabled = false
                    }
                    is ResponseState.Success -> {
                        Toast.makeText(this@LoginActivity, "Sukses Login", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    }

                    is ResponseState.Error -> {
                        loginButton.isEnabled = true
                        loginButton.text = "Login"
                        Toast.makeText(this@LoginActivity, result.errorMessage, Toast.LENGTH_SHORT).show()
                    }

                    else -> binding.root.isGone = true
                }
            }
        }
    }

}