package ntua.hci.mysecondandroidapp.ui.signup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_signup.*
import ntua.hci.mysecondandroidapp.R
import ntua.hci.mysecondandroidapp.ui.login.afterTextChanged
import ntua.hci.mysecondandroidapp.ui.main.MainActivity
import ntua.hci.mysecondandroidapp.ui.splash.SplashActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val name = findViewById<EditText>(R.id.name)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val retype_password = findViewById<EditText>(R.id.retype_password)
        val signup = findViewById<Button>(R.id.signup)

        signupViewModel = ViewModelProviders.of(this, SignupViewModelFactory())
            .get(SignupViewModel::class.java)

        signupViewModel.signupFormState.observe(this@SignupActivity, Observer {
            val signupState = it ?: return@Observer

            // disable signup button unless all text input fields are valid
            signup.isEnabled = signupState.isDataValid

            if (signupState.nameError != null) {
                name.error = getString(signupState.nameError)
            }
            if (signupState.usernameError != null) {
                username.error = getString(signupState.usernameError)
            }
            if (signupState.passwordError != null) {
                password.error = getString(signupState.passwordError)
            }
            if (signupState.retypePasswordError != null) {
                retype_password.error = getString(signupState.retypePasswordError)
            }
            if (signupState.passwordMismatchError != null) {
                retype_password.error = getString(signupState.passwordMismatchError)
            }
        })

        signupViewModel.signupResult.observe(this@SignupActivity, Observer {
            val signupResult = it ?: return@Observer

            if (signupResult.error != null) {
                showSignupFailed(signupResult.error)
            }
            if (signupResult.success != null) {
                updateUiWithUser(signupResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()

            if (signupResult.success != null) {
                val main = Intent(this, MainActivity::class.java)
                main.putExtra("DISPLAY_NAME", signupResult.success.displayName)
                startActivity(main)
            } else {
                val splash = Intent(this, SplashActivity::class.java)
                startActivity(splash)
            }
        })

        name.afterTextChanged {
            signupViewModel.signupDataChanged(
                name.text.toString(),
                username.text.toString(),
                password.text.toString(),
                retype_password.text.toString()
            )
        }

        username.afterTextChanged {
            signupViewModel.signupDataChanged(
                name.text.toString(),
                username.text.toString(),
                password.text.toString(),
                retype_password.text.toString()
            )
        }

        password.afterTextChanged {
            signupViewModel.signupDataChanged(
                name.text.toString(),
                username.text.toString(),
                password.text.toString(),
                retype_password.text.toString()
            )
        }

        retype_password.apply {
            afterTextChanged {
                signupViewModel.signupDataChanged(
                    name.text.toString(),
                    username.text.toString(),
                    password.text.toString(),
                    retype_password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        signupViewModel.signup(
                            name.text.toString(),
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            signup.setOnClickListener {
                signupViewModel.signup(
                    name.text.toString(),
                    username.text.toString(),
                    password.text.toString())
            }
        }
    }

    private fun updateUiWithUser(model: SignedUpUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName !",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showSignupFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}