package com.beyebe.fastdevframe.view.widget

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.beyebe.fastdevframe.R

class LoadMoreRecyclerView : RecyclerView {

    private var mIsFooterEnable = false//是否允许加载更多

    /**
     * 自定义实现了头部和底部加载更多的adapter
     */
    private var mAutoLoadAdapter: AutoLoadAdapter? = null
    /**
     * 标记是否正在加载更多，防止再次调用加载更多接口
     */
    private var mIsLoadingMore: Boolean = false
    /**
     * 标记加载更多的position
     */
    private var mLoadMorePosition: Int = 0
    /**
     * 加载更多的监听-业务需要实现加载数据
     */
    private var mListener: LoadMoreListener? = null
    private val TAG = "LoadMoreRecyclerView"

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    /**
     * 初始化-添加滚动监听
     *
     *
     * 回调加载更多方法，前提是
     *
     * 1、有监听并且支持加载更多：null != mListener && mIsFooterEnable
     * 2、目前没有在加载，正在上拉（dy>0），当前最后一条可见的view是否是当前数据列表的最好一条--及加载更多
     *
     */
    private fun init() {
        super.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (null != mListener && mIsFooterEnable && !mIsLoadingMore && dy > 0) {
                    val lastVisiblePosition = lastVisiblePosition
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItem = firstVisiblePosition
                    val visibleItemCount = layoutManager.childCount
                    if (totalItemCount <= visibleItemCount + firstVisibleItem) {
                        mAutoLoadAdapter?.footerViewHolder?.itemView?.visibility = View.VISIBLE
                        setLoadingMore(true)
                        mLoadMorePosition = lastVisiblePosition
                        mListener!!.onLoadMore()
                    }
                }
            }
        })
    }

    /**
     * 设置加载更多的监听
     *
     * @param listener
     */
    fun setLoadMoreListener(listener: LoadMoreListener) {
        mListener = listener
    }

    /**
     * 设置正在加载更多

     * @param loadingMore
     */
    fun setLoadingMore(loadingMore: Boolean) {
        this.mIsLoadingMore = loadingMore
    }

    /**
     * 加载更多监听
     */
    interface LoadMoreListener {

        /**
         * 加载更多
         */
        fun onLoadMore()

    }

    /**

     */
    inner class AutoLoadAdapter(
            /**
             * 数据adapter
             */
            private val mInternalAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var mIsHeaderEnable: Boolean = false
        private var mHeaderResId: Int = 0
        var footerViewHolder: FooterViewHolder? = null
            private set

        init {
            mIsHeaderEnable = false
        }

        override fun getItemViewType(position: Int): Int {
            val headerPosition = 0
            val footerPosition = itemCount - 1

            if (headerPosition == position && mIsHeaderEnable && mHeaderResId > 0) {
                return TYPE_HEADER
            }

            if (footerPosition == position && mIsFooterEnable) {
                return TYPE_FOOTER
            }
            /**
             * 这么做保证layoutManager切换之后能及时的刷新上对的布局
             */
            if (layoutManager is LinearLayoutManager) {
                return TYPE_LIST
            } else if (layoutManager is StaggeredGridLayoutManager) {
                return TYPE_STAGGER
            } else {
                return TYPE_NORMAL
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == TYPE_HEADER) {
                return HeaderViewHolder(LayoutInflater.from(parent.context).inflate(
                        mHeaderResId, parent, false))
            }
            if (viewType == TYPE_FOOTER) {
                footerViewHolder = FooterViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                                R.layout.list_foot_loading, parent, false))
                return footerViewHolder as FooterViewHolder
            } else {
                // type normal
                return mInternalAdapter.onCreateViewHolder(parent, viewType)
            }
        }

        inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val indicator: View

            init {
                indicator = itemView.findViewById(R.id.avLoadingIndicatorView)
            }
        }

        inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val type = getItemViewType(position)
            if (type == TYPE_LIST || type == TYPE_STAGGER || type == TYPE_NORMAL) {
                mInternalAdapter.onBindViewHolder(holder, position)
            } else if (type == TYPE_FOOTER) {
                if (!mIsLoadingMore) {
                    val footerViewHolder = holder as FooterViewHolder
                    footerViewHolder.itemView.visibility = View.GONE
                    footerViewHolder.indicator.visibility = View.GONE
                    footerViewHolder.indicator.visibility = View.VISIBLE
                }
            }
        }

        /**
         * 需要计算上加载更多和添加的头部俩个

         * @return
         */
        override fun getItemCount(): Int {
            var count = mInternalAdapter.itemCount
            if (mIsFooterEnable) count++
            if (mIsHeaderEnable) count++

            return count
        }

        fun setHeaderEnable(enable: Boolean) {
            mIsHeaderEnable = enable
        }

        fun addHeaderView(resId: Int) {
            mHeaderResId = resId
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<ViewHolder>?) {
        if (adapter != null) {
            mAutoLoadAdapter = AutoLoadAdapter(adapter)
        }
        super.setAdapter(mAutoLoadAdapter)
    }

    /**
     * 切换layoutManager

     * 为了保证切换之后页面上还是停留在当前展示的位置，记录下切换之前的第一条展示位置，切换完成之后滚动到该位置
     * 另外切换之后必须要重新刷新下当前已经缓存的itemView，否则会出现布局错乱（俩种模式下的item布局不同），
     * RecyclerView提供了swapAdapter来进行切换adapter并清理老的itemView cache

     * @param layoutManager
     */
    fun switchLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        val firstVisiblePosition = firstVisiblePosition
        //        getLayoutManager().removeAllViews();
        setLayoutManager(layoutManager)
        //        super.swapAdapter(mAutoLoadAdapter, true);
        getLayoutManager().scrollToPosition(firstVisiblePosition)
    }

    /**
     * 获取第一条展示的位置

     * @return
     */
    private val firstVisiblePosition: Int
        get() {
            val position: Int
            if (layoutManager is LinearLayoutManager) {
                position = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            } else if (layoutManager is GridLayoutManager) {
                position = (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            } else if (layoutManager is StaggeredGridLayoutManager) {
                val layoutManager = layoutManager as StaggeredGridLayoutManager
                val lastPositions = layoutManager.findFirstVisibleItemPositions(IntArray(layoutManager.spanCount))
                position = getMinPositions(lastPositions)
            } else {
                position = 0
            }
            return position
        }

    /**
     * 获得当前展示最小的position

     * @param positions
     * *
     * @return
     */
    private fun getMinPositions(positions: IntArray): Int {
        val size = positions.size
        var minPosition = Integer.MAX_VALUE
        for (i in 0..size - 1) {
            minPosition = Math.min(minPosition, positions[i])
        }
        return minPosition
    }

    /**
     * 获取最后一条展示的位置

     * @return
     */
    private val lastVisiblePosition: Int
        get() {
            val position: Int
            if (layoutManager is LinearLayoutManager) {
                position = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            } else if (layoutManager is GridLayoutManager) {
                position = (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
            } else if (layoutManager is StaggeredGridLayoutManager) {
                val layoutManager = layoutManager as StaggeredGridLayoutManager
                val lastPositions = layoutManager.findLastVisibleItemPositions(IntArray(layoutManager.spanCount))
                position = getMaxPosition(lastPositions)
            } else {
                position = layoutManager.itemCount - 1
            }
            return position
        }

    /**
     * 获得最大的位置

     * @param positions
     * *
     * @return
     */
    private fun getMaxPosition(positions: IntArray): Int {
        val size = positions.size
        var maxPosition = Integer.MIN_VALUE
        for (i in 0..size - 1) {
            maxPosition = Math.max(maxPosition, positions[i])
        }
        return maxPosition
    }

    /**
     * 添加头部view

     * @param resId
     */
    fun addHeaderView(resId: Int) {
        mAutoLoadAdapter!!.addHeaderView(resId)
    }

    /**
     * 设置头部view是否展示
     * @param enable
     */
    fun setHeaderEnable(enable: Boolean) {
        mAutoLoadAdapter!!.setHeaderEnable(enable)
    }

    /**
     * 设置是否支持自动加载更多

     * @param autoLoadMore
     */
    fun setAutoLoadMoreEnable(autoLoadMore: Boolean) {
        mIsFooterEnable = autoLoadMore
    }

    /**
     * 通知更多的数据已经加载

     * 每次加载完成之后添加了Data数据，用notifyItemRemoved来刷新列表展示，
     * 而不是用notifyDataSetChanged来刷新列表

     * @param hasMore
     */
    fun notifyMoreFinish(hasMore: Boolean) {
        setAutoLoadMoreEnable(hasMore)
        adapter.notifyItemRemoved(mLoadMorePosition)
        mIsLoadingMore = false
    }

    companion object {
        /**
         * item 类型
         */
        val TYPE_NORMAL = 0
        val TYPE_HEADER = 1//头部--支持头部增加一个headerView
        val TYPE_FOOTER = 2//底部--往往是loading_more
        val TYPE_LIST = 3//代表item展示的模式是list模式
        val TYPE_STAGGER = 4//代码item展示模式是网格模式
    }
}
