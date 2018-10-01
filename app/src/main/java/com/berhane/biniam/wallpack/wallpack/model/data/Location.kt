/*
 * DayTime:9/21/18 1:49 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Location(
        var city: String,
        var country: String

) : Parcelable


