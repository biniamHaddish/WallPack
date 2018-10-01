/*
 * DayTime:9/5/18 2:38 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CategoryLinks(var self: String,
                         var photos: String) : Parcelable