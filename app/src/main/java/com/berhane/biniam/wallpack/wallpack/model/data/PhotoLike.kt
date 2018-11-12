/*
 * DayTime:11/10/18 6:59 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoLike(
        var user: User,
        var photo: Photos
) : Parcelable
