/*
 * DayTime:10/7/18 10:55 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.View.frag

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.utils.SmartFragmentStatePagerAdapter

class PhotographerPager(fm: FragmentManager, Photographer: Photos) : SmartFragmentStatePagerAdapter(fm) {

    private val photos = Photographer

    override fun getItem(position: Int): Fragment {
      //  if (photos != null) {

            when (position) {
                0 -> return PhotographerDetailsFragment.newInstance(this.photos)
                1 -> return PhotographerLikesFragment.newInstance(this.photos)
                2 -> return PhotographerCollectionFragment.newInstance(this.photos)
            }

            return PhotographerDetailsFragment.newInstance(this.photos)


    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return photos.user.total_photos.toString() + "\tPHOTOS "
            1 -> return photos.user.total_likes.toString() + "\tLIKES "
            2 -> return photos.user.total_collections.toString() + "\tCOLLECTION"
        }
        return null
    }
}