/*
 * DayTime:9/9/18 10:31 PM :
 * Year:2018 :
 * Author:bini :
 */

package com.berhane.biniam.wallpack.wallpack.View.frag

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.model.View.WallPackViewModel
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.utils.FragmentArgumentDelegate
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.adapter.WallPackPhotoAdapter
import com.bumptech.glide.Glide
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView


class FeaturedFragment : Fragment() {

    private var pageNumber: Int = 1
    private lateinit var viewModel: WallPackViewModel
    private lateinit var mRecyclerView: XRecyclerView
    private var viewAdapter: WallPackPhotoAdapter? = null


    private var sortOrder by FragmentArgumentDelegate<String>()
    private val TAG = "FeaturedFragment"

    companion object {
        fun newInstance(sortOrder: String) = FeaturedFragment().apply {
            this.sortOrder = sortOrder
        }
    }

    override fun onResume() {
        super.onResume()
        loadCuratedPhotos(false)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        Glide.with(this@FeaturedFragment).resumeRequests()
                    }
                    else -> {
                        Glide.with(this@FeaturedFragment).pauseRequests()
                    }
                }
            }
        })

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.curated_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(WallPackViewModel::class.java)
        mRecyclerView = rootView.findViewById(R.id.CuratedRecyclerView)

        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initFeaturedFragment()
    }

    fun loadCuratedPhotos(loadMore: Boolean) {
        viewModel.getCuratedPhotos(pageNumber, PhotoConstants.PERPAGE, sortOrder)!!.observe(this@FeaturedFragment,
                Observer<List<Photos>> { t: List<Photos>? ->
                    if (viewAdapter == null) {
                        viewAdapter = WallPackPhotoAdapter(t!!, activity as Activity)
                        mRecyclerView.adapter = viewAdapter
                    } else {
                        if (loadMore) {
                            viewAdapter!!.addImageInfo(t!!)
                            mRecyclerView.loadMoreComplete()
                        } else {
                            viewAdapter!!.setImageInfo(t!!)
                            mRecyclerView.refreshComplete()
                        }
                    }
                })
    }

    /**
     * initializing the value of our CollectionFragment here
     */
    private fun initFeaturedFragment() {
        mRecyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse)
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallPulseSync)
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow)
        mRecyclerView.defaultFootView
        mRecyclerView.visibility = View.VISIBLE
        mRecyclerView.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {
                ++pageNumber
                loadCuratedPhotos(true)
            }

            override fun onRefresh() {
                pageNumber = 1
                loadCuratedPhotos(false)
            }
        })
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach")
        super.onDetach()
        viewAdapter = null
    }


    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        if (mRecyclerView != null) {
            mRecyclerView.destroy()
        }
    }
}
