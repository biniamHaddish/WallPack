/*
 * DayTime:10/20/18 6:02 PM :
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
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.utils.EndlessRecyclerViewScrollListener
import com.berhane.biniam.wallpack.wallpack.utils.FragmentArgumentDelegate
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.adapter.WallPackPhotoAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.search_photo_fragment.*

class SearchPhotoFragment : Fragment() {

    private var isLoading = false
    private var currentPage: Int = 0
    private var totalPage: Int = 0
    private var pageNumber: Int = 1
    private lateinit var viewModel: WallPackViewModel
    private lateinit var mRecyclerView: RecyclerView
    private var viewAdapter: WallPackPhotoAdapter? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private var progressLayout = photoSearchProgress


    private var query by FragmentArgumentDelegate<String>()

    companion object {

        fun newInstance(query: String) = SearchPhotoFragment().apply {
            this.query = query
        }
    }

    override fun onResume() {
        super.onResume()
        loadSearchedResult(false)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        Glide.with(this@SearchPhotoFragment).resumeRequests()
                    }
                    else -> {
                        Glide.with(this@SearchPhotoFragment).pauseRequests()
                    }
                }
            }
        })
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initPhotoSearch()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.search_photo_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(WallPackViewModel::class.java)
        progressLayout = rootView.findViewById(R.id.photoSearchProgress)
        mRecyclerView = rootView.findViewById(R.id.photoSearchRecyclerView)
        progressLayout.showEmpty(R.drawable.ic_hourglass_empty_24px,"Search Photos","")
        return rootView
    }

    private fun initPhotoSearch() {
        //Add which views you don't want to hide. In this case don't hide the toolbar
        mRecyclerView.itemAnimator = DefaultItemAnimator() as RecyclerView.ItemAnimator?
        var linearLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.layoutManager = linearLayoutManager
        // Ids to show while the loading view is showing
        //excludeViewWhileLoading()
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                progressLayout.showContent()
                currentPage = page
                totalPage = totalItemsCount
                excludeViewWhileLoading()
                ++pageNumber
                // load the searched photo here
                loadSearchedResult(true)
            }
        }
        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)
    }

    private fun loadSearchedResult(load:Boolean) {
    viewModel.getSearchedPhotos(query,pageNumber,PhotoConstants.PERPAGE)!!.observe(this@SearchPhotoFragment,
            Observer<List<Photos>> { t: List<Photos>? ->
                if (viewAdapter == null) {
                    viewAdapter = WallPackPhotoAdapter((t as MutableList<Photos>?)!!, activity as Activity)
                    mRecyclerView.adapter = viewAdapter
                } else {
                    if (load) {
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
     *
     */
    private fun excludeViewWhileLoading() {
        var viewId = arrayListOf<Int>()
        viewId.add(R.id.CuratedRecyclerView)
        viewId.add(R.id.featured_viewpager)
        progressLayout.showLoading(viewId)
    }
}