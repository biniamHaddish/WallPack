/*
 * DayTime:10/7/18 10:55 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.View.frag

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.SmartFragmentStatePagerAdapter

class PhotographerPager(fm: FragmentManager) : SmartFragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return FeaturedFragment.newInstance(PhotoConstants.PHOTOS)
            1 -> return FeaturedFragment.newInstance(PhotoConstants.LIKES)
            2 -> return FeaturedFragment.newInstance(PhotoConstants.COLLECTION)
        }
        return FeaturedFragment.newInstance(PhotoConstants.LATEST)
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "PHOTOS "
            1 -> return "LIKES "
            2 -> return "COLLECTION"
        }
        return null
    }
}