package ntua.hci.mysecondandroidapp.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
//import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import ntua.hci.mysecondandroidapp.R
import ntua.hci.mysecondandroidapp.data.DataSource
import ntua.hci.mysecondandroidapp.data.Repository
import ntua.hci.mysecondandroidapp.ui.cart.ShoppingCart
import ntua.hci.mysecondandroidapp.ui.product.ProductAdapter


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

        val displayName = intent.getStringExtra("DISPLAY_NAME")
        val txtName = findViewById<TextView>(R.id.txtName)

        txtName.text = getString(R.string.txtName) + " " + displayName + "!"

        if (Intent.ACTION_SEARCH == intent.action){
            intent.getStringExtra(SearchManager.QUERY)?.also {
                query -> ProductAdapter(this@MainActivity,Repository(DataSource()).getProduct(query))
            }
        }

        btnDisconnect.setOnClickListener {
            finish()
            //exitProcess(0)
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            //Assumes current activity is the searcahble activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}