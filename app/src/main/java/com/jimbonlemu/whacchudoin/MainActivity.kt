package com.jimbonlemu.whacchudoin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jimbonlemu.whacchudoin.databinding.ActivityMainBinding
import com.jimbonlemu.whacchudoin.utils.PreferenceManager
import com.jimbonlemu.whacchudoin.views.HomeActivity
import com.jimbonlemu.whacchudoin.views.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.time.Duration.Companion.seconds

class MainActivity : AppCompatActivity() {
    private val preferenceManager: PreferenceManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            playAnimation()
            lifecycleScope.launch {
                delay(SPLASH_SCREEN_DURATION.seconds)
                if (preferenceManager.getToken.isNullOrEmpty()) {
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                    finish()

                }
            }
        }
    }

    private fun ActivityMainBinding.playAnimation() {
        AnimatorSet().apply {
            playSequentially(
                ObjectAnimator.ofFloat(ivSplash, View.ALPHA, 1f).setDuration(1000),
            )
            start()
        }
    }

    companion object {
        const val SPLASH_SCREEN_DURATION = 3
    }
}