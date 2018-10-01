/*
 * DayTime:9/5/18 2:39 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UserProfileImageUrl(var small:String,
                               var medium:String,
                               var large:String) : Parcelable
