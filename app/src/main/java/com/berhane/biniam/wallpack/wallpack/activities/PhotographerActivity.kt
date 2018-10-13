/*
 * DayTime:9/23/18 1:41 PM:
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
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.View.frag.PhotographerCollectionFragment
import com.berhane.biniam.wallpack.wallpack.View.frag.PhotographerDetailsFragment
import com.berhane.biniam.wallpack.wallpack.View.frag.PhotographerPager
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.photographer_activity.*


class PhotographerActivity : AppCompatActivity() {

    val TAG = "PhotographerAc"
    private lateinit var context: Context
    private val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photographer_activity)
        context = this


        val photographerImg = findViewById<ImageView>(R.id.photographer_img)
        // Setting the values for the above vars
        val intent = intent
        val photos = intent.getStringExtra("photographer")
        val photographerInfo = Gson().fromJson<Photos>(photos, Photos::class.java)

        // Setting the photographer Image from the photo Details
        Glide.with(this@PhotographerActivity)
                .load(photographerInfo.user.profile_image.large)
                .into(photographerImg)

        photographer_name.text = photographerInfo.user.name
        photographer_location.text = photographerInfo.user.location
        photographer_link.text = photographerInfo.user.portfolio_url
        photographer_bio.text = photographerInfo.user.bio
//        badge_tv.text = photographerInfo.user.badge.slug


        //pager
        val viewPager = findViewById<ViewPager>(R.id.photographer_viewpager)
        val tabsCollection = findViewById<TabLayout>(R.id.photographer_tabs)
        val fragmentAdapter = PhotographerPager(supportFragmentManager, photographerInfo)
        viewPager.adapter = fragmentAdapter
        tabsCollection.setupWithViewPager(viewPager)

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                viewPager.currentItem = position
            }
        })

    }
}