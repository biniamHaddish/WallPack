/*
 * DayTime:11/6/18 12:18 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.View.frag

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.model.View.WallPackViewModel
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.model.data.User
import com.berhane.biniam.wallpack.wallpack.utils.EndlessRecyclerViewScrollListener
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.adapter.WallPackPhotoAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.photographer_fragment.*

class UserPhotos:Fragment() {

    private val TAG: String = "UserPhotos"
    private var pageNumber: Int = 1
    private var currentPage: Int = 0
    private var totalPage: Int = 0
    private lateinit var viewModel: WallPackViewModel
    private lateinit var mRecyclerView: RecyclerView
    private var viewAdapter: WallPackPhotoAdapter? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private var progress_layout = photographer_progress

    companion object {
        fun newInstance(user_data: User) = UserPhotos().apply {
            val args = Bundle()
            args.putParcelable("User_Photos", user_data)
            val fragment = UserPhotos()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "OnCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.photographer_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(WallPackViewModel::class.java)
        progress_layout = rootView.findViewById(R.id.photographer_progress)
        mRecyclerView = rootView.findViewById(R.id.photographer_photos_recycler)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initPhotographerPhotos()
    }

    private fun initPhotographerPhotos() {
        //Add which views you don't want to hide. In this case don't hide the toolbar
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        var linearLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.layoutManager = linearLayoutManager
        // Ids to show while the loading view is showing
        excludeViewWhileLoading()
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                progress_layout.showContent()
                currentPage = page
                totalPage = totalItemsCount
                excludeViewWhileLoading()
                ++pageNumber
                loadPhotographerLikedPhotos(true)
            }
        }
        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)
    }

    override fun onResume() {
        super.onResume()
        loadPhotographerLikedPhotos(false)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        Glide.with(this@UserPhotos).resumeRequests()
                    }
                    else -> {
                        Glide.with(this@UserPhotos).pauseRequests()
                    }
                }
            }
        })
    }

    /**
     * Will Load all the Collections ,Likes and Photos of the Given Photographer
     */
    private fun loadPhotographerLikedPhotos(loadMore: Boolean) {
        val args = arguments
        var UserPhotos: User? = args!!.getParcelable("User_Photos")
        viewModel.getPhotographersPhotos(UserPhotos!!, pageNumber, PhotoConstants.PERPAGE, PhotoConstants.LATEST)!!.observe(this@UserPhotos,
                Observer<List<Photos>> { t: List<Photos>? ->
                    if (viewAdapter == null) {
                        viewAdapter = WallPackPhotoAdapter((t as MutableList<Photos>?)!!, activity as Activity)
                        mRecyclerView.adapter = viewAdapter
                    } else {
                        if (loadMore) {
                            viewAdapter!!.addAll(t!!)
                            mRecyclerView.recycledViewPool.clear()
                        } else {
                            viewAdapter!!.setImageInfo((t as MutableList<Photos>?)!!)
                            mRecyclerView.recycledViewPool.clear()
                        }
                    }
                })
    }

    /**
     * excluding some views while showing the  loading progress bar
     */
    private fun excludeViewWhileLoading() {
        var viewId = arrayListOf<Int>()
        viewId.add(R.id.photographer_photos_recycler)
        viewId.add(R.id.photographer_viewpager)
        progress_layout.showLoading(viewId)
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