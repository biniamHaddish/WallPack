/*
 * DayTime:10/7/18 10:55 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.View.frag

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.model.data.User
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.SmartFragmentStatePagerAdapter

class PhotographerPager(fm: FragmentManager, Photographer: Photos) : SmartFragmentStatePagerAdapter(fm) {

    private val  photos = Photographer

    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> return PhotographerDetailsFragment.newInstance(this.photos)
            1 -> return PhotographerDetailsFragment.newInstance(this.photos)
            2 -> return PhotographerDetailsFragment.newInstance(this.photos)
        }
        return PhotographerDetailsFragment.newInstance(this.photos)
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