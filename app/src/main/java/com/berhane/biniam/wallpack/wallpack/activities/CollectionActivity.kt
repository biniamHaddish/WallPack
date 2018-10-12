/*
 * DayTime:9/24/18 4:07 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.activities

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.View.frag.CollectionPageAdapter


class CollectionActivity : AppCompatActivity() {


    private lateinit var context: Context
    private val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.collection_activity)
        context = this

        val viewPager = findViewById<ViewPager>(R.id.collection_viewPager)
        val tabsCollection = findViewById<TabLayout>(R.id.collection_tabs)
        val fragmentAdapter = CollectionPageAdapter(supportFragmentManager)

        viewPager.adapter = fragmentAdapter
        tabsCollection.setupWithViewPager(viewPager)

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
              // Timber.d("%d",state)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
              //  Toast.makeText(context, "State \t$position offset\t$positionOffset positionPixel \t $positionOffsetPixels", Toast.LENGTH_LONG).show()

            }

            override fun onPageSelected(position: Int) {
                   viewPager.currentItem = position
            }

        })




    }
}