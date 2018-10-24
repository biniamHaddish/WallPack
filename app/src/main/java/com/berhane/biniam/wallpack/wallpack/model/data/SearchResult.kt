/*
 * DayTime:10/20/18 12:52 AM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchResult(var total: Int,
                        var totalPages: Int,
                        var results: List<Photos>
): Parcelable