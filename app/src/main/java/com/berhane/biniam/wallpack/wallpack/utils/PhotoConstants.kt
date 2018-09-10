/*
 * DayTime:9/5/18 2:40 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils

import com.berhane.biniam.wallpack.wallpack.BuildConfig


object PhotoConstants {
    // Auth Data
    const val ACCESS_KEY: String = BuildConfig.ACCESS_KEY
    const val SECRET_KEY: String = BuildConfig.SECRET_KEY

    val BASE_URL: String = "https://api.unsplash.com/"
    const val latest: String = "latest"
    const val oldest: String = "oldest"
    const val popular: String = "popular"
    const val perPage:Int = 20

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


}
