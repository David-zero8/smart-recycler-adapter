package io.github.zero8.smartrecycleradapter.nestedadapter

import androidx.recyclerview.widget.RecyclerView

/**
 * Holds reference to a [androidx.recyclerview.widget.RecyclerView] for a [smartrecycleradapter.viewholder.SmartViewHolder] subclass.
 */
interface SmartNestedRecyclerViewHolder {

    /**
     * Override and assign this reference to the target [androidx.recyclerview.widget.RecyclerView] in a [smartrecycleradapter.viewholder.SmartViewHolder] subclass.
     */
    val recyclerView: RecyclerView
}