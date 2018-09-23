/*
 * DayTime:9/21/18 1:44 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data

data class PhotoDetails(
        var id: String,
        var created_at: String,
        var width: Int,
        var height: Int,
        var color: String,
        var downloads: Int,
        var likes: Int,
        var liked_by_user: Boolean,
        var exif: Exif,
        var location: Location,
        var user: User,
        var photos: Photos
)