/*
 * DayTime:9/18/18 11:30 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils.adapter

import android.widget.FrameLayout
import android.widget.ImageView
import com.berhane.biniam.wallpack.wallpack.model.data.Photos

interface PhotoClickListner {
    fun PhotoClicked(position: Int, target: FrameLayout, photo:Photos)
}