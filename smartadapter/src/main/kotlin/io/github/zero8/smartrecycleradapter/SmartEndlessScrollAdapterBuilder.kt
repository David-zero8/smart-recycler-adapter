package io.github.zero8.smartrecycleradapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import io.github.zero8.smartrecycleradapter.listener.OnLoadMoreListener
import java.util.concurrent.Executor

class SmartEndlessScrollAdapterBuilder : SmartAdapterBuilder() {

    private var isEndlessScrollEnabled: Boolean = true
    private var autoLoadMoreEnabled = false
    @LayoutRes
    private var loadMoreLayoutResource = R.layout.load_more_view
    private var onLoadMoreListener: OnLoadMoreListener? = null

    override fun getSmartRecyclerAdapter(): SmartRecyclerAdapter {
        return SmartEndlessScrollRecyclerAdapter(
            diffCallback = diffCallback,
            items = items,
            backgroundExecutor = backgroundExecutor
        ).also {
            it.isEndlessScrollEnabled = isEndlessScrollEnabled
            it.autoLoadMoreEnabled = autoLoadMoreEnabled
            it.loadMoreLayoutResource = loadMoreLayoutResource
            it.onLoadMoreListener = onLoadMoreListener
        }
    }

    /**
     * @see SmartEndlessScrollRecyclerAdapter.isEndlessScrollEnabled
     */
    fun setEndlessScrollEnabled(isEndlessScrollEnabled: Boolean): SmartEndlessScrollAdapterBuilder {
        this.isEndlessScrollEnabled = isEndlessScrollEnabled
        return this
    }

    /**
     * @see SmartEndlessScrollRecyclerAdapter.autoLoadMoreEnabled
     */
    fun setAutoLoadMoreEnabled(autoLoadMoreEnabled: Boolean): SmartEndlessScrollAdapterBuilder {
        this.autoLoadMoreEnabled = autoLoadMoreEnabled
        return this
    }

    /**
     * @see SmartEndlessScrollRecyclerAdapter.loadMoreLayoutResource
     */
    fun setLoadMoreLayoutResource(@LayoutRes loadMoreLayoutResource: Int): SmartEndlessScrollAdapterBuilder {
        this.loadMoreLayoutResource = loadMoreLayoutResource
        return this
    }

    /**
     * @see SmartEndlessScrollRecyclerAdapter.onLoadMoreListener
     */
    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener): SmartEndlessScrollAdapterBuilder {
        this.onLoadMoreListener = onLoadMoreListener
        return this
    }

    override fun setDiffCallback(diffCallback: DiffUtil.ItemCallback<Any>): SmartEndlessScrollAdapterBuilder {
        this.diffCallback = diffCallback
        return this
    }

    override fun setBackgroundExecutor(executor: Executor): SmartEndlessScrollAdapterBuilder {
        this.backgroundExecutor = executor
        return this
    }
}