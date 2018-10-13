/*
 * DayTime:10/13/18 11:41 AM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Badge(
        var title: String,
        var primary: Boolean,
        var slug: String,
        var link: String
) : Parcelable

