package ntua.hci.mysecondandroidapp.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ntua.hci.mysecondandroidapp.data.DataSource
import ntua.hci.mysecondandroidapp.data.Repository

/**
 * ViewModel provider factory to instantiate SignupViewModel.
 * Required given SignupViewModel has a non-empty constructor
 */
class SignupViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            return SignupViewModel(
                signupRepository = Repository(
                    dataSource = DataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
