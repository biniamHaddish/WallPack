/*
 * DayTime:9/13/18 11:43 AM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.View.frag

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
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
import com.berhane.biniam.wallpack.wallpack.model.View.WallPackViewModel
import com.berhane.biniam.wallpack.wallpack.model.data.PhotoCollection
import com.berhane.biniam.wallpack.wallpack.utils.EndlessRecyclerViewScrollListener
import com.berhane.biniam.wallpack.wallpack.utils.FragmentArgumentDelegate
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.adapter.CollectionAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.collection_fragment.*
import kotlinx.android.synthetic.main.collection_fragment.view.*

class CollectionFragment : Fragment() {
    val TAG = "CollectionFragment"
    private var pageNumber: Int = 1
    private var isLoading = false
    private var currentPage: Int = 0
    private var totalPage: Int = 0
    private lateinit var viewModel: WallPackViewModel
    private lateinit var mRecyclerView: RecyclerView
    private var viewAdapter: CollectionAdapter? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private var progress_layout = collection_Progress_layout

    private var collectionType by FragmentArgumentDelegate<String>()

    companion object {
        fun newInstance(collectionType: String) = CollectionFragment().apply {
            this.collectionType = collectionType
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "fragment Attatched")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.collection_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(WallPackViewModel::class.java)
        mRecyclerView = rootView.findViewById(R.id.CollectionRecyclerView)
        progress_layout = rootView.findViewById(R.id.collection_Progress_layout)
        rootView.collection_Progress_layout.findViewById<View>(R.id.collection_Progress_layout)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        loadPhotoCollections(false)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        Glide.with(this@CollectionFragment).resumeRequests()
                    }
                    else -> {
                        Glide.with(this@CollectionFragment).pauseRequests()
                    }
                }
            }
        })

    }

    /**
     * Will let the fragment start after the base activity is finished
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initCollection()
    }

    /**
     * initializing the value of our CollectionFragment here
     */
    private fun initCollection() {

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
                loadPhotoCollections(true)
            }
        }
        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)
    }

    /**
     * Will Load more CollectionFragment if the value is true
     */
    fun loadPhotoCollections(moreCollection: Boolean) {
        if (collectionType == PhotoConstants.COLLECTION_TYPE_ALL) {

            viewModel.getPhotoCollection(pageNumber, PhotoConstants.PERPAGE)!!.observe(this@CollectionFragment,
                    Observer<List<PhotoCollection>> { t: List<PhotoCollection>? ->
                        if (viewAdapter == null) {
                            viewAdapter = CollectionAdapter((t as MutableList<PhotoCollection>?)!!, activity as Activity)
                            mRecyclerView.adapter = viewAdapter
                        } else {
                            isLoading=true
                            if (moreCollection) {
                                viewAdapter!!.addCollectionPhotos(t!!)
                                mRecyclerView.recycledViewPool.clear()
                            } else {
                                isLoading=false
                                progress_layout.showContent()
                                viewAdapter!!.setImageInfo(t!!)
                                mRecyclerView.recycledViewPool.clear()
                            }
                        }

                    }
            )
        } else if (collectionType == (PhotoConstants.COLLECTION_TYPE_FEATURED)) {

            viewModel.getFeaturedCollection(pageNumber, PhotoConstants.PERPAGE)!!.observe(this@CollectionFragment,
                    Observer<List<PhotoCollection>> { t: List<PhotoCollection>? ->
                        if (viewAdapter == null) {
                            viewAdapter = CollectionAdapter((t as MutableList<PhotoCollection>?)!!, activity as Activity)
                            mRecyclerView.adapter = viewAdapter
                        } else {
                            if (moreCollection) {
                                viewAdapter!!.addCollectionPhotos(t!!)

                            } else {
                                viewAdapter!!.setImageInfo(t!!)
                            }
                        }

                    }
            )

        } else if (collectionType == (PhotoConstants.COLLECTION_TYPE_CURATED)) {

            viewModel.getCuratedCollection(pageNumber, PhotoConstants.PERPAGE)!!.observe(this@CollectionFragment,
                { t: List<PhotoCollection>? ->
                    if (viewAdapter == null) {
                        viewAdapter = CollectionAdapter((t as MutableList<PhotoCollection>?)!!, activity as Activity)
                        mRecyclerView.adapter = viewAdapter
                    } else {
                        if (moreCollection) {
                            viewAdapter!!.addCollectionPhotos(t!!)

                        } else {
                            viewAdapter!!.setImageInfo(t!!)
                            // mRecyclerView.refreshComplete()
                        }
                    }
                }
            )
        }
        isLoading = false
    }


    private fun excludeViewWhileLoading() {
        var viewId = arrayListOf<Int>()
        viewId.add(R.id.CollectionRecyclerView)
        viewId.add(R.id.collection_viewPager)
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