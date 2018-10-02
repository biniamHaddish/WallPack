/*
 * DayTime:10/1/18 4:18 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.os.Build
import android.support.annotation.Size
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.TextView

import com.berhane.biniam.wallpack.wallpack.R

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

        fun getStatusBarHeight(r: Resources): Int {
            var result = 0
            val resourceId = r.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = r.getDimensionPixelSize(resourceId)
            }
            return result
        }

        fun getNavigationBarHeight(r: Resources): Int {
//            if (!isNavigationBarShow) {
//                return 0
//            }
            var result = 0
            val resourceId = r.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = r.getDimensionPixelSize(resourceId)
            }
            return result
        }

        @Size(2)
        fun getScreenSize(context: Context): IntArray {
            if (DisplayUtils.isLandscape(context)) {
                return intArrayOf(context.resources.displayMetrics.widthPixels, context.resources.displayMetrics.heightPixels)
            } else {
                val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                if (manager == null) {
                    return intArrayOf(context.resources.displayMetrics.widthPixels, context.resources.displayMetrics.heightPixels + DisplayUtils.getNavigationBarHeight(context.resources))
                } else {
                    val size = Point()
                    val display = manager.defaultDisplay
                    display.getRealSize(size)
                    return intArrayOf(size.x, size.y)
                }
            }
        }

//        private val isNavigationBarShow: Boolean
//            get() {
//                val activity = WallPack.applicationContext()
//                if (activity != null) {
//                    val display = activity!!.getWindowManager().getDefaultDisplay()
//                    val size = Point()
//                    val realSize = Point()
//                    display.getSize(size)
//                    display.getRealSize(realSize)
//                    return realSize.y != size.y
//                } else {
//                    return false
//                }
//            }

        fun setWindowTop(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val icon = BitmapFactory.decodeResource(activity.resources, R.drawable.ic_launcher)
                val taskDescription = ActivityManager.TaskDescription(
                        activity.getString(R.string.app_name),
                        icon,
                        ThemeManager.getPrimaryColor(activity))
                activity.setTaskDescription(taskDescription)
                icon.recycle()
            }
        }

        fun setStatusBarStyle(activity: Activity, onlyDarkStatusBar: Boolean) {
            var flags = (activity.window.decorView.systemUiVisibility
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                if (onlyDarkStatusBar || !ThemeManager.getInstance(activity).isLightTheme) {
                    flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
            activity.window.decorView.systemUiVisibility = flags
        }

        fun setNavigationBarStyle(activity: Activity,
                                  onlyDarkNavigationBar: Boolean, translucent: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !isLandscape(activity)) {
                var flags = (activity.window.decorView.systemUiVisibility
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                if (translucent) {
                    flags = flags or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                }
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                if (!onlyDarkNavigationBar && ThemeManager.getInstance(activity).isLightTheme) {
                    if (translucent) {
                        activity.window.navigationBarColor = Color.argb((0.03 * 255).toInt(), 0, 0, 0)
                    } else {
                        activity.window.navigationBarColor = Color.rgb(241, 241, 241)
                    }
                } else {
                    flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    if (translucent) {
                        activity.window.navigationBarColor = Color.argb((0.2 * 255).toInt(), 0, 0, 0)
                    } else {
                        activity.window.navigationBarColor = Color.rgb(26, 26, 26)
                    }
                }
                activity.window.decorView.systemUiVisibility = flags
            }
        }

        fun cancelTranslucentNavigation(a: Activity) {
            a.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }

        fun changeTheme(c: Context) {
            ThemeManager.getInstance(c)
                    .setLightTheme(
                            c,
                            !ThemeManager.getInstance(c).isLightTheme)
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
    }

}
