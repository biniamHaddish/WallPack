/*
 * DayTime:11/1/18 2:24 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils.connectivity

import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import com.berhane.biniam.wallpack.wallpack.BuildConfig
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.WallPack
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthenticationInterceptor : Interceptor {

    private var authPref: SharedPreferences? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        authPref = WallPack.applicationContext().getSharedPreferences(PhotoConstants.PREFERENCE_NAME, 0)
        val accessToken = authPref!!.getString(PhotoConstants.KEY_ACCESS_TOKEN, "")

        val request: Request
        if (!TextUtils.isEmpty(accessToken)) {
            if (accessToken != null) {
                Log.d("AuthenticationT", accessToken)
            }
            request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()
        } else {
            request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Client-ID " + BuildConfig.ACCESS_KEY)
                    .build()
        }
        return chain.proceed(request)
    }
}