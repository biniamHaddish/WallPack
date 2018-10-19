/*
 * DayTime:9/15/18 12:22 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.View.frag.FeaturedPageAdapter
import com.berhane.biniam.wallpack.wallpack.View.frag.NewPhotosFragment
import com.berhane.biniam.wallpack.wallpack.utils.ZoomOutPageTransformer

class FeaturedActivity : AppCompatActivity() {

    private lateinit var context: Context
    private val manager = supportFragmentManager
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.featured_activity)
        this.context=this
        viewPager = findViewById(R.id.featured_viewpager)

        val tabsCollection = findViewById<TabLayout>(R.id.featured_tabs)
        val fragmentAdapter = FeaturedPageAdapter(supportFragmentManager)
        //viewPager.setPageTransformer(true, ZoomOutPageTransformer())
        viewPager.adapter = fragmentAdapter
        tabsCollection.setupWithViewPager(viewPager)

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
                // Toast.makeText(context, "State \t$state", Toast.LENGTH_LONG).show()
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Toast.makeText(context, "State \t$position offset\t$positionOffset positionPixel \t $positionOffsetPixels", Toast.LENGTH_LONG).show()
            }

            override fun onPageSelected(position: Int) {
                viewPager.currentItem = position
            }

        })
        // bottom Navigation
        bottomNavSetUp()
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    private fun loadActivity(activity: Activity) {
        val intent = Intent(context, activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun fetchColor(@ColorRes color: Int): Int {
        return ContextCompat.getColor(context, color)
    }

    private fun bottomNavSetUp() {
        // preparing the view
        val bottomNavigation = findViewById<View>(R.id.navigation) as AHBottomNavigation
        val home = AHBottomNavigationItem(R.string.title_home, R.drawable.ic_round_home_24px, R.color.whiteSmoke)
        val collection = AHBottomNavigationItem(R.string.photo_collections, R.drawable.ic_round_photo_library_24px, R.color.whiteSmoke)
        val features = AHBottomNavigationItem(R.string.trending, R.drawable.ic_round_trending_up_24px, R.color.whiteSmoke)
        val searching = AHBottomNavigationItem(R.string.search_menu_title, R.drawable.ic_magnify, R.color.whiteSmoke)

        // Setting the Colors
        bottomNavigation.defaultBackgroundColor = Color.WHITE
        bottomNavigation.accentColor = fetchColor(R.color.tokyoColorAccent)
        bottomNavigation.inactiveColor = fetchColor(R.color.colorBottomNavigationInactive)

        // behaviour for the bottom nav
        bottomNavigation.isTranslucentNavigationEnabled = true
        bottomNavigation.isBehaviorTranslationEnabled = true
        // Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.isForceTint = true
        bottomNavigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

        //Adding the elements to the bottom Navigation
        bottomNavigation.addItem(home)
        bottomNavigation.addItem(collection)
        bottomNavigation.addItem(features)
        bottomNavigation.addItem(searching)

        // Setting the very 1st item as home screen.
        bottomNavigation.currentItem = 2
        // Set listeners
        bottomNavigation.setOnTabSelectedListener { position, wasSelected ->
            // Do something cool here...
            val mainActivity=MainActivity()
            val activity = CollectionActivity()
            val featuredActivity = FeaturedActivity()

            when (position) {
                0 -> loadActivity(mainActivity)
                1 -> loadActivity(activity)
                2 -> loadActivity(featuredActivity)
                3 -> Toast.makeText(context, "" + position, Toast.LENGTH_LONG).show()
            }
            true
        }
    }
}