/*
 * DayTime:9/7/18 10:46 AM :
 * Year:2018 :
 * Author:bini :
 */

/*
 * DayTime:9/5/18 2:38 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PhotoCategory(var id: Int,
                         var title: String,
                         var photo_count: Int,
                         var links: CategoryLinks) : Parcelable