/*
 * DayTime:9/5/18 3:53 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils

import android.app.Application
import android.content.Context
import com.berhane.biniam.wallpack.wallpack.R
import android.content.pm.PackageManager
import android.os.Build
import android.app.Activity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable


class WallPack : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: WallPack? = null
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun getCircularProgress() {
            val circularProgressDrawable = CircularProgressDrawable(applicationContext())
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.setColorSchemeColors(ContextCompat.getColor(applicationContext(), R.color.tokyoColorAccent))
            circularProgressDrawable.start()
        }


        fun isStoragePermissionGranted(activity: Activity): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    return true
                } else {
                    ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                    return false
                }
            } else {
                return true
            }
        }



    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any
        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = WallPack.applicationContext()
    }
}