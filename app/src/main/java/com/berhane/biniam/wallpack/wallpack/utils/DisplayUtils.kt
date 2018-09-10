/*
 * DayTime:9/6/18 6:31 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Build
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.applyDimension
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.TextView

/**
 * Display utils.
 *
 * An utils class that make operations of display easier.
 *
 */

class DisplayUtils(context: Context) {

    private var dpi = 0

    init {
        dpi = context.resources.displayMetrics.densityDpi
    }

    fun dpToPx(dp: Int): Float {
        return if (dpi == 0) {
            0f
        } else (dp * (dpi / 160.0)).toFloat()
    }

    companion object {
        private val DEFAULT_DELAY: Long = 0
        fun getStatusBarHeight(r: Resources): Int {
            var result = 0
            val resourceId = r.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = r.getDimensionPixelSize(resourceId)
            }
            return result
        }

        fun setTypeface(c: Context, t: TextView) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                t.typeface = Typeface.createFromAsset(c.assets, "fonts/Courier.ttf")
            }
        }

        fun abridgeNumber(num: Int): String {
            var num = num
            if (num < 1000) {
                return num.toString()
            } else {
                num = num / 100
                return (num / 10.0).toString() + "K"
            }
        }

        fun isTabletDevice(context: Context): Boolean {
            return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
        }

        fun isLandscape(context: Context): Boolean {
            return context.resources
                    .configuration
                    .orientation == Configuration.ORIENTATION_LANDSCAPE
        }

        fun convertDpToPx(context: Context, dp: Float): Int {
            return applyDimension(COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
        }

        fun convertPxToDp(context: Context, dp: Float): Float {
            return dp * context.resources.displayMetrics.density
        }
        fun configuredHideYView(v: View) {
            v.scaleY = 0f
            v.pivotY = 0f
        }

        fun showViewByScale(v: View): ViewPropertyAnimator {

            return v.animate().setStartDelay(DEFAULT_DELAY)
                    .scaleX(1f).scaleY(1f)
        }

        fun hideViewByScaleXY(v: View): ViewPropertyAnimator {
            return hideViewByScale(v, DEFAULT_DELAY, 0, 0)
        }

        fun hideViewByScaleY(v: View): ViewPropertyAnimator {
            return hideViewByScale(v, DEFAULT_DELAY, 1, 0)
        }

        private fun hideViewByScale(v: View, delay: Long, x: Int, y: Int): ViewPropertyAnimator {

            return v.animate().setStartDelay(delay.toLong())
                    .scaleX(x.toFloat()).scaleY(y.toFloat())
        }
    }
}
