package io.github.zero8.smartrecycleradapter

import io.github.zero8.smartrecycleradapter.listener.OnLoadMoreListener

/**
 * Defines the extension methods for [SmartEndlessScrollRecyclerAdapter].
 */
interface ISmartEndlessScrollRecyclerAdapter {

    /**
     * Checks if endless scrolling is enabled.
     */
    var isEndlessScrollEnabled: Boolean

    /**
     * Checks if the [SmartEndlessScrollRecyclerAdapter] is in loading state.
     * Good to use when async loading takes time and user scrolls back and forth.
     */
    var isLoading: Boolean

    /**
     * Enables or disables the auto load more view.
     *
     *  * Enabled state shows and indeterminate spinner.
     *  * Disabled state shows an load more button for passive activation.
     */
    var autoLoadMoreEnabled: Boolean

    /**
     * [smartrecycleradapter.listener.OnLoadMoreListener] callback for listening on when the [SmartEndlessScrollRecyclerAdapter]
     * is showing the [smartrecycleradapter.viewholder.LoadMoreViewHolder].
     */
    var onLoadMoreListener: OnLoadMoreListener?

    /**
     * Enables customization of the layout for the [smartrecycleradapter.viewholder.LoadMoreViewHolder].
     */
    var loadMoreLayoutResource: Int
}