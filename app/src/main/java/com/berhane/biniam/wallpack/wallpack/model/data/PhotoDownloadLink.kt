/*
 * DayTime:9/5/18 2:38 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PhotoDownloadLinks(
        var self: String,
        var html: String,
        var download: String,
        var downlods:Int,
        var download_location: String) : Parcelable