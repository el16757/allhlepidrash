package ntua.hci.mysecondandroidapp.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_spash.*
import ntua.hci.mysecondandroidapp.R
import ntua.hci.mysecondandroidapp.ui.login.LoginActivity
import ntua.hci.mysecondandroidapp.ui.signup.SignupActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spash)

        btnEmail.setOnClickListener {
            val login = Intent(this, LoginActivity::class.java)
            startActivity(login)
        }

        txtSignUp.setOnClickListener {
            val signup = Intent(this, SignupActivity::class.java)
            startActivity(signup)
        }
    }
}