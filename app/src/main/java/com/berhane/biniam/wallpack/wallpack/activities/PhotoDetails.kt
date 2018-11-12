/*
 * DayTime:9/20/18 9:10 AM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.model.data.PhotoDetails
import com.berhane.biniam.wallpack.wallpack.model.data.PhotoLike
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.utils.DownloadHelper
import com.berhane.biniam.wallpack.wallpack.utils.DownloadHelper.DownloadType.DOWNLOAD
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.WallPack
import com.berhane.biniam.wallpack.wallpack.utils.connectivity.RetrofitClient
import com.berhane.biniam.wallpack.wallpack.utils.image_utills.NestedScrollPhotoView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.google.gson.Gson
import kotlinx.android.synthetic.main.detailed_photo.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


class PhotoDetails : AppCompatActivity(), RetrofitClient.OnReportDownloadListener, RetrofitClient.OnSetLikeListener, RetrofitClient.OnRequestPhotoDetailsListener {


    var context: Context? = null
    val TAG = "PhotoDetails"

    internal var floatingActionFabMenu: FloatingActionMenu? = null
    private var fabDownload: FloatingActionButton? = null
    private var fabWallpaper: FloatingActionButton? = null
    private var fabStats: FloatingActionButton? = null
    private var fabInfo: FloatingActionButton? = null
    private var photos: Photos? = null
    private var retrofitClient: RetrofitClient? = null
    private var coordinatorLayout: CoordinatorLayout? = null
    private var likedByUser: Boolean? = null
    private var photoDetails: PhotoDetails? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    var bottomSheet:FrameLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailed_photo)
        photos = Gson().fromJson<Photos>(intent.getStringExtra("Photo"), Photos::class.java)
        // Making the status bar translucent
        retrofitClient = RetrofitClient.getRetrofitClient()
        coordinatorLayout = findViewById(R.id.detailed_photo)
        val localLayoutParams = window.attributes
        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags)
        context = this
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        val imageView = findViewById<NestedScrollPhotoView>(R.id.image_detailed)
        // val progressbar=findViewById<ProgressBar>(R.id.detail_progress)
        val photographerImg = findViewById<ImageView>(R.id.photographerIMGView)
        floatingActionFabMenu = findViewById(R.id.fab_menu)
        fabDownload = findViewById(R.id.fab_download)
        fabWallpaper = findViewById(R.id.fab_wallpaper)
        fabStats = findViewById(R.id.fab_stats)
        fabInfo = findViewById(R.id.fab_info)

        //setting the click Listener
        retrofitClient!!.requestPhotoDetails(photos!!.id, this@PhotoDetails)
        fabDownload!!.setOnClickListener(onClickListener)
        fabInfo!!.setOnClickListener(onClickListener)
        fabStats!!.setOnClickListener(onClickListener)
        fabWallpaper!!.setOnClickListener(onClickListener)
        photographerName.text = "By " + photos!!.user.name
//        detailed_user_location.text = photos.user.location
//        published_date.text = photos.created_at.split("T")[0]
//        likes_details.text = NumberFormat.getInstance(Locale.US).format(photos.likes) + " Likes"
//        details_download.text = NumberFormat.getInstance(Locale.US).format(photos.downloads)
//        color_details.text = photos.color


        //Circular drawable until the detailed image loads
        var drawable = resources.getDrawable(R.drawable.ic_palette, resources.newTheme())
        drawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(drawable, Color.parseColor(photos!!.color))
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
                .load(photos!!.urls.regular)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOption)
                .into(imageView)

        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        // imageView.maxScale = getMaxiScale(true)
        fab_menu.setClosedOnTouchOutside(true)
        fab_menu.visibility = View.VISIBLE

        Glide.with(this@PhotoDetails)
                .load(photos!!.user.profile_image.large)
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
                share.putExtra(Intent.EXTRA_TEXT, photos!!.links.html + PhotoConstants.UNSPLASH_UTM_PARAMETERS)
                startActivity(Intent.createChooser(share, getString(R.string.share_via)))
            }
        }


        favoriteButton()
        createCustomAnimation()
        bottomSheetBehavior()
        //hide the bottomSheet if it was Expanded
        imageView.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    //Requesting the image details here
    override fun onRequestPhotoDetailsSuccess(call: Call<PhotoDetails>, response: Response<PhotoDetails>) {
        if (response.isSuccessful) {
            photoDetails = response.body()
            likedByUser = photoDetails!!.liked_by_user
            if (likedByUser as Boolean) {
                like_details_btn.isChecked = true
            }
        } else if (response.code() == 403) {
            PhotoConstants.showSnack(coordinatorLayout!!, "Can not make request.")
        } else {
            retrofitClient!!.requestPhotoDetails(photos!!.id, this@PhotoDetails)
        }
    }

    override fun onRequestPhotoDetailsFailed(call: Call<PhotoDetails>, t: Throwable) {
        t.printStackTrace()
        Log.d(TAG, t.message)
        retrofitClient!!.requestPhotoDetails(photos!!.id, this@PhotoDetails)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.fab_download -> if (WallPack.isStoragePermissionGranted(this@PhotoDetails) && photos != null) {
                floatingActionFabMenu!!.close(true)
                Toast.makeText(applicationContext, getString(R.string.download_started), Toast.LENGTH_SHORT).show()
                downloadPhoto(photos!!.urls.full, DOWNLOAD)
            }
            R.id.fab_wallpaper -> if (WallPack.isStoragePermissionGranted(this@PhotoDetails) && photos != null) {
                floatingActionFabMenu!!.close(true)
//                currentAction = WALLPAPER
//                wallpaperDialog = WallpaperDialog()
//                wallpaperDialog.setListener(object : WallpaperDialog.WallpaperDialogListener() {
//                    fun onCancel() {
//                        DownloadHelper.getInstance(this@DetailActivity).removeDownloadRequest(downloadReference)
//                    }
//                })
//                wallpaperDialog.show(fragmentManager, null)
//
//                when (sharedPreferences.getString("wallpaper_quality", "Full")) {
//                    "Raw" -> downloadPhoto(mPhoto.urls.raw, WALLPAPER)
//                    "Full" -> downloadPhoto(mPhoto.urls.full, WALLPAPER)
//                    "Regular" -> downloadPhoto(mPhoto.urls.regular, WALLPAPER)
//                    "Small" -> downloadPhoto(mPhoto.urls.small, WALLPAPER)
//                    "Thumb" -> downloadPhoto(mPhoto.urls.thumb, WALLPAPER)
//                    else -> downloadPhoto(mPhoto.urls.full, WALLPAPER)
//                }
            }
            R.id.fab_info -> if (photos != null) {
                floatingActionFabMenu!!.close(true)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//                val infoDialog = InfoDialog()
//                infoDialog.setPhotoDetails(mPhotoDetails)
//                infoDialog.show(fragmentManager, null)
            }
            R.id.fab_stats -> if (photos != null) {
                floatingActionFabMenu!!.close(true)
//                val statsDialog = StatsDialog()
//                statsDialog.setPhoto(mPhoto)
//                statsDialog.show(fragmentManager, null)
            }
        }
    }

    private fun downloadPhoto(url: String, @DownloadHelper.DownloadType downloadType: Int) {
        retrofitClient!!.reportPhotoDownload(photos!!.id, this@PhotoDetails)
        val photoName = photos!!.id + "_" + "wallPack" + PhotoConstants.DOWNLOAD_PHOTO_FORMAT
        DownloadHelper.getInstance(this).addDownloadRequest(downloadType, url, photoName)
    }

    private fun createCustomAnimation() {

        val set = AnimatorSet()

        val scaleOutX = ObjectAnimator.ofFloat(floatingActionFabMenu!!.menuIconView, "scaleX", 1.0f, 0.2f)
        val scaleOutY = ObjectAnimator.ofFloat(floatingActionFabMenu!!.menuIconView, "scaleY", 1.0f, 0.2f)

        val scaleInX = ObjectAnimator.ofFloat(floatingActionFabMenu!!.menuIconView, "scaleX", 0.2f, 1.0f)
        val scaleInY = ObjectAnimator.ofFloat(floatingActionFabMenu!!.menuIconView, "scaleY", 0.2f, 1.0f)

        scaleOutX.duration = 50
        scaleOutY.duration = 50

        scaleInX.duration = 150
        scaleInY.duration = 150

        scaleInX.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                floatingActionFabMenu!!.menuIconView.setImageResource(if (floatingActionFabMenu!!.isOpened)
                    R.drawable.ic_expand_less_24px
                else
                    R.drawable.ic_expand_more_24px)
            }
        })

        set.play(scaleOutX).with(scaleOutY)
        set.play(scaleInX).with(scaleInY).after(scaleOutX)
        set.interpolator = OvershootInterpolator(2f)

        floatingActionFabMenu!!.iconToggleAnimatorSet = set
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

    /**
     * Defining the BottomSheet Behavior
     */
    private fun bottomSheetBehavior(){
        //BottomSheet Goes here
        bottomSheet = coordinatorLayout!!.findViewById(R.id.bottom_sheet) as FrameLayout
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {


                // React to state change
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED")
                    BottomSheetBehavior.STATE_DRAGGING -> Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING")
                    BottomSheetBehavior.STATE_EXPANDED -> Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED")
                    BottomSheetBehavior.STATE_HIDDEN -> Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN")
                    BottomSheetBehavior.STATE_SETTLING -> Log.e("Bottom Sheet Behaviour", "STATE_SETTLING")

                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                // React to dragging events
                Log.e("onSlide", "onSlide \t $slideOffset")
            }
        })
    }



    /**
     * Click this button to send the user fav to unsplash.com
     */
    private fun favoriteButton() {
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
                if (fav) {
                    retrofitClient!!.setLikeForAPhoto(photos!!.id, fav, this@PhotoDetails)
                } else {
                    retrofitClient!!.setLikeForAPhoto(photos!!.id, !fav, this@PhotoDetails)
                }
            }

            override fun onClick(p0: View?) {
                TODO("not implemented")
            }
        })
    }

    override fun onReportDownloadSuccess(call: Call<ResponseBody>, response: Response<ResponseBody>) {
        if (response.isSuccessful) {

        }
    }

    override fun onReportDownloadFailed(call: Call<ResponseBody>, t: Throwable) {

    }

    override fun onSetLikeSuccess(call: Call<PhotoLike>, response: Response<PhotoLike>) {

    }

    override fun onSetLikeFailed(call: Call<PhotoLike>, t: Throwable) {

    }

}