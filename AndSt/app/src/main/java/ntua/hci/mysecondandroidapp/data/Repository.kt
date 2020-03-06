package ntua.hci.mysecondandroidapp.data

import ntua.hci.mysecondandroidapp.data.model.LoggedInUser
import ntua.hci.mysecondandroidapp.data.model.ProductItems
import ntua.hci.mysecondandroidapp.ui.product.ProductAdapter

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class Repository(val dataSource: DataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    fun signup(name: String, username: String, password: String): Result<LoggedInUser> {
        //handle signup
        val result = dataSource.signup(name, username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    fun getProduct(query : String): List<ProductItems> {
        var x = dataSource.getProducts(query)
        return x
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
