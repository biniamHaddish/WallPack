/*
 * DayTime:10/24/18 11:54 AM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class CollectionSerachResult(var total: Int,
                             var totalPages: Int,
                             var results: List<PhotoCollection>
) : Parcelable