/*
 * DayTime:9/15/18 12:22 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.View.frag.CollectionFragment
import com.berhane.biniam.wallpack.wallpack.View.frag.FeaturedPageAdapter
import com.berhane.biniam.wallpack.wallpack.View.frag.NewPhotosFragment
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import kotlinx.android.synthetic.main.featured_activity.*

class FeaturedActivity : AppCompatActivity() {
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
                val collectionFragment = CollectionFragment.newInstance(PhotoConstants.COLLECTION_TYPE_ALL)
                loadFragment(collectionFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.photo_search -> {
                // search fragment should be here
                return@OnNavigationItemSelectedListener true
            }
            R.id.featured -> {
//                val curatedFrag = FeaturedFragment.newInstance()
//                loadFragment(curatedFrag)

                val activity = FeaturedActivity()
                loadActivity(activity)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.featured_activity)

        val viewPager = findViewById<ViewPager>(R.id.featured_viewpager)
        val tabsCollection = findViewById<TabLayout>(R.id.featured_tabs)
        val fragmentAdapter = FeaturedPageAdapter(supportFragmentManager)

        viewPager.adapter = fragmentAdapter
        tabsCollection.setupWithViewPager(viewPager)

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
                //Toast.makeText(context, "State \t$state", Toast.LENGTH_LONG).show()
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //  Toast.makeText(context, "State \t$position offset\t$positionOffset positionPixel \t $positionOffsetPixels", Toast.LENGTH_LONG).show()

            }

            override fun onPageSelected(position: Int) {
                viewPager.currentItem = position
            }

        })

    }

    /**
     *  load Fragment here using the fragment obj
     */
    private fun loadFragment(fragment: Fragment) {
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.featuredFragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun loadActivity(activity: Activity) {
        val intent = Intent(context, activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}