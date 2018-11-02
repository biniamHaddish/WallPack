/*
 * DayTime:10/26/18 12:08 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.api

import com.berhane.biniam.wallpack.wallpack.model.data.AccessToken
import com.berhane.biniam.wallpack.wallpack.model.data.Me
import com.berhane.biniam.wallpack.wallpack.model.data.User
import retrofit2.Call
import retrofit2.http.*


interface Authorization {

    @POST("oauth/token")
    fun getAccessToken(@Query("client_id") client_id: String,
                       @Query("client_secret") client_secret: String,
                       @Query("redirect_uri") redirect_uri: String,
                       @Query("code") code: String,
                       @Query("grant_type") grant_type: String): Call<AccessToken>


}