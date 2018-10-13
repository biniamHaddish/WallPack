/*
 * DayTime:10/10/18 10:40 AM :
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
import com.berhane.biniam.wallpack.wallpack.utils.PhotoConstants
import com.berhane.biniam.wallpack.wallpack.utils.adapter.CollectionAdapter
import com.berhane.biniam.wallpack.wallpack.utils.adapter.WallPackPhotoAdapter
import com.bumptech.glide.Glide
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView

class PhotographerCollectionFragment : Fragment() {

    private var pageNumber: Int = 1
    private lateinit var viewModel: WallPackViewModel
    private lateinit var mRecyclerView: XRecyclerView
    private var collectionAdapter: CollectionAdapter? = null

    private val TAG: String = "PhotographerFrag"


    companion object {
        fun newInstance(photographer: Photos) = PhotographerCollectionFragment().apply {
            val args = Bundle()
            args.putParcelable("photographerCollection", photographer)
            val fragment = PhotographerCollectionFragment()
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
                        Glide.with(this@PhotographerCollectionFragment).resumeRequests()
                    }
                    else -> {
                        Glide.with(this@PhotographerCollectionFragment).pauseRequests()
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
        var photographerCol: Photos? = args!!.getParcelable("photographerCollection")
        viewModel.getPhotographerCollection(photographerCol!!.user, pageNumber, PhotoConstants.PERPAGE)!!.observe(this@PhotographerCollectionFragment,
                Observer<List<PhotoCollection>> { t: List<PhotoCollection>? ->
                    if (collectionAdapter == null) {
                        collectionAdapter = CollectionAdapter(t!!, activity as Activity)
                        mRecyclerView.adapter = collectionAdapter
                    } else {
                        if (loadMore) {
                            collectionAdapter!!.addImageInfo(t!!)
                            mRecyclerView.loadMoreComplete()
                        } else {
                            collectionAdapter!!.setImageInfo(t!!)
                            mRecyclerView.refreshComplete()
                        }
                    }
                })
    }


    override fun onDetach() {
        Log.d(TAG, "onDetach")
        super.onDetach()
        collectionAdapter = null
    }


    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        if (mRecyclerView != null) {
            mRecyclerView.destroy()
        }
    }

}