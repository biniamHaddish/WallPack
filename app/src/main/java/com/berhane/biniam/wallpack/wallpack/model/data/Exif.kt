/*
 * DayTime:9/21/18 1:48 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Exif(
        var make: String,
        var model: String,
        var exposure_time: String,
        var aperture: String,
        var focal_length: String,
        var iso: Int
) : Parcelable