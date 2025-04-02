package io.github.zero8.smartrecycleradapter.listener

/*
 * Created by Manne Öhlund on 21/09/20.
 * Copyright © 2020. All rights reserved.
 */

import androidx.recyclerview.widget.RecyclerView
import io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter
import io.github.zero8.smartrecycleradapter.viewholder.SmartViewHolder

/**
 * Listener for when a view holder created by the adapter should be bound with data.
 *
 * Invoked from [SmartRecyclerAdapter.onBindViewHolder] and should be implemented in a [SmartViewHolder] extension.
 *
 * @see RecyclerView.Adapter.onBindViewHolder
 */
interface OnBindViewHolderListener {

    /**
     * Called when a view holder created by the adapter should be bound with data.
     * @param adapter target SmartRecyclerAdapter
     * @param viewHolder target ViewHolder
     */
    fun onBindViewHolder(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<Any>
    )

    /**
     * Called when a view holder created by the adapter should be bound with data.
     * @param adapter target SmartRecyclerAdapter
     * @param viewHolder target ViewHolder
     * @param payloads selected payload
     */
    fun onBindViewHolder(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<Any>,
        payloads: MutableList<Any>
    ) = Unit
}
