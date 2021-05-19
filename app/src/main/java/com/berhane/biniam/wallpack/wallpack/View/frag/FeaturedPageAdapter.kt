/*
 * DayTime:9/15/18 12:18 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.View.frag

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.SmartFragmentStatePagerAdapter

class FeaturedPageAdapter(fm: FragmentManager) : SmartFragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return FeaturedFragment.newInstance(PhotoConstants.LATEST)
            1 -> return FeaturedFragment.newInstance(PhotoConstants.POPULAR)
            2 -> return FeaturedFragment.newInstance(PhotoConstants.OLDEST)
        }
        return FeaturedFragment.newInstance(PhotoConstants.LATEST)
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "LATEST "
            1 -> return "POPULAR "
            2 -> return "OLDEST"
        }
        return null
    }
}