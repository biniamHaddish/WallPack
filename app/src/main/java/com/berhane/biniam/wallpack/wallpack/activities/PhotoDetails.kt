/*
 * DayTime:9/20/18 9:10 AM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.google.gson.Gson
import kotlinx.android.synthetic.main.detailed_photo.*

class PhotoDetails : AppCompatActivity() {

    var context: Context? = null
    val TAG = "PhotoDetails"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_photo)
        context = this
        val imageView = findViewById<ImageView>(R.id.image_detailed)
        val photographerImg = findViewById<ImageView>(R.id.photographerIMGView)
        val photographerName = findViewById<TextView>(R.id.photographerName)
//        val detailed_like_btn = findViewById<ImageButton>(R.id.detailed_like_btn)
        val detailed_user_location = findViewById<TextView>(R.id.detailed_user_location)
//        val published_date = findViewById<TextView>(R.id.published_date)
//        val tvLikes = findViewById<TextView>(R.id.tvLikes)
//        val tvDownloads = findViewById<TextView>(R.id.tvDownloads)
//        val tvColor = findViewById<TextView>(R.id.tvColor)


        val photos: Photos = Gson().fromJson<Photos>(intent.getStringExtra("Photo"), Photos::class.java)
//            display metrics


        val displayMetrics = (context as PhotoDetails).getResources().getDisplayMetrics()
        val imageHeight: Float = displayMetrics.widthPixels / (photos.width.toFloat() / photos.height.toFloat())

        photographerName.text = "By " + photos.user.first_name
        detailed_user_location.text = photos.user.location

        // load the image here
        Glide.with(this@PhotoDetails)
                .load(photos.urls.regular)
                .apply(RequestOptions().priority(Priority.HIGH)
                        .placeholder(R.drawable.placeholder))
                .into(imageView)


        Glide.with(this@PhotoDetails)
                .load(photos.user.profile_image.medium)
                .apply(RequestOptions().priority(Priority.HIGH)
                        .placeholder(R.drawable.ic_account_circle))
                .into(photographerImg)

        Log.d(TAG, "userImage" + photos.user.profile_image.medium)
    }

}