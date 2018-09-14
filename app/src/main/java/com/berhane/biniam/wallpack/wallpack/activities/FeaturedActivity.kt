/*
 * DayTime:9/14/18 1:13 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.View.frag.CollectionFragment
import com.berhane.biniam.wallpack.wallpack.View.frag.NewPhotosFragment
import kotlinx.android.synthetic.main.activity_main.*

class FeaturedActivity:AppCompatActivity() {
    private lateinit var context: Context
    private val manager = supportFragmentManager


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val newPhotoFragment = NewPhotosFragment.newInstance()
                loadFragment(newPhotoFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.photo_collection -> {
                val collectionFragment = CollectionFragment.newInstance()
                loadFragment(collectionFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.photo_search -> {
                // search fragment should be here
                return@OnNavigationItemSelectedListener true
            }
            R.id.featured -> {
//                val curatedFrag = CuratedFrag.newInstance()
//                loadFragment(curatedFrag)

                val activity=FeaturedActivity()
                loadActivity(activity)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.featured_activity)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val localLayoutParams = window.attributes
        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags)
        context = this
    }


    /**
     *  load Fragment here using the fragment obj
     */
    fun loadFragment(fragment: Fragment) {
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun loadActivity(activity: Activity){
        val intent = Intent(context, activity::class.java)
        startActivity(intent)
    }
}