/*
 * DayTime:10/26/18 12:20 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.activities

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.model.data.AccessToken
import com.berhane.biniam.wallpack.wallpack.model.data.Me
import com.berhane.biniam.wallpack.wallpack.model.data.User
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.connectivity.RetrofitClient
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity(), RetrofitClient.OnRequestAccessTokenListener,
        RetrofitClient.OnRequestMeProfileListener,
        RetrofitClient.OnRequestUserProfileListener {

    private val TAG: String = "LoginActivity"
    private var retrofitClient: RetrofitClient? = null
    private var authPref: SharedPreferences? = null
    var authorized: Boolean = false
    private lateinit var me: Me

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        retrofitClient = RetrofitClient.getRetrofitClient()
        authPref = this.getSharedPreferences(PhotoConstants.PREFERENCE_NAME, 0)

        val accessToken = authPref!!.getString(PhotoConstants.KEY_ACCESS_TOKEN, "")

        this.authorized = !TextUtils.isEmpty(accessToken)
        // Login Button Clicked event
        login_btn.setOnClickListener {
            openWebPage(PhotoConstants.loginUrl())
        }
        // Join Button Clicked event
        join_btn.setOnClickListener {
            openWebPage(PhotoConstants.UNSPLASH_JOIN_URL)
        }
    }

    fun isAuthorized(): Boolean {
        return authorized
    }

    /**
     * LiveData of getting an AccessToken
     */
    private fun getAccessToken(code: String) {
        if (null == retrofitClient) {
            retrofitClient = RetrofitClient.getRetrofitClient()
        }
        retrofitClient!!.requestAccessToken(code, this@LoginActivity)
    }

    /**
     * Open chrome tabs
     * @param url
     */
    private fun openWebPage(url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    /**
     * New Intent coming here
     */
    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        if (intent.data != null && !TextUtils.isEmpty(intent.data.authority)
                && PhotoConstants.HOST == intent.data.authority) {
            getAccessToken(intent.data.getQueryParameter("code"))
        }
    }

    override fun onRequestAccessTokenSuccess(call: Call<AccessToken>, response: Response<AccessToken>) {
        if (response.isSuccessful) {
            writeToPrefFile(response.body()!!)
            retrofitClient!!.cancelCall()
            retrofitClient!!.requestMeProfile(this@LoginActivity)
            finish()
        } else {
            PhotoConstants.showSnack(container_login, "Request Of Token Failed")
        }
    }

    override fun onRequestAccessTokenFailed(call: Call<AccessToken>, t: Throwable) {
        t.printStackTrace()
        Log.e(TAG, t.message)
        PhotoConstants.showSnack(container_login, "Request AccessToken Failed")
    }

    override fun onRequestMeProfileSuccess(call: Call<Me>, response: Response<Me>) {

        if (response.isSuccessful && response.body() != null && isAuthorized()) {
            writeMeInfo(response.body()!!)
            retrofitClient!!.requestUserProfile(response.body()!!.username, this@LoginActivity)
        } else if (isAuthorized()) {
            retrofitClient!!.requestMeProfile(this@LoginActivity)
        }
    }

    override fun onRequestMeProfileFailed(call: Call<Me>, t: Throwable) {
        if (isAuthorized()) {
            retrofitClient!!.requestMeProfile(this)
        }
        t.printStackTrace()
        Log.e(TAG, t.message)
        PhotoConstants.showSnack(container_login, "Request Me Profile Failed")
    }

    override fun onRequestUserProfileSuccess(call: Call<User>, response: Response<User>) {
        if (response.isSuccessful && response.body() != null && isAuthorized()) {
            writeUserInfo(response.body()!!)
        } else if (isAuthorized()) {
            retrofitClient!!.requestUserProfile(me.username, this)
        }
    }

    override fun onRequestUserProfileFailed(call: Call<User>, t: Throwable) {
        t.printStackTrace()
        Log.e(TAG, t.message)
        PhotoConstants.showSnack(container_login, "Request User Profile Failed")
    }

    /**
     * Writing the data to Preference File
     */
    private fun writeToPrefFile(tokenData: AccessToken) {
        val editor = authPref!!.edit()
        editor.putString(PhotoConstants.KEY_ACCESS_TOKEN, tokenData.access_token)
        editor.putString(PhotoConstants.KEY_TOKEN_TYPE, tokenData.token_type)
        editor.putString(PhotoConstants.KEY_SCOPE_TYPE, tokenData.scope)
        editor.putInt(PhotoConstants.KEY_CREATED_AT, tokenData.created_at)
        editor.apply()
        authorized = true
    }

    private fun writeMeInfo(me: Me) {
        val editor = authPref!!.edit()
        editor.putString(PhotoConstants.KEY_USERNAME, me.username)
        editor.putString(PhotoConstants.KEY_FIRST_NAME, me.first_name)
        editor.putString(PhotoConstants.KEY_LAST_NAME, me.last_name)
        editor.putString(PhotoConstants.KEY_EMAIL, me.email)
        editor.apply()
    }

    private fun writeUserInfo(user: User) {
        val editor = authPref!!.edit()
        editor.putString(PhotoConstants.KEY_USERNAME, user.username)
        editor.putString(PhotoConstants.KEY_FIRST_NAME, user.first_name)
        editor.putString(PhotoConstants.KEY_LAST_NAME, user.last_name)
        editor.putString(PhotoConstants.KEY_AVATAR_PATH, user.profile_image.large)
        editor.putString(PhotoConstants.KEY_USER_ID, user.id)
        editor.apply()
    }

}
