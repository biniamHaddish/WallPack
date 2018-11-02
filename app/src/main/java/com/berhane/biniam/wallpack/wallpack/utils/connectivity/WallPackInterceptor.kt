/*
 * DayTime:9/5/18 4:43 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils.connectivity

import com.berhane.biniam.wallpack.wallpack.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class WallPackInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {

        val request: Request = chain!!.request()
                .newBuilder()
                .addHeader("Authorization", "Client-ID " + BuildConfig.ACCESS_KEY)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build()
        return chain.proceed(request)
    }
}