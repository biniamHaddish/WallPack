/*
 * DayTime:9/9/18 8:26 PM :
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
import com.berhane.biniam.wallpack.wallpack.model.data.PhotoCollection
import com.berhane.biniam.wallpack.wallpack.model.data.Photos
import com.berhane.biniam.wallpack.wallpack.utils.FragmentArgumentDelegate
import com.berhane.biniam.wallpack.wallpack.utils.adapter.WallPackPhotoAdapter
import com.bumptech.glide.Glide
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView

class NewPhotosFragment : Fragment() {

    private var pageNumber: Int = 1
    private lateinit var viewModel: WallPackViewModel
    private lateinit var mRecyclerView: XRecyclerView
    private var viewAdapter: WallPackPhotoAdapter? = null
    private var categoryId = -1

//    private var collectionType by FragmentArgumentDelegate<String>()
//    private var photocollection by FragmentArgumentDelegate<PhotoCollection>()

    val TAG = "NewPhotosFragment"


    companion object {
        fun newInstance() = NewPhotosFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        val rootView = inflater!!.inflate(R.layout.new_photo_frag_layout, container, false)
        viewModel = ViewModelProviders.of(this).get(WallPackViewModel::class.java)
        mRecyclerView = rootView.findViewById(R.id.photo_recycler_view)

        return rootView

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWallpackPhotoView()

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

    private fun initWallpackPhotoView() {
        mRecyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse)
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallPulseSync)
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow)
        mRecyclerView.defaultFootView
        mRecyclerView.visibility = View.VISIBLE
        mRecyclerView.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {
                ++pageNumber
                loadPhotos(true)
            }

            override fun onRefresh() {
                pageNumber = 1
                loadPhotos(false)
            }
        })

    }

    /**
     *
     */
    private fun loadPhotos(onLoadNext: Boolean) {

        viewModel.getPhotosList(pageNumber, categoryId)!!.observe(this@NewPhotosFragment,
                Observer<List<Photos>> { t: List<Photos>? ->
                    if (viewAdapter == null) {
                        viewAdapter = WallPackPhotoAdapter(t!!, activity as Activity)
                        mRecyclerView.adapter = viewAdapter
                    } else {
                        if (onLoadNext) {
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