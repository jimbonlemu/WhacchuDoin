package com.jimbonlemu.whacchudoin.views


import android.os.Bundle
import android.view.LayoutInflater
import com.jimbonlemu.whacchudoin.core.CoreActivity
import com.jimbonlemu.whacchudoin.databinding.ActivityRegisterBinding

class RegisterActivity : CoreActivity<ActivityRegisterBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {

        }
    }

    override fun setupBinding(layoutInflater: LayoutInflater): ActivityRegisterBinding =
        ActivityRegisterBinding.inflate(layoutInflater)
}