/*
 * DayTime:9/5/18 3:53 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils

import android.app.Application
import android.content.Context

class WallPack : Application() {

    init {
        instance = this
    }
    companion object {
        private var instance: WallPack? = null
        fun applicationContext() : Context {
            return instance!!.applicationContext
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