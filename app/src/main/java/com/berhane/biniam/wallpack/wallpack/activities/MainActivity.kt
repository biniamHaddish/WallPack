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
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.View.frag.CollectionFragment
import com.berhane.biniam.wallpack.wallpack.View.frag.NewPhotosFragment
import kotlinx.android.synthetic.main.activity_main.*
import com.berhane.biniam.wallpack.wallpack.R.id.toolbar
import android.support.design.widget.AppBarLayout




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

                val activity = FeaturedActivity()
                loadActivity(activity)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


//    private val mOnDrawerNavigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener { item ->
//        when (item.itemId) {
//
//            R.id.photo_collection -> {
//                val collectionFragment = CollectionFragment.newInstance()
//                loadFragment(collectionFragment)
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.featured -> {
//                val activity = FeaturedActivity()
//                loadActivity(activity)
//                return@OnNavigationItemSelectedListener true
//            }
//        }
//        //drawerLayout?.closeDrawer(GravityCompat.START)
//        true
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

//        val accountImage= findViewById<ImageView>(R.id.AccountImage)
//
//        accountImage.setOnClickListener {
//            Toast.makeText(this,"this is an entry point for the App",Toast.LENGTH_LONG).show()
//        }
        // val headerView = drawerNavigationView.inflateHeaderView(R.layout.nav_header)
        //Navigation Listener
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //drawerNavigationView.setNavigationItemSelectedListener(mOnDrawerNavigationItemSelectedListener)


        val localLayoutParams = window.attributes
        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags)
        context = this
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
        startActivity(intent)
    }


}







