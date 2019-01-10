/*
 * DayTime:11/14/18 12:08 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Links(
        var self: String,
        var html: String,
        var download: String,
        var download_location: String
) : Parcelable

