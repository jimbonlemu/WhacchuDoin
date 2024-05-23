package com.jimbonlemu.whacchudoin.core

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.jimbonlemu.whacchudoin.R

abstract class CoreActivity<viewBinding: ViewBinding> : AppCompatActivity()  {
    private var _binding: viewBinding? = null
    protected val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = setupBinding(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun setupBinding(layoutInflater: LayoutInflater): viewBinding

}