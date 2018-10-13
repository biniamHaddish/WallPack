/*
 * DayTime:10/7/18 1:03 PM :
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
import android.widget.Toast
import com.berhane.biniam.wallpack.wallpack.R
import com.berhane.biniam.wallpack.wallpack.model.View.WallPackViewModel
import com.berhane.biniam.wallpack.wallpack.model.data.PhotoCollection
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.utils.FragmentArgumentDelegate
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.adapter.CollectionAdapter
import com.berhane.biniam.wallpack.wallpack.utils.adapter.WallPackPhotoAdapter
import com.bumptech.glide.Glide
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView

class PhotographerDetailsFragment : Fragment() {

    private val TAG: String = "PhotographerFrag"
    private var pageNumber: Int = 1
    private lateinit var viewModel: WallPackViewModel
    private lateinit var mRecyclerView: XRecyclerView
    private var viewAdapter: WallPackPhotoAdapter? = null

    companion object {
        fun newInstance(photographer: Photos) = PhotographerDetailsFragment().apply {
            val args = Bundle()
            args.putParcelable("photographerPhotos", photographer)
            val fragment = PhotographerDetailsFragment()
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
        mRecyclerView = rootView.findViewById(R.id.photographer_photos_recycler)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated" + "Photographer Activity Already Started!!!!")
        initPhotographerPhotos()
    }

    private fun initPhotographerPhotos() {
        mRecyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse)
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallPulseSync)
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow)
        mRecyclerView.defaultFootView
        mRecyclerView.visibility = View.VISIBLE
        mRecyclerView.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {
                ++pageNumber
                loadPhotographerPhotos(true)
            }

            override fun onRefresh() {
                pageNumber = 1
                loadPhotographerPhotos(false)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadPhotographerPhotos(false)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        Glide.with(this@PhotographerDetailsFragment).resumeRequests()
                    }
                    else -> {
                        Glide.with(this@PhotographerDetailsFragment).pauseRequests()
                    }
                }
            }
        })
    }

    /**
     * Will Load all the Collections ,Likes and Photos of the Given Photographer
     */
    private fun loadPhotographerPhotos(loadMore: Boolean) {
        val args = arguments
        var photographerCol: Photos? = args!!.getParcelable("photographerPhotos")
        viewModel.getPhotographersPhotos(photographerCol!!.user, pageNumber, PhotoConstants.PERPAGE, PhotoConstants.LATEST)!!.observe(this@PhotographerDetailsFragment,
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