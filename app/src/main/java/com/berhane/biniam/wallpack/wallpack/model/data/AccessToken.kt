/*
 * DayTime:10/26/18 12:10 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class AccessToken(
        var access_token: String,
        var token_type: String,
        var scope: String,
        var created_at: Int
) : Parcelable
/**
 * access_token : 091343ce13c8ae780065ecb3b13dc903475dd22cb78a05503c2e0c69c5e98044
 * token_type : bearer
 * scope : public read_photos write_photos
 * created_at : 1436544465
 */



