package smartrecycleradapter.feature

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import smartadapter.SmartRecyclerAdapter
import smartadapter.viewevent.dragdrop.AutoDragAndDropBinder
import smartadapter.viewevent.listener.OnClickEventListener
import smartadapter.viewevent.listener.OnLongClickEventListener
import smartadapter.viewevent.swipe.AutoRemoveItemSwipeEventBinder
import smartrecycleradapter.feature.simpleitem.SimpleItemViewHolder
import smartrecycleradapter.utils.showToast

/*
 * Created by Manne Öhlund on 2019-08-11.
 * Copyright (c) All rights reserved.
 */

class MultipleEventsAndExtensionsActivity : BaseSampleActivity() {

    private lateinit var smartRecyclerAdapter: SmartRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Drag & Drop, Swipe, ViewEvents"

        val items = (0..50).toMutableList()

        smartRecyclerAdapter = SmartRecyclerAdapter
            .items(items)
            .map(Integer::class, SimpleItemViewHolder::class)
            .add(OnClickEventListener {
                showToast("onClick ${smartRecyclerAdapter.currentList[it.position].toString()}")
            })
            .add(OnLongClickEventListener {
                showToast("onLongClick ${it.position}")
            })
            .add(AutoDragAndDropBinder(ItemTouchHelper.UP or ItemTouchHelper.DOWN, longPressDragEnabled = true) { event ->
                supportActionBar?.subtitle =
                    "onItemMoved from ${event.viewHolder.bindingAdapterPosition} to ${event.targetViewHolder.bindingAdapterPosition}"
            })
            .add(AutoRemoveItemSwipeEventBinder(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) { event ->
                showToast(event.viewHolder, event.direction)
            })
            .into(binding.recyclerView)
    }

    val showToast = { viewHolder: RecyclerView.ViewHolder, direction: Int ->
        val directionStr = when (direction) {
            ItemTouchHelper.LEFT -> "Left"
            ItemTouchHelper.RIGHT -> "Right"
            else -> "??"
        }
        Toast.makeText(
            applicationContext,
            "onItemSwiped @ ${viewHolder.bindingAdapterPosition}, $directionStr",
            Toast.LENGTH_SHORT
        ).show()
    }
}
