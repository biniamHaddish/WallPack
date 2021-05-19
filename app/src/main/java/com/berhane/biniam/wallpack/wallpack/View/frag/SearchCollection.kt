/*
 * DayTime:10/24/18 11:15 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.View.frag

import android.app.Activity
import android.os.Bundle
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
import kotlinx.android.synthetic.main.collection_fragment.view.*
import kotlinx.android.synthetic.main.search_photo_fragment.*

class SearchCollection : Fragment() {

    private var currentPage: Int = 0
    private var totalPage: Int = 0
    private var pageNumber: Int = 1
    private lateinit var viewModel: WallPackViewModel
    private lateinit var mRecyclerView: RecyclerView
    private var viewAdapter: CollectionAdapter? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private var progressLayout = photoSearchProgress


    private var mQuery by FragmentArgumentDelegate<String>()


    companion object {
        fun newInstance(query: String) = SearchCollection().apply {
            this.mQuery = query
        }
    }

    override fun onResume() {
        super.onResume()
        loadCollectionSearchResult(false)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        Glide.with(this@SearchCollection).resumeRequests()
                    }
                    else -> {
                        Glide.with(this@SearchCollection).pauseRequests()
                    }
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initCollectionSearch()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.collection_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(WallPackViewModel::class.java)
        mRecyclerView = rootView.findViewById(R.id.CollectionRecyclerView)
        progressLayout = rootView.findViewById(R.id.collection_Progress_layout)
        rootView.collection_Progress_layout.findViewById<View>(R.id.collection_Progress_layout)
        return rootView
    }


    private fun initCollectionSearch() {
        //Add which views you don't want to hide. In this case don't hide the toolbar
        mRecyclerView.itemAnimator = DefaultItemAnimator() as RecyclerView.ItemAnimator?
        var linearLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.layoutManager = linearLayoutManager
        // Ids to show while the loading view is showing
        excludeViewWhileLoading()
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                progressLayout.showContent()
                currentPage = page
                totalPage = totalItemsCount
                ++pageNumber
                // load the searched photo here
                loadCollectionSearchResult(true)
            }
        }
        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)
    }

    private fun loadCollectionSearchResult(isLoading: Boolean) {
        viewModel.getCollectionSearchResult(mQuery ,pageNumber, PhotoConstants.PERPAGE)!!.observe(this@SearchCollection,
                Observer<List<PhotoCollection>> { t: List<PhotoCollection>? ->
                    if (viewAdapter == null) {
                        viewAdapter = CollectionAdapter((t as MutableList<PhotoCollection>?)!!, activity as Activity)
                        mRecyclerView.adapter = viewAdapter
                    } else {
                        if (isLoading) {
                            viewAdapter!!.addCollectionPhotos(t!!)
                            mRecyclerView.recycledViewPool.clear()
                        } else {
                            progressLayout.showContent()
                            viewAdapter!!.setImageInfo(t!!)
                            mRecyclerView.recycledViewPool.clear()
                        }
                    }

                }
        )
    }

    override fun onDetach() {
        super.onDetach()
        viewAdapter = null
        scrollListener!!.resetState()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (mRecyclerView != null) {
            scrollListener!!.resetState()
        }
    }

    private fun excludeViewWhileLoading() {
        var viewId = arrayListOf<Int>()
        viewId.add(R.id.CollectionRecyclerView)
        viewId.add(R.id.collection_viewPager)
        progressLayout.showLoading(viewId)
    }


}