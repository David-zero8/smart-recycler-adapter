package io.github.zero8.smartrecycleradapter.nestedadapter

import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView
import io.github.zero8.smartrecycleradapter.RecyclerViewBinder
import io.github.zero8.smartrecycleradapter.SmartAdapterBuilder
import io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter
import io.github.zero8.smartrecycleradapter.SmartViewHolderType
import io.github.zero8.smartrecycleradapter.listener.OnAttachedToRecyclerViewListener
import io.github.zero8.smartrecycleradapter.listener.OnBindViewHolderListener
import io.github.zero8.smartrecycleradapter.listener.OnCreateViewHolderListener
import io.github.zero8.smartrecycleradapter.listener.OnViewRecycledListener
import io.github.zero8.smartrecycleradapter.viewholder.SmartViewHolder
import smartrecycleradapter.extension.SmartViewHolderBinder

/**
 * Holds map of multiple [nestedAdapters] that binds with main parent [SmartRecyclerAdapter] data items.
 *
 * All data items must implement [SmartNestedItem] to resolve [SmartNestedItem.items].
 *
 * All [smartrecycleradapter.viewholder.SmartViewHolder] must implement [SmartNestedRecyclerViewHolder] in order to bind the recyclerView to target [nestedAdapters].
 *
 * [SmartNestedAdapterBinder] also saves scroll states for all [nestedAdapters].
 */
class SmartNestedAdapterBinder(
    override val identifier: Any = SmartNestedAdapterBinder::class,
    override val viewHolderType: SmartViewHolderType,
    override val smartRecyclerAdapterBuilder: SmartAdapterBuilder,
    override var recyclerViewBinder: RecyclerViewBinder? = null,
    var reuseParentAdapterRecycledViewPool: Boolean = false
) : SmartViewHolderBinder,
    NestedAdapterBinder,
    OnCreateViewHolderListener,
    OnBindViewHolderListener,
    OnViewRecycledListener,
    OnAttachedToRecyclerViewListener {

    private val nestedAdapters = mutableMapOf<SmartViewHolder<Any>, SmartRecyclerAdapter>()
    private val scrollStates = mutableMapOf<Int, Parcelable?>()
    private var recycledViewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<Any>
    ) {
        if (!viewHolderType.isInstance(viewHolder) && nestedAdapters.contains(viewHolder)) {
            return
        }

        if (!nestedAdapters.contains(viewHolder)) {
            nestedAdapters[viewHolder] = smartRecyclerAdapterBuilder.create()
        }

        with((viewHolder as SmartNestedRecyclerViewHolder).recyclerView) {
            recyclerViewBinder?.invoke(viewHolder, this)
            setRecycledViewPool(recycledViewPool)
        }
    }

    override fun onBindViewHolder(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<Any>
    ) {
        if (!viewHolderType.isInstance(viewHolder) && !nestedAdapters.contains(viewHolder)) {
            return
        }

        val items = (adapter.getItem(viewHolder.bindingAdapterPosition) as SmartNestedItem<*>).items
        (viewHolder as SmartNestedRecyclerViewHolder).recyclerView.adapter =
            nestedAdapters[viewHolder]!!.also {
                it.setItems(items as MutableList<*>)
            }

        // Restore scroll state
        (viewHolder as SmartNestedRecyclerViewHolder).recyclerView.layoutManager?.onRestoreInstanceState(scrollStates[viewHolder.bindingAdapterPosition])
    }

    override fun onViewRecycled(adapter: SmartRecyclerAdapter, viewHolder: SmartViewHolder<Any>) {
        if (!viewHolderType.isInstance(viewHolder) && !nestedAdapters.contains(viewHolder)) {
            return
        }

        // Save scroll state
        scrollStates[viewHolder.bindingAdapterPosition] = (viewHolder as SmartNestedRecyclerViewHolder).recyclerView.layoutManager?.onSaveInstanceState()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        if (reuseParentAdapterRecycledViewPool) {
            recycledViewPool = recyclerView.recycledViewPool
        }
    }
}