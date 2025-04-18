package smartrecycleradapter.viewevent.listener

/*
 * Created by Manne Öhlund on 2020-09-23.
 * Copyright (c) All rights reserved.
 */

import android.graphics.Color
import android.widget.CompoundButton
import androidx.annotation.IdRes
import io.github.zero8.smartrecycleradapter.R
import io.github.zero8.smartrecycleradapter.Position
import io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter
import io.github.zero8.smartrecycleradapter.SmartViewHolderType
import io.github.zero8.smartrecycleradapter.ViewId
import io.github.zero8.smartrecycleradapter.listener.OnBindViewHolderListener
import io.github.zero8.smartrecycleradapter.listener.OnCreateViewHolderListener
import io.github.zero8.smartrecycleradapter.viewholder.SmartViewHolder
import smartrecycleradapter.extension.SmartViewHolderBinder
import smartrecycleradapter.extension.findView
import smartrecycleradapter.extension.setBackgroundAttribute
import smartrecycleradapter.viewevent.model.ViewEvent
import smartrecycleradapter.viewevent.state.SmartStateHolder
import smartrecycleradapter.viewevent.viewholder.OnItemSelectedEventListener
import smartrecycleradapter.viewevent.viewmodel.ViewEventViewModel
import java.util.TreeSet
import kotlin.reflect.KClass

/**
 * Extends [OnMultiItemSelectListener] and contains the logic for the single check states for recycler adapter positions.
 */
open class OnSingleItemCheckListener(
    override val identifier: Any = OnSingleItemCheckListener::class,
    override val viewHolderType: SmartViewHolderType = SmartViewHolder::class,
    selectableItemType: KClass<*> = Any::class,
    @IdRes viewId: ViewId = R.id.undefined
) : OnMultiItemCheckListener(
    viewId = viewId,
    selectableItemType = selectableItemType
) {

    /**
     * Adds the position to the data set and [.disable]s any old positions.
     * @param position the adapter position
     */
    override fun enable(position: Position) {
        for (oldPositions in selectedItems) {
            disable(oldPositions)
        }
        clear()
        super.enable(position)
    }

    /**
     * Removes the position from the data set and calls [SmartRecyclerAdapter.smartNotifyItemChanged].
     * @param position the adapter position
     */
    override fun disable(position: Position) {
        super.disable(position)
    }
}

/**
 * Extends [OnMultiItemSelectListener] and contains the logic for the single selection states for recycler adapter positions.
 */
open class OnSingleItemSelectListener(
    override val identifier: Any = OnSingleItemSelectListener::class,
    override val viewHolderType: SmartViewHolderType = SmartViewHolder::class,
    selectableItemType: KClass<*> = Any::class,
    @IdRes viewId: ViewId = R.id.undefined
) : OnMultiItemSelectListener(
    enableOnLongClick = false,
    selectableItemType = selectableItemType,
    viewId = viewId
) {

    /**
     * Adds the position to the data set and [.disable]s any old positions.
     * @param position the adapter position
     */
    override fun enable(position: Position) {
        for (oldPositions in selectedItems) {
            disable(oldPositions)
        }
        clear()
        super.enable(position)
    }

    /**
     * Removes the position from the data set and calls [SmartRecyclerAdapter.smartNotifyItemChanged].
     * @param position the adapter position
     */
    override fun disable(position: Position) {
        super.disable(position)
    }
}

/**
 * Extends [OnMultiItemSelectListener] and contains the logic for the multi check states for recycler adapter positions.
 */
open class OnMultiItemCheckListener(
    override val identifier: Any = OnMultiItemCheckListener::class,
    override val viewHolderType: SmartViewHolderType = SmartViewHolder::class,
    selectableItemType: KClass<*> = Any::class,
    @IdRes viewId: ViewId = R.id.undefined
) : OnMultiItemSelectListener(
    enableOnLongClick = false,
    selectableItemType = selectableItemType,
    viewId = viewId
) {

    override fun onCreateViewHolder(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<Any>
    ) {
        smartRecyclerAdapter = adapter

        val view = findView(viewId, viewHolder)

        with(view) {
            setOnClickListener {
                toggle(viewHolder.bindingAdapterPosition)
                eventListener.invoke(
                    ViewEvent.OnItemSelected(
                        adapter,
                        viewHolder,
                        viewHolder.bindingAdapterPosition,
                        view,
                        isSelected(viewHolder.bindingAdapterPosition)
                    )
                )
            }
        }
    }

    override fun setSelected(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<Any>
    ) {
        val view = viewHolder.itemView.findViewById<CompoundButton>(viewId)
        view.isChecked = isSelected(viewHolder.bindingAdapterPosition)
    }
}

/**
 * Contains the logic for the multi select states for recycler adapter positions.
 *
 * if [enableOnLongClick] is true multi select will be enabled after a long click, otherwise a regular [ViewEvent.OnClick] will be emitted when tapping.<br/>
 * [viewId] is by default [R.id.undefined] to target all [SmartViewHolder.itemView].<br/>
 * [viewHolderType] is by default [SmartViewHolder]::class to target all view holders.
 * [eventListener] is by default noop in case of [OnMultiItemSelectListener] will be used with [ViewEventViewModel] along with live data observer.
 */
open class OnMultiItemSelectListener(
    override val identifier: Any = OnMultiItemSelectListener::class,
    val enableOnLongClick: Boolean = true,
    override val viewHolderType: SmartViewHolderType = SmartViewHolder::class,
    internal val selectableItemType: KClass<*> = Any::class,
    @IdRes val viewId: ViewId = R.id.undefined,
    override var eventListener: (ViewEvent) -> Unit = {}
) : OnViewEventListener<ViewEvent>,
    SmartViewHolderBinder,
    SmartStateHolder,
    OnCreateViewHolderListener,
    OnBindViewHolderListener {

    /**
     * The target [SmartRecyclerAdapter].
     */
    lateinit var smartRecyclerAdapter: SmartRecyclerAdapter

    /**
     * Provides sorted set of selected positions.
     */
    override var selectedItems = TreeSet<Int>()

    /**
     * Provides selected item count.
     */
    val selectedItemsCount: Int
        get() = selectedItems.size

    override fun enable(position: Position) {
        selectedItems.add(position)
    }

    override fun enableAll() {
        smartRecyclerAdapter.getItems().forEachIndexed { index, item ->
            if (selectableItemType.isInstance(item)) {
                enable(index)
            }
        }
    }

    override fun disable(position: Position) {
        selectedItems.remove(position)
    }

    override fun disableAll() {
        selectedItems.toIntArray().forEach { position ->
            disable(position)
        }
    }

    /**
     * Toggles selection of a position in adapter and calls [SmartRecyclerAdapter.smartNotifyItemChanged].
     * @param position the adapter position
     */
    override fun toggle(position: Position) {
        if (selectedItems.contains(position)) {
            disable(position)
        } else {
            enable(position)
        }
    }

    override fun clear() {
        selectedItems.clear()
    }

    /**
     * Checks if position is selected.
     * @param position position in adapter
     * @return true if position is contained in the selection set
     */
    fun isSelected(position: Position): Boolean {
        return selectedItems.contains(position)
    }

    /**
     * Removes selected items in the adapter with animation then clears the state holder set.
     */
    fun removeSelections() {
        for (position in selectedItems.descendingSet()) {
            smartRecyclerAdapter.removeItem(position, notifyDataSetChanged = false) {

            }
        }
        smartRecyclerAdapter.commitPendingItems() {
            selectedItems.clear()
        }

    }

    override fun onCreateViewHolder(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<Any>
    ) {
        smartRecyclerAdapter = adapter

        val view = findView(viewId, viewHolder)

        with(view) {
            if (enableOnLongClick) {
                setOnLongClickListener {
                    toggle(viewHolder.bindingAdapterPosition)
                    setSelected(adapter, viewHolder)
                    eventListener.invoke(
                        ViewEvent.OnItemSelected(
                            adapter,
                            viewHolder,
                            viewHolder.bindingAdapterPosition,
                            view,
                            isSelected(viewHolder.bindingAdapterPosition)
                        )
                    )
                    true
                }
            }
            setOnClickListener {
                if (!enableOnLongClick
                    || enableOnLongClick
                    && selectedItemsCount > 0
                ) {
                    toggle(viewHolder.bindingAdapterPosition)
                    setSelected(adapter, viewHolder)
                    eventListener.invoke(
                        ViewEvent.OnItemSelected(
                            adapter,
                            viewHolder,
                            viewHolder.bindingAdapterPosition,
                            view,
                            isSelected(viewHolder.bindingAdapterPosition)
                        )
                    )
                } else {
                    eventListener.invoke(
                        ViewEvent.OnClick(
                            adapter,
                            viewHolder,
                            viewHolder.bindingAdapterPosition,
                            view
                        )
                    )
                }
            }
        }
    }

    override fun onBindViewHolder(adapter: SmartRecyclerAdapter, viewHolder: SmartViewHolder<Any>) {
        setSelected(adapter, viewHolder)
    }

    open fun setSelected(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<Any>
    ) {
        if (viewHolder is OnItemSelectedEventListener) {
            viewHolder.onItemSelect(
                ViewEvent.OnItemSelected(
                    adapter,
                    viewHolder,
                    viewHolder.bindingAdapterPosition,
                    viewHolder.itemView,
                    isSelected(viewHolder.bindingAdapterPosition)
                )
            )
        } else {
            if (isSelected(viewHolder.bindingAdapterPosition)) {
                viewHolder.itemView.setBackgroundColor(Color.RED)
            } else {
                viewHolder.itemView.setBackgroundAttribute(android.R.attr.selectableItemBackground)
            }
        }
    }
}
