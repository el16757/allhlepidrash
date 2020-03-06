package ntua.hci.mysecondandroidapp.data

import android.content.res.XmlResourceParser
import android.os.AsyncTask
import android.util.Log
import co.nedim.maildroidx.MaildroidX
import co.nedim.maildroidx.MaildroidXType
import co.nedim.maildroidx.callback
import ntua.hci.mysecondandroidapp.App
import ntua.hci.mysecondandroidapp.R
import ntua.hci.mysecondandroidapp.data.model.CartItems
import ntua.hci.mysecondandroidapp.data.model.LoggedInUser
import ntua.hci.mysecondandroidapp.data.model.ProductItems
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class DataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            val hostSettings = readSettings(R.xml.settings, "host")

            val url: String = String.format(
                hostSettings.getAttributeValue(null, "login_uri"),
                hostSettings.getAttributeValue(null, "name"),
                username,
                password.md5()
            )

            val response = HTTPAsyncGetTask().execute(url).get()
            val data = JSONObject(response)

            return when {
                data.has("Success") -> {
                    val result = data.get("Success")
                    val userid = (result as JSONObject).keys().next()
                    val user = LoggedInUser(userid, result.getString(userid))
                    Result.Success(user)
                }
                data.has("Error") -> Result.Error(Exception(data.getString("Error")))
                else -> Result.Error(Exception("Unspecified Error"))
            }
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun signup(name: String, username: String, password: String): Result<LoggedInUser> {
        try {
            val hostSettings = readSettings(R.xml.settings, "host")
            val mailSettings = readSettings(R.xml.settings, "mail")

            val url: String = String.format(
                hostSettings.getAttributeValue(null, "register_uri"),
                hostSettings.getAttributeValue(null, "name")
            )
            val response = HTTPAsyncPostTask().execute(
                url,
                hostSettings.getAttributeValue(null, "post_params"),
                name,
                username,
                password.md5()
            ).get()

            return when {
                (response == "\"Success\"") -> {
                    MaildroidX.Builder()
                        .smtp(mailSettings.getAttributeValue(null, "host"))
                        .smtpUsername(mailSettings.getAttributeValue(null, "username"))
                        .smtpPassword(mailSettings.getAttributeValue(null, "password"))
                        .smtpAuthentication(true)
                        .port(mailSettings.getAttributeValue(null, "port"))
                        .type(MaildroidXType.HTML)
                        .to("$name <$username>")
                        .from(mailSettings.getAttributeValue(null, "from"))
                        .subject(mailSettings.getAttributeValue(null, "subject"))
                        .body(
                            String.format(
                                mailSettings.getAttributeValue(null, "body"),
                                name)
                        ).onCompleteCallback(object: MaildroidX.onCompleteCallback {
                            override val timeout: Long
                                get() = 3000

                            override fun onSuccess() {
                                Log.d("MaildroiX", "SUCCESS")
                            }

                            override fun onFail(errorMessage: String) {
                                Log.d("MaildroidX", "FAILURE: $errorMessage")
                            }
                        }).mail()

                    this.login(name, password)
                }
                else -> {
                    val data = JSONObject(response)
                    Result.Error(Exception(data.getString("Error"))) //
                }
            }
        } catch (e: Throwable) {
            return Result.Error(IOException("Error signing up", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
    fun getProducts(item :String) :List<ProductItems>{
        try {
            val hostSettings = readSettings(R.xml.settings, "host")

            val url: String = String.format(
                hostSettings.getAttributeValue(null, "product_uri"),
                hostSettings.getAttributeValue(null, "name"),
                item
            )

            val response = HTTPAsyncPostTask().execute(url).get()
            val data = JSONArray(response)
            val items : MutableList<ProductItems> = mutableListOf<ProductItems>()
            for (i in 0 until data.length())  {
                val obj = data.getJSONObject(i)
                val itemName= obj.getString("itemName")
                val storename= obj.getString("storename")
                val price= obj.getDouble("price")
                val item = ProductItems(itemName,storename,price)
                items.add(item)
            }
            return items
        } catch (e: Throwable) {
            throw IOException("Error counld't show products", e)
        }
    }
    fun addToCart(item :String , username : String , quantity : Int): Result<String>{
        try {
            val hostSettings = readSettings(R.xml.settings, "host")

            val url: String = String.format(
                hostSettings.getAttributeValue(null, "cart_uri"),
                hostSettings.getAttributeValue(null, "name")
            )

            val response = HTTPAsyncPostTask().execute(
                url,
                hostSettings.getAttributeValue(null, "cart_post_params"),
                username,
                item,
                quantity.toString()
            ).get()
            val data = JSONObject(response)

            return when {
                data.has("Success") -> {
                    Result.Success("Success")
                }
                data.has("Error") -> Result.Error(Exception(data.getString("Error")))
                else -> Result.Error(Exception("Unspecified Error"))
            }
        } catch (e: Throwable) {
            return Result.Error(IOException("Error adding to cart", e))
        }
    }

    fun getCart(username : String ): Result<List<CartItems>>{
        try {
            val hostSettings = readSettings(R.xml.settings, "host")


            val url: String = String.format(
                hostSettings.getAttributeValue(null, "cart_uri"),
                hostSettings.getAttributeValue(null, "name"),
                username
            )

            val response = HTTPAsyncGetTask().execute(url).get()
            val data = JSONArray(response)
            val items : MutableList<CartItems> = mutableListOf<CartItems>()
            for (i in 0 until data.length())  {
                val obj = data.getJSONObject(i);
                val itemName = obj.getString("itemName")
                val storename = obj.getString("storename")
                val price = obj.getDouble("price")
                val product = ProductItems(itemName,storename,price)
                val item = CartItems(product)
                items.add(item)
            }
            return Result.Success(items)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error couldn't show cart", e))
        }
    }

    private fun readSettings(resource_id: Int, tag: String): XmlResourceParser {
        val xml = App.resourses.getXml(resource_id)
        var eventType = -1

        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {
                if (xml.name == tag)
                    return xml
            }
            eventType = xml.next()
        }
        throw Exception("Invalid settings.xml file")
    }

    private fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray()))
            .toString(16).padStart(32, '0')
    }

    inner class HTTPAsyncGetTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            return URL(params[0]).readText()
        }
    }

    inner class HTTPAsyncPostTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            val url = URL(params[0])

            val urlParams:String = String.format(
                params[1].toString(), params[2], params[3], params[4]
            )

            val response = StringBuffer()

            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "POST"

                val wr = OutputStreamWriter(outputStream)
                wr.write(urlParams)
                wr.flush()

                // I have responseCode as well
                BufferedReader(InputStreamReader(inputStream)).use {
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                }
            }
            return response.toString()
        }
    }

}

