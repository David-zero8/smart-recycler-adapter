package io.github.zero8.smartrecycleradapter.listener

/*
 * Created by Manne Öhlund on 21/09/20.
 * Copyright © 2020. All rights reserved.
 */

import androidx.recyclerview.widget.RecyclerView


/**
 * Listener for when a view created by this adapter has been detached from its window.
 *
 * Invoked from [io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter.onViewDetachedFromWindow] and should be implemented in a [SmartViewHolder] extension.
 *
 * @see RecyclerView.Adapter.onViewDetachedFromWindow
 */
interface OnViewDetachedFromWindowListener {

    /**
     * Called when a view created by the adapter has been detached from its window.
     * @param viewHolder target ViewHolder
     */
    fun onViewDetachedFromWindow(viewHolder: RecyclerView.ViewHolder)
}
