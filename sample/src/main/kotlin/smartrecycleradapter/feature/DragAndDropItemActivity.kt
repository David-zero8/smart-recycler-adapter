package smartrecycleradapter.feature

import android.os.Bundle
import io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter
import smartrecycleradapter.viewevent.dragdrop.AutoDragAndDropBinder
import smartrecycleradapter.viewevent.listener.OnClickEventListener
import smartrecycleradapter.viewevent.listener.OnLongClickEventListener
import smartrecycleradapter.feature.simpleitem.SimpleItemViewHolder
import smartrecycleradapter.utils.showToast

/*
 * Created by Manne Öhlund on 2019-08-11.
 * Copyright (c) All rights reserved.
 */

class DragAndDropItemActivity : BaseSampleActivity() {
    private lateinit var smartRecyclerAdapter: SmartRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Drag & Drop"

        val items = (0..20).toMutableList()

        smartRecyclerAdapter = SmartRecyclerAdapter
            .items(items)
            .map(Integer::class, SimpleItemViewHolder::class)
            .add(OnClickEventListener {
                showToast("onClick ${smartRecyclerAdapter.currentList[it.position].toString()}")
            })
            .add(OnLongClickEventListener {
                showToast("onLongClick ${it.position}")
            })
            .add(AutoDragAndDropBinder(longPressDragEnabled = true) { event ->
                supportActionBar?.subtitle =
                    "onItemMoved from ${event.viewHolder.bindingAdapterPosition} to ${event.targetViewHolder.bindingAdapterPosition}"
            })
            .into<SmartRecyclerAdapter>(binding.recyclerView)

    }
}
