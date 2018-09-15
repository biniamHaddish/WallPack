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
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.View.frag.CollectionFragment
import com.berhane.biniam.wallpack.wallpack.View.frag.FeaturedPageAdapter
import com.berhane.biniam.wallpack.wallpack.View.frag.NewPhotosFragment
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

                val activity= FeaturedActivity()
                loadActivity(activity)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.featured_activity)
        featured_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)

        val fragmentAdapter = FeaturedPageAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.featured_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.curatedToolbar) {
            Toast.makeText(this,"biniam",Toast.LENGTH_LONG).show()
            true
        } else super.onOptionsItemSelected(item)
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