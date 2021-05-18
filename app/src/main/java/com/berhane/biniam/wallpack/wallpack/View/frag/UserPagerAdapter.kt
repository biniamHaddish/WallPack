/*
 * DayTime:11/6/18 12:39 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.View.frag

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.berhane.biniam.wallpack.wallpack.model.data.User
import com.berhane.biniam.wallpack.wallpack.utils.SmartFragmentStatePagerAdapter

class UserPagerAdapter(fm: FragmentManager, userData: User) : SmartFragmentStatePagerAdapter(fm) {

    private val user = userData
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return UserPhotos.newInstance(this.user)
            1 -> return UserLikes.newInstance(this.user)
            2 -> return UserPhotoCollection.newInstance(this.user)
        }
        return UserPhotos.newInstance(this.user)
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return user.total_photos.toString() + "\tPHOTOS "
            1 -> return user.total_likes.toString() + "\tLIKES "
            2 -> return user.total_collections.toString() + "\tCOLLECTION"
        }
        return null
    }
}