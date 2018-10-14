/*
 * DayTime:9/15/18 11:24 PM :
 * Year:2018 :
 * Author:bini :
 */

/*
 * DayTime:9/5/18 2:40 PM :
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
import android.support.v7.widget.Toolbar
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.View.frag.NewPhotosFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var context: Context
    private val manager = supportFragmentManager
    val TAG: String = "MainActivity"

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val newPhotoFragment = NewPhotosFragment.newInstance()
                loadFragment(newPhotoFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.photo_collection -> {
                val activity = CollectionActivity()
                loadActivity(activity)
                return@OnNavigationItemSelectedListener true
            }
            R.id.photo_search -> {
                // search fragment should be here
                return@OnNavigationItemSelectedListener true
            }
            R.id.featured -> {
                val activity = FeaturedActivity()
                loadActivity(activity)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val newPhotoFragment = NewPhotosFragment.newInstance()
        loadFragment(newPhotoFragment)
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

    fun loadActivity(activity: Activity) {
        val intent = Intent(context, activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }


}







