package ntua.hci.mysecondandroidapp.ui.signup

/**
 * Authentication result : success (user details) or error message.
 */
data class SignupResult(
    val success: SignedUpUserView? = null,
    val error: Int? = null
)
