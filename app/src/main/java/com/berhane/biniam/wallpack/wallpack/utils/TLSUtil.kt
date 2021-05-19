/*
 * DayTime:10/31/18 4:23 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

open class TLSUtil {
    protected fun getClientBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
        return builder
    }
}

