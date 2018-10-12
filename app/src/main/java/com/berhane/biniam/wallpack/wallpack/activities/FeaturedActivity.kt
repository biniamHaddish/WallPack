/*
 * DayTime:9/15/18 12:22 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.activities

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.View.frag.FeaturedPageAdapter
import com.berhane.biniam.wallpack.wallpack.utils.ZoomOutPageTransformer

class FeaturedActivity : AppCompatActivity() {

    private lateinit var context: Context
    private val manager = supportFragmentManager
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.featured_activity)

        viewPager = findViewById<ViewPager>(R.id.featured_viewpager)
        val tabsCollection = findViewById<TabLayout>(R.id.featured_tabs)
        val fragmentAdapter = FeaturedPageAdapter(supportFragmentManager)
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
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

    /**
     *  load Fragment here using the fragment obj
     */
    private fun loadFragment(fragment: Fragment) {
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.featuredFragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

//    private fun loadActivity(activity: Activity) {
//        val intent = Intent(context, activity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//        startActivity(intent)
//    }
}