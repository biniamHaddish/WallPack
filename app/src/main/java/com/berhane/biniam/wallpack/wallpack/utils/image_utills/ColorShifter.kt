/*
 * DayTime:9/6/18 6:12 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils.image_utills

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import java.util.regex.Pattern

class ColorShifter {
    companion object {
        /**
         * Compute the background color for item view in photo list or collection list.
         *
         * @param context Context.
         * @param color   A string that can be converted to a color without "#". For example, "000000".
         */
        fun computeCardBackgroundColor(context: Context, color: String): Int {
            var color = color
            if (TextUtils.isEmpty(color) || !Pattern.compile("^#[a-fA-F0-9]{6}").matcher(color).matches() && !Pattern.compile("^[a-fA-F0-9]{6}").matcher(color).matches()) {
                return Color.argb(0, 0, 0, 0)
            } else {
                if (Pattern.compile("^[a-fA-F0-9]{6}").matcher(color).matches()) {
                    color = "#" + color
                }
                val backgroundColor = Color.parseColor(color)
                val red = backgroundColor and 0x00FF0000 shr 16
                val green = backgroundColor and 0x0000FF00 shr 8
                val blue = backgroundColor and 0x000000FF

                return Color.rgb(
                        (red + (255 - red) * 0.7).toInt(),
                        (green + (255 - green) * 0.7).toInt(),
                        (blue + (255 - blue) * 0.7).toInt())

            }
        }
    }
}