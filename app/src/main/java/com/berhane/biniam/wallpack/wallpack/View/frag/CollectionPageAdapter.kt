/*
 * DayTime:9/24/18 3:39 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.View.frag

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.SmartFragmentStatePagerAdapter

class CollectionPageAdapter(fm: FragmentManager) : SmartFragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return CollectionFragment.newInstance(PhotoConstants.COLLECTION_TYPE_ALL)
            1 -> return CollectionFragment.newInstance(PhotoConstants.COLLECTION_TYPE_CURATED)
            2 -> return CollectionFragment.newInstance(PhotoConstants.COLLECTION_TYPE_FEATURED)

        }
        return CollectionFragment.newInstance(PhotoConstants.COLLECTION_TYPE_ALL)
    }
    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "All "
            1 -> return "Curated "
            2 -> return "Featured"
        }
        return null
    }

}