package smartrecycleradapter.feature

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter
import smartrecycleradapter.viewevent.listener.OnClickEventListener
import smartrecycleradapter.viewevent.listener.OnLongClickEventListener
import smartrecycleradapter.viewevent.model.ViewEvent
import smartrecycleradapter.viewevent.viewholder.OnItemClickEventListener
import smartrecycleradapter.viewevent.viewholder.OnItemLongClickEventListener
import smartrecycleradapter.feature.simpleitem.SimpleItemViewHolder
import smartrecycleradapter.utils.showToast

/*
 * Created by Manne Öhlund on 2019-08-11.
 * Copyright (c) All rights reserved.
 */

class SimpleItemOnClickOnLongClickActivity : BaseSampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "onClick onLongClick Sample"

        val items = (0..100).toMutableList()

        SmartRecyclerAdapter
            .items(items)
            .map(Integer::class, SimpleEventListenerViewHolder::class)
            .add(OnClickEventListener {
                showToast("onItemClick ${it.position}")
            })
            .add(OnClickEventListener {
                showToast("onItemClick ${it.position}")
            })
            .add(OnLongClickEventListener {
                showToast("onItemLongClick ${it.position}")
            })
            .into<SmartRecyclerAdapter>(binding.recyclerView)
    }

    class SimpleEventListenerViewHolder(parentView: ViewGroup) : SimpleItemViewHolder(parentView),
        OnItemClickEventListener,
        OnItemLongClickEventListener {

        override fun onViewEvent(event: ViewEvent.OnClick) {
            Toast.makeText(itemView.context, "SimpleEventListenerViewHolder ${event::class.simpleName} intercept", Toast.LENGTH_SHORT).show()
        }

        override fun onViewEvent(event: ViewEvent.OnLongClick) {
            Toast.makeText(itemView.context, "SimpleEventListenerViewHolder ${event::class.simpleName} intercept", Toast.LENGTH_SHORT).show()
        }
    }
}
