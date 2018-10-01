/*
 * DayTime:9/5/18 2:39 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PhotoUrls(var raw: String,
                     var full: String,
                     var regular: String,
                     var small: String,
                     var thumb: String) : Parcelable

