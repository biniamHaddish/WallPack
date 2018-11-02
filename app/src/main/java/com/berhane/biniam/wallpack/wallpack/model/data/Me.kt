/*
 * DayTime:10/26/18 12:44 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class Me(
        var id: String,
        var username: String,
        var first_name: String,
        var last_name: String,
        var portfolio_url: String,
        var bio: String,
        var location: String,
        var total_likes: Int ,
        var total_photos: Int ,
        var total_collections: Int ,
        var followed_by_user: Boolean ,
        var downloads: Int ,
        var uploads_remaining: Int ,
        var instagram_username: String,
        var email: String,
        var links: UserLinks
)


