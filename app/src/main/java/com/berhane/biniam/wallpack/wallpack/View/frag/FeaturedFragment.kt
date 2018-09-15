/*
 * DayTime:9/15/18 12:15 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.View.frag

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.berhane.biniam.wallpack.wallpack.R


class FeaturedFragment :  Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fetured_fragment, container, false)
    }
}