/*
 * DayTime:11/13/18 7:18 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Stats(
        var downloads: Int,
        var views: Int,
        var likes: Int,
        var links: Links
) : Parcelable
