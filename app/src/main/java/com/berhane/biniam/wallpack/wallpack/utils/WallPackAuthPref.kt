/*
 * DayTime:11/1/18 11:13 AM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.utils

import android.content.Context
import android.content.SharedPreferences

class WallPackAuthPref(context:Context) : SharedPreferences.OnSharedPreferenceChangeListener {

    var acessToken: String = ""
    var tokenType: String = ""
    var isAuthorized: Boolean = false
    var createdAt: Long = 0
    val prefs: SharedPreferences = context.getSharedPreferences(PhotoConstants.PREFERENCE_NAME, 0)

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}