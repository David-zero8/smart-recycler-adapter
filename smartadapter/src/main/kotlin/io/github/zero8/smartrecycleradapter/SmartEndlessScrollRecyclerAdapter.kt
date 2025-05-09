package io.github.zero8.smartrecycleradapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import io.github.zero8.smartrecycleradapter.listener.OnLoadMoreListener
import io.github.zero8.smartrecycleradapter.viewholder.LoadMoreViewHolder
import io.github.zero8.smartrecycleradapter.viewholder.SmartViewHolder
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Enables endless scrolling or pagination. Let's the adapter show a [smartrecycleradapter.viewholder.LoadMoreViewHolder] when scrolled to last item.
 */
@Suppress("UNCHECKED_CAST")
class SmartEndlessScrollRecyclerAdapter(
    diffCallback: DiffUtil.ItemCallback<Any> = DEFAULT_DIFF_CALLBACK,
    items: MutableList<Any>,
    backgroundExecutor: Executor = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors() + 1
    )
) : SmartRecyclerAdapter(diffCallback, items, backgroundExecutor), ISmartEndlessScrollRecyclerAdapter {

    private val VIEW_TYPE_LOADING = Integer.MAX_VALUE

    private val endlessScrollOffset: Int
        get() = if (isEndlessScrollEnabled) 1 else 0

    override var isEndlessScrollEnabled: Boolean = true
        set(enable) {
            field = enable
        }
    override var isLoading: Boolean = false
    override var autoLoadMoreEnabled: Boolean = false
    override var onLoadMoreListener: OnLoadMoreListener? = null
    @LayoutRes
    override var loadMoreLayoutResource = R.layout.load_more_view

    override fun getItemViewType(position: Position): ViewType {
        return if (isEndlessScrollEnabled && position == itemCount - endlessScrollOffset) {
            VIEW_TYPE_LOADING
        } else super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: ViewType): SmartViewHolder<Any> {
        return if (viewType == VIEW_TYPE_LOADING) {
            LoadMoreViewHolder(parent, loadMoreLayoutResource, autoLoadMoreEnabled)
        } else super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(smartViewHolder: SmartViewHolder<Any>, position: Position) {
        if (position < itemCount - endlessScrollOffset) {
            super.onBindViewHolder(smartViewHolder, position)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + endlessScrollOffset
    }

    override fun onViewAttachedToWindow(holder: SmartViewHolder<Any>) {
        super.onViewAttachedToWindow(holder)
        if (holder is LoadMoreViewHolder) {
            if (autoLoadMoreEnabled) {
                holder.itemView.post {
                    onLoadMoreListener?.invoke(this, holder)
                }
            } else {
                holder.toggleLoading(false)
                holder.loadMoreButton?.setOnClickListener {
                    holder.itemView.post {
                        onLoadMoreListener?.invoke(this, holder)
                        holder.toggleLoading(true)
                    }
                }
            }
        }
    }

    companion object {

        val DEFAULT_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return false
            }
        }

        /**
         * Builder of [SmartRecyclerAdapter] for easy implementation.
         * @return SmartAdapterBuilder
         */
        fun items(items: List<Any>): SmartEndlessScrollAdapterBuilder =
            SmartEndlessScrollAdapterBuilder().also {
                it.setItems(items)
            }

        /**
         * Builder of [SmartRecyclerAdapter] for easy implementation.
         * @return SmartAdapterBuilder
         */
        fun empty(): SmartEndlessScrollAdapterBuilder = SmartEndlessScrollAdapterBuilder()
    }
}