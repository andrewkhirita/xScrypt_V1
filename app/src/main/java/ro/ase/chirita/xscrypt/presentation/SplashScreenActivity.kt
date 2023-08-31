package ro.ase.chirita.xscrypt.presentation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.delay
import ro.ase.chirita.xscrypt.R
import ro.ase.chirita.xscrypt.presentation.auth.AuthenticationActivity


class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashScreenActivity,AuthenticationActivity::class.java))
            finish()
        },1000)
    }
}