/*
 * DayTime:9/20/18 9:10 AM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.CompoundButton
import android.widget.ImageView
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.image_utills.NestedScrollPhotoView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.detailed_photo.*
import java.text.NumberFormat
import java.util.*


class PhotoDetails : AppCompatActivity() {

    var context: Context? = null
    val TAG = "PhotoDetails"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_photo)


        val fab: View = findViewById(R.id.fab_info)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
        }

        // Making the status bar translucent

        val localLayoutParams = window.attributes
        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags)
        context = this
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        val imageView = findViewById<NestedScrollPhotoView>(R.id.image_detailed)
        // val progressbar=findViewById<ProgressBar>(R.id.detail_progress)
        val photographerImg = findViewById<ImageView>(R.id.photographerIMGView)

        //
        val photos = Gson().fromJson<Photos>(intent.getStringExtra("Photo"), Photos::class.java)

        photographerName.text = "By " + photos.user.name
//        detailed_user_location.text = photos.user.location
//        published_date.text = photos.created_at.split("T")[0]
//        likes_details.text = NumberFormat.getInstance(Locale.US).format(photos.likes) + " Likes"
//        details_download.text = NumberFormat.getInstance(Locale.US).format(photos.downloads)
//        color_details.text = photos.color

        //Circular drawable until the detailed image loads
        var drawable = resources.getDrawable(R.drawable.ic_palette, resources.newTheme())
        drawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(drawable, Color.parseColor(photos.color))
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.XOR)
        //color_details.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)


        // load the image here
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.setColorSchemeColors(ContextCompat.getColor(this, R.color.tokyoColorAccent))
        circularProgressDrawable.start()

        val requestOption = RequestOptions().placeholder(circularProgressDrawable).centerCrop()

        Glide.with(this@PhotoDetails)
                .load(photos.urls.regular)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOption)
                .into(imageView)
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
       // imageView.maxScale = getMaxiScale(true)

        Glide.with(this@PhotoDetails)
                .load(photos.user.profile_image.large)
                .apply(RequestOptions().priority(Priority.HIGH)
                        .placeholder(R.drawable.ic_account_circle))
                .into(photographerImg)

        // Sharing the Photo Event goes Here
        share_photo_btn.setOnClickListener {
            if (photos != null) {
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.unsplash_photo))
                share.putExtra(Intent.EXTRA_TEXT, photos.links.html + PhotoConstants.UNSPLASH_UTM_PARAMETERS)
                startActivity(Intent.createChooser(share, getString(R.string.share_via)))
            }
        }

        favoriteButton()
    }


    // will take us to the Photographer details collection of photos and description for the collection
    fun getPhotographerDetails(view: View) {
        val photos = Gson().fromJson<Photos>(intent.getStringExtra("Photo"), Photos::class.java)
        val gSon = Gson().toJson(photos)
        val intent = Intent(this, PhotographerActivity::class.java)
        intent.putExtra("photographer", gSon)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.none, R.anim.activity_slide_out)
    }

    //    /**
//     * Click this button to send the user fav to unsplash.com
//     */
  private fun favoriteButton(){
        val scaleAnimation = ScaleAnimation(0.7f,
                1.0f, 0.7f,
                1.0f, Animation.RELATIVE_TO_SELF,
                0.7f, Animation.RELATIVE_TO_SELF,
                0.7f)
        scaleAnimation.duration = 500
        val bounceInterpolator = BounceInterpolator()
        scaleAnimation.interpolator = bounceInterpolator

        like_details_btn.setOnCheckedChangeListener(object : View.OnClickListener, CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(btn: CompoundButton?, fav: Boolean) {
                btn?.startAnimation(scaleAnimation)
                Log.d("favValue", "$fav")
            }

            override fun onClick(p0: View?) {
                TODO("not implemented")
            }
        })
    }


}