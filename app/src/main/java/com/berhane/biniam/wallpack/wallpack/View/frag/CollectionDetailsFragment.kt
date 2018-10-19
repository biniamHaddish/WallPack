/*
 * DayTime:9/25/18 4:58 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.View.frag

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
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
import com.berhane.biniam.wallpack.wallpack.model.data.PhotoCollection
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.utils.EndlessRecyclerViewScrollListener
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.adapter.WallPackPhotoAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.detailed_collection_fragment.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CollectionDetailsFragment : Fragment() {

    private val TAG: String = "CollectionDFragment"
    private var pageNumber: Int = 1
    private var currentPage: Int = 0
    private var totalPage: Int = 0
    private lateinit var viewModel: WallPackViewModel
    private lateinit var mRecyclerView: RecyclerView
    private var viewAdapter: WallPackPhotoAdapter? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private var progress_layout = detailedCollectionProgress

    companion object {
        fun newInstance(collectionId: PhotoCollection): CollectionDetailsFragment {
            val args = Bundle()
            args.putParcelable("collectionWithId", collectionId)
            val fragment = CollectionDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d(TAG, "fragment Attached")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.detailed_collection_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(WallPackViewModel::class.java)
        progress_layout = rootView.findViewById(R.id.detailedCollectionProgress)
        mRecyclerView = rootView.findViewById(R.id.detailedCollectionRecyclerView)
        return rootView
    }

    /**
     * Will let the fragment start after the base activity is finished
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initCollection()
    }

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
                loadDetailedPhotoCollections(true)
            }
        }
        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)
    }

    override fun onResume() {
        super.onResume()
        loadDetailedPhotoCollections(false)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        Glide.with(this@CollectionDetailsFragment).resumeRequests()
                    }
                    else -> {
                        Glide.with(this@CollectionDetailsFragment).pauseRequests()
                    }
                }
            }
        })

    }

    /**
     *
     */
    private fun loadDetailedPhotoCollections(moreCollection: Boolean) {
        val args = arguments
        var collection: PhotoCollection = args!!.getParcelable("collectionWithId")
        Log.d(TAG, "coll_id" + collection.id)
        viewModel.getPhotoCollectionById(collection, pageNumber, PhotoConstants.PERPAGE)!!.observe(this@CollectionDetailsFragment,
                Observer<List<Photos>> { t: List<Photos>? ->
                    if (viewAdapter == null) {
                        viewAdapter = WallPackPhotoAdapter((t as MutableList<Photos>?)!!, activity as Activity)
                        mRecyclerView.adapter = viewAdapter
                    } else {
                        if (moreCollection) {
                            viewAdapter!!.addAll(t!!)
                            mRecyclerView.recycledViewPool.clear()
                        } else {
                            viewAdapter!!.setImageInfo((t as MutableList<Photos>?)!!)
                            mRecyclerView.recycledViewPool.clear()
                        }
                    }

                }
        )
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
    }


    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        scrollListener!!.resetState()
    }
}