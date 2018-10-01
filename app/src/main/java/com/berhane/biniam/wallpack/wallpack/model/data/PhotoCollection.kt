/*
 * DayTime:9/7/18 10:46 AM :
 * Year:2018 :
 * Author:bini :
 */


package com.berhane.biniam.wallpack.wallpack.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoCollection(var id: Int,
                           var title: String,
                           var description: String,
                           var published_at: String,
                           var updated_at: String,
                           var curated: Boolean,
                           var total_photos: Int,
                           var privateX: Boolean,
                           var share_key: String,
                           var cover_photo: Photos,
                           var user: User,
                           var links: CollectionLinks) : Parcelable
