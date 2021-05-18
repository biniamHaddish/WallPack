/*
 * DayTime:10/24/18 3:21 PM :
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
import com.berhane.biniam.wallpack.wallpack.model.data.User
import com.berhane.biniam.wallpack.wallpack.utils.EndlessRecyclerViewScrollListener
import com.berhane.biniam.wallpack.wallpack.utils.FragmentArgumentDelegate
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.adapter.UserAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.search_photo_fragment.*

class SearchUserFragment: Fragment() {

    private var currentPage: Int = 0
    private var totalPage: Int = 0
    private var pageNumber: Int = 1
    private lateinit var viewModel: WallPackViewModel
    private lateinit var mRecyclerView: RecyclerView
    private var viewAdapter: UserAdapter? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private var progressLayout = photoSearchProgress


    private var query by FragmentArgumentDelegate<String>()

    companion object {

        fun newInstance(query: String) = SearchUserFragment().apply {
            this.query = query
        }
    }
    override fun onResume() {
        super.onResume()
        loadUserSearchResult(false)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        Glide.with(this@SearchUserFragment).resumeRequests()
                    }
                    else -> {
                        Glide.with(this@SearchUserFragment).pauseRequests()
                    }
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUserSearch()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.search_photo_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(WallPackViewModel::class.java)
        progressLayout = rootView.findViewById(R.id.photoSearchProgress)
        mRecyclerView = rootView.findViewById(R.id.photoSearchRecyclerView)
        return rootView
    }

    private fun initUserSearch() {
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
                progressLayout.showContent()
                currentPage = page
                totalPage = totalItemsCount
                ++pageNumber
                // load the searched photo here
                loadUserSearchResult(true)
            }
        }
        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)
    }


    fun loadUserSearchResult(isLoad:Boolean){
        viewModel.getPhotographerSearchResult(query,pageNumber, PhotoConstants.PERPAGE)!!.observe(this@SearchUserFragment,
                Observer<List<User>> { t: List<User>? ->
                    if (viewAdapter == null) {
                        viewAdapter = UserAdapter((t as MutableList<User>?)!!, activity as Activity)
                        mRecyclerView.adapter = viewAdapter
                    } else {
                        if (isLoad) {
                            viewAdapter!!.addAllUser(t!!)
                            mRecyclerView.recycledViewPool.clear()
                        } else {
                            viewAdapter!!.setUser((t as MutableList<User>?)!!)
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
        viewId.add(R.id.photoSearchRecyclerView)
        viewId.add(R.id.search_viewpager)
        progressLayout.showLoading(viewId)
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
}