package io.github.zero8.smartrecycleradapter.listener

import androidx.recyclerview.widget.RecyclerView

/**
 * Listener for when [io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter] has been detached to the target recycler view.
 *
 * Invoked from [io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter.onDetachedFromRecyclerView] and can be implemented in a [smartrecycleradapter.binders.SmartRecyclerAdapterExtension] that needs a recycler view.
 *
 * @see RecyclerView.Adapter.onDetachedFromRecyclerView
 */
interface OnDetachedFromRecyclerViewListener {

    /**
     * Called when [io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter] has been detached to the target recycler view.
     *
     * @param recyclerView target recycler view
     */
    fun onDetachedFromRecyclerView(recyclerView: RecyclerView)
}