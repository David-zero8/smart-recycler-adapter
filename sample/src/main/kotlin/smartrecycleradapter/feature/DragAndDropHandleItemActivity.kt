package smartrecycleradapter.feature

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import io.github.zero8.smartrecycleradapter.sample.R
import smartadapter.SmartRecyclerAdapter
import smartadapter.viewevent.dragdrop.AutoDragAndDropBinder
import smartadapter.viewevent.listener.OnClickEventListener
import smartadapter.viewevent.listener.OnLongClickEventListener
import smartadapter.viewholder.DraggableViewHolder
import smartadapter.viewholder.SmartViewHolder
import smartrecycleradapter.utils.showToast

/*
 * Created by Manne Öhlund on 2019-08-11.
 * Copyright (c) All rights reserved.
 */

class DragAndDropHandleItemActivity : BaseSampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Drag & Drop handle"

        val items = (0..50).toMutableList()

        SmartRecyclerAdapter
            .items(items)
            .map(Integer::class, SimpleDragHandleItemViewHolder::class)
            .add(OnClickEventListener {
                showToast("onClick ${it.position}")
            })
            .add(OnLongClickEventListener {
                showToast("onLongClick ${it.position}")
            })
            .add(AutoDragAndDropBinder(ItemTouchHelper.UP or ItemTouchHelper.DOWN) {
                supportActionBar?.subtitle =
                    "onItemMoved from ${it.viewHolder.bindingAdapterPosition} to ${it.targetViewHolder.bindingAdapterPosition}"
            })
            .into<SmartRecyclerAdapter>(binding.recyclerView)
    }

    class SimpleDragHandleItemViewHolder(parentView: ViewGroup) :
        SmartViewHolder<Int>(parentView, R.layout.simple_drag_handle_item),
        DraggableViewHolder {

        private var textView: TextView = itemView.findViewById(R.id.textView)
        private val dragHandle: View = itemView.findViewById(R.id.dragHandle)

        override var draggableView = dragHandle

        override fun bind(item: Int) {
            textView.text = "Item $item"
        }
    }
}
