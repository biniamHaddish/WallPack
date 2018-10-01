/*
 * DayTime:9/5/18 3:53 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils

import android.app.Application
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.widget.CircularProgressDrawable
import com.berhane.biniam.wallpack.wallpack.R

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
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any
        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = WallPack.applicationContext()
    }
}