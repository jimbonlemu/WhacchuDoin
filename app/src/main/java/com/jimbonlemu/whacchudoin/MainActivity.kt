package com.jimbonlemu.whacchudoin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jimbonlemu.whacchudoin.databinding.ActivityMainBinding
import com.jimbonlemu.whacchudoin.views.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()

        }
    }
}