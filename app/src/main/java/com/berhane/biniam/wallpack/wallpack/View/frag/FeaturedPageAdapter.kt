/*
 * DayTime:9/15/18 12:18 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.View.frag

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class FeaturedPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                CollectionFragment()
            }
            1 ->
                CuratedFrag()
            else -> {
                return NewPhotosFragment()
            }

        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Collection "
            1 -> "Curated "
            else -> {
                return "New"
            }
        }
    }
}