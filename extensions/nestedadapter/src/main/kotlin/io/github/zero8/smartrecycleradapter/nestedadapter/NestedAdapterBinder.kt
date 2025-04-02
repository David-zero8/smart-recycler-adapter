package io.github.zero8.smartrecycleradapter.nestedadapter

import io.github.zero8.smartrecycleradapter.RecyclerViewBinder
import io.github.zero8.smartrecycleradapter.SmartAdapterBuilder

/**
 * Defines the basic NestedAdapterBinder.
 */
interface NestedAdapterBinder {
    /**
     * Reusable [SmartAdapterBuilder] to create a new [io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter] for each [smartrecycleradapter.viewholder.SmartViewHolder] subclass of type [SmartNestedRecyclerViewHolder]
     */
    val smartRecyclerAdapterBuilder: SmartAdapterBuilder

    /**
     * With [RecyclerViewBinder] you can target specific [SmartNestedRecyclerViewHolder] subclasses to define and set [androidx.recyclerview.widget.RecyclerView] properties like [androidx.recyclerview.widget.RecyclerView.LayoutManager].
     */
    var recyclerViewBinder: RecyclerViewBinder?
}