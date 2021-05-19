/*
 * DayTime:10/24/18 12:04 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.View.frag

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.berhane.biniam.wallpack.wallpack.utils.SmartFragmentStatePagerAdapter

class SearchPagerAdapter(fm: FragmentManager, query: String) : SmartFragmentStatePagerAdapter(fm) {

    private val mQuery = query

    override fun getItem(position: Int): Fragment {

            when (position) {
                0 -> return SearchPhotoFragment.newInstance(this.mQuery)
                1 -> return SearchUserFragment.newInstance(this.mQuery)
                2 -> return SearchCollection.newInstance(this.mQuery)
            }
        return SearchPhotoFragment.newInstance(this.mQuery)
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "PHOTOS "
            1 -> return "USER "
            2 -> return "COLLECTION"
        }
        return null
    }
}