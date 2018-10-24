/*
 * DayTime:10/24/18 11:53 AM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SearchUserResult(var total: Int,
                       var totalPages: Int,
                       var results: List<User>
) : Parcelable