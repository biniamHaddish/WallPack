/*
 * DayTime:9/5/18 2:40 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils

import android.app.Activity
import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import com.berhane.biniam.wallpack.wallpack.BuildConfig
import com.berhane.biniam.wallpack.wallpack.api.UnSplashApi
import com.berhane.biniam.wallpack.wallpack.model.data.Me
import com.berhane.biniam.wallpack.wallpack.model.data.User


object PhotoConstants {
    // Auth Data
//   unsplsh://wallpack.com
    const val ACCESS_KEY: String = BuildConfig.ACCESS_KEY
    const val SECRET_KEY: String = BuildConfig.SECRET_KEY

    const val BASE_URL: String = "https://api.unsplash.com/"
    const val UnSplash_URL = "https://unsplash.com/"
    const val OAuth2_Url: String = "https://unsplash.com/oauth/"
    const val Authorize_Url: String = "https://unsplash.com/oauth/authorize"
    const val Token_Url: String = "https://unsplash.com/oauth/token"

    const val UNSPLASH_UPLOAD_URL = "https://unsplash.com/submit"
    const val UNSPLASH_JOIN_URL = "https://unsplash.com/join"
    const val HOST: String = "wallpack.com"
    const val SCHEME: String = "unsplsh"
    const val GRANTE_TYPE: String = "authorization_code"
    const val REDIRECT_URI: String = "unsplsh://wallpack.com"
    const val UNSPLASH_UTM_PARAMETERS: String = "?utm_source=resplash&utm_medium=referral&utm_campaign=api-credit"
    const val LATEST: String = "latest"
    const val OLDEST: String = "oldest"
    const val POPULAR: String = "popular"
    const val TRENDING: String = "trending"
    const val COLLECTION: String = "collection"
    const val PHOTOS: String = "photos"
    const val LIKES: String = "likes"
    const val COLLECTION_TYPE_ALL: String = "all"
    const val COLLECTION_TYPE_CURATED: String = "Curated"
    const val COLLECTION_TYPE_FEATURED: String = "Featured"
    const val COLLECTION_BY_ID: String = "CollectionId"
    const val PERPAGE: Int = 25


    const val CATEGORY_TOTAL_NEW = 10
    const val CATEGORY_TOTAL_FEATURED = 20
    const val CATEGORY_BUILDINGS_ID = 30
    const val CATEGORY_FOOD_DRINK_ID = 40
    const val CATEGORY_NATURE_ID = 50
    const val CATEGORY_OBJECTS_ID = 60
    const val CATEGORY_PEOPLE_ID = 70
    const val CATEGORY_TECHNOLOGY_ID = 80

    const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    const val DOWNLOAD_PATH = "/Pictures/wallpack/"
    const val DOWNLOAD_PHOTO_FORMAT = ".jpg"
    const val DOWNLOAD_COLLECTION_FORMAT = ".zip"

    // Authorization of the user
    private val me: Me? = null
    private val user: User? = null

    private val access_token: String? = null
    private val username: String? = null
    private val first_name: String? = null
    private val last_name: String? = null
    private val email: String? = null
    private val avatar_path: String? = null
    private val authorized: Boolean = false

    const val   PREFERENCE_NAME = "wallPack_authorize_preference"
     var KEY_ACCESS_TOKEN = "access_token"
     var KEY_TOKEN_TYPE = "token_type"
     var KEY_SCOPE_TYPE = "scope"
     var KEY_CREATED_AT = "created_at"
     var KEY_USERNAME = "username"
     var KEY_FIRST_NAME = "first_name"
     var KEY_LAST_NAME = "last_name"
     var KEY_EMAIL = "email"
     var KEY_AVATAR_PATH = "avatar_path"
     var KEY_USER_ID="user_id"

    fun loginUrl(): String {
        return (UnSplash_URL + "oauth/authorize"
                + "?client_id=" + ACCESS_KEY
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=" + "code"
                + "&scope=" + "public+read_user+write_user+read_photos+write_photos+write_likes+write_followers+read_collections+write_collections")
    }

    /**
     * Will show snack message providing the message and the View
     */
   fun showSnack(view:View,message:String){
       Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
   }
}
