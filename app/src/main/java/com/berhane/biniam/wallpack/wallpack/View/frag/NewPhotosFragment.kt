/*
 * DayTime:9/9/18 8:26 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.View.frag

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.activities.LoginActivity
import com.berhane.biniam.wallpack.wallpack.activities.PhotographerActivity
import com.berhane.biniam.wallpack.wallpack.model.View.WallPackViewModel
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.utils.EndlessRecyclerViewScrollListener
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.WallPack
import com.berhane.biniam.wallpack.wallpack.utils.adapter.WallPackPhotoAdapter
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.new_photo_frag_layout.*


class NewPhotosFragment : Fragment() {
    val TAG = "NewPhotosFragment"
    private var authPref: SharedPreferences? = null
    private var avatarInfo: String? = null
    private var accessToken: String? = null
    private var pageNumber: Int = 1
    private var isLoading = false
    private var currentPage: Int = 0
    private var totalPage: Int = 0
    private lateinit var viewModel: WallPackViewModel
    private lateinit var mRecyclerView: RecyclerView
    private var viewAdapter: WallPackPhotoAdapter? = null
    private var categoryId = -1
    // Store a member variable for the listener
    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    companion object {
        fun newInstance() = NewPhotosFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.new_photo_frag_layout, container, false)
        viewModel = ViewModelProviders.of(this).get(WallPackViewModel::class.java)
        mRecyclerView = rootView.findViewById(R.id.photo_recycler_view)
        authPref = WallPack.applicationContext().getSharedPreferences(PhotoConstants.PREFERENCE_NAME, 0)
        avatarInfo = authPref!!.getString(PhotoConstants.KEY_AVATAR_PATH, "")
        accessToken = authPref!!.getString(PhotoConstants.KEY_ACCESS_TOKEN, "")

        return rootView

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        AccountImage.setOnClickListener {
            if (TextUtils.isEmpty(accessToken) && TextUtils.isEmpty(avatarInfo)) {
                val intent = Intent(activity as Activity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            } else {
                val intent = Intent(activity as Activity, PhotographerActivity::class.java)
                val photos = Gson().fromJson<Photos>(intent.getStringExtra("Photo"), Photos::class.java)
                val gSon = Gson().toJson(photos)
                intent.putExtra("Photo", gSon)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }
        }
        initRecyclerImageView()
        loadProfileInfo()
    }

    override fun onResume() {
        super.onResume()
        loadPhotos(false)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        Glide.with(this@NewPhotosFragment).resumeRequests()
                    }
                    else -> {
                        Glide.with(this@NewPhotosFragment).pauseRequests()
                    }
                }
            }
        })
    }

    private fun initRecyclerImageView() {
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        var linearLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.layoutManager = linearLayoutManager
        // Retain an instance so that you can call `resetState()` for fresh Loading
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                newPhotoProgressLayout.showContent()
                currentPage = page
                totalPage = totalItemsCount
                isLoading = true
                ++pageNumber
                loadPhotos(true)
            }
        }
        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)
    }

    /**
     *
     */
    private fun loadPhotos(onLoadNext: Boolean) {
        viewModel.getPhotosList(pageNumber, categoryId)!!.observe(this@NewPhotosFragment,
            { t: List<Photos>? ->
                if (viewAdapter == null) {
                    viewAdapter =
                        (t as MutableList<Photos>?)?.let { WallPackPhotoAdapter(it, activity as Activity) }
                    mRecyclerView.adapter = viewAdapter
                } else {
                    if (onLoadNext) {
                        viewAdapter!!.addAll(t!!)
                    } else {
                        viewAdapter!!.setImageInfo((t as MutableList<Photos>?)!!)
                    }
                }
            })
        isLoading = false
        scrollListener!!.resetState()

    }

    private fun loadProfileInfo() {
        if (!TextUtils.isEmpty(avatarInfo)) {
            context?.let {
                Glide.with(it)
                    .load(avatarInfo)
                    .into(AccountImage)
            }
        }

    }


    override fun onDetach() {
        Log.d(TAG, "onDetach")
        super.onDetach()
        viewAdapter = null
        scrollListener!!.resetState()
    }


    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        if (mRecyclerView != null) {
            scrollListener!!.resetState()
        }
    }
}
