/*
 * DayTime:9/23/18 1:41 PM:
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.R.id.*
import com.berhane.biniam.wallpack.wallpack.View.frag.PhotographerPager
import com.berhane.biniam.wallpack.wallpack.View.frag.UserPagerAdapter
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.model.data.User
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.connectivity.RetrofitClient
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.photographer_activity.*
import retrofit2.Call
import retrofit2.Response


class PhotographerActivity : AppCompatActivity(), RetrofitClient.OnRequestUserProfileListener {

    val TAG = "PhotographerAc"
    private lateinit var context: Context
    private var retrofitClient: RetrofitClient? = null
    private var authPref: SharedPreferences? = null
    private var avatarInfo: String? = null
    private var accessToken: String? = null
    private var username: String = ""
    private var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photographer_activity)
        context = this

        //Init the value of the Photographer or App user here
        retrofitClient = RetrofitClient.getRetrofitClient()
        authPref = context.getSharedPreferences(PhotoConstants.PREFERENCE_NAME, 0)
        avatarInfo = authPref!!.getString(PhotoConstants.KEY_AVATAR_PATH, "")
        accessToken = authPref!!.getString(PhotoConstants.KEY_ACCESS_TOKEN, "")
        username = authPref!!.getString(PhotoConstants.KEY_USERNAME, "").toString()
        email = authPref!!.getString(PhotoConstants.KEY_EMAIL, "").toString()


        val photographerImg = findViewById<ImageView>(R.id.photographer_img)
        // Setting the values for the above vars

        val intent = intent
        val photos = intent.getStringExtra("photographer")
        val photographerInfo = Gson().fromJson<Photos>(photos, Photos::class.java)

        if (photographerInfo != null) {
            // Setting the photographer Image from the photo Details
            Glide.with(this@PhotographerActivity)
                    .load(photographerInfo.user.profile_image.large)
                    .into(photographerImg)

            photographer_name.text = photographerInfo.user.name
            photographer_location.text = photographerInfo.user.location
            photographer_link.text = photographerInfo.user.portfolio_url
            photographer_bio.text = photographerInfo.user.bio

            //------------------------------Pager-----------------------------
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
        } else {
            retrofitClient!!.requestUserProfile(username, this@PhotographerActivity)
            // Setting the photographer Image from the photo Details
            Glide.with(this@PhotographerActivity)
                    .load(avatarInfo)
                    .into(photographerImg)


        }
    }

    private fun getUserPhotosCollectionsLikes(user: User) {

        if (user != null) {
            photographer_name.text = user!!.name
            photographer_location.text = user!!.location
            photographer_link.text = email
            photographer_bio.text = user!!.bio

            val viewPager = findViewById<ViewPager>(R.id.photographer_viewpager)
            val tabsCollection = findViewById<TabLayout>(R.id.photographer_tabs)
            val fragmentAdapter = UserPagerAdapter(supportFragmentManager, user)

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

    override fun onRequestUserProfileSuccess(call: Call<User>, response: Response<User>) {
        if (response.isSuccessful && response.body() != null) {
            getUserPhotosCollectionsLikes(response.body()!!)
        } else if (!TextUtils.isEmpty(username)) {
          //  retrofitClient!!.requestUserProfile(username, this)
        }
    }

    override fun onRequestUserProfileFailed(call: Call<User>, t: Throwable) {
        t.printStackTrace()
        t.message?.let { Log.d(TAG, it) }
    }
}