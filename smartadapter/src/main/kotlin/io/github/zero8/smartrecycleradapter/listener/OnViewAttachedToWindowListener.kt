package io.github.zero8.smartrecycleradapter.listener

/*
 * Created by Manne Öhlund on 21/09/20.
 * Copyright © 2020. All rights reserved.
 */

import androidx.recyclerview.widget.RecyclerView

/**
 * Listener for when a view created by the adapter has been attached to a window.
 *
 * Invoked from [io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter.onViewAttachedToWindow] and should be implemented in a [SmartViewHolder] extension.
 *
 * @see RecyclerView.Adapter.onViewAttachedToWindow
 */
interface OnViewAttachedToWindowListener {

    /**
     * Called when a view created by the adapter has been attached to a window.
     * @param viewHolder target ViewHolder
     */
    fun onViewAttachedToWindow(viewHolder: RecyclerView.ViewHolder)
}
