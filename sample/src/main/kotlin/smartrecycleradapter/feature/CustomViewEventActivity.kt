package smartrecycleradapter.feature

/*
 * Created by Manne Öhlund on 2020-09-23.
 * Copyright (c) All rights reserved.
 */

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.zero8.smartrecycleradapter.sample.R
import smartadapter.Position
import smartadapter.SmartRecyclerAdapter
import smartadapter.viewevent.listener.OnCustomViewEventListener
import smartadapter.viewevent.model.ViewEvent
import smartadapter.viewevent.viewholder.CustomViewEventListenerHolder
import smartadapter.viewholder.SmartAdapterHolder
import smartadapter.viewholder.SmartViewHolder
import smartrecycleradapter.utils.showToast

@SuppressLint("ClickableViewAccessibility")
class CustomViewEventActivity : BaseSampleActivity() {

    class CustomEvent(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<*>,
        position: Position,
        view: View
    ) : ViewEvent(adapter, viewHolder, position, view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Custom View Event Sample"

        val items = (0..100).toMutableList()

        SmartRecyclerAdapter
            .items(items)
            .map(Integer::class, SimpleCustomViewEventListenerViewHolder::class)
            .add(OnCustomViewEventListener { event ->
                showToast(event)
            })
            .into<SmartRecyclerAdapter>(binding.recyclerView)
    }

    fun showToast(event: ViewEvent) {
        val touchEvent =
            (event as? ViewEvent.OnTouchEvent)?.run { "\n\n${this.event}" } ?: ""
        showToast("${event.viewHolder::class.simpleName}\n${event::class.simpleName} ${event.position} $touchEvent")
    }

    open class SimpleCustomViewEventListenerViewHolder(parentView: ViewGroup) :
        SmartViewHolder<Int>(parentView, R.layout.simple_item),
        CustomViewEventListenerHolder,
        SmartAdapterHolder {

        override lateinit var customViewEventListener: (ViewEvent) -> Unit
        override var smartRecyclerAdapter: SmartRecyclerAdapter? = null

        init {
            itemView.setOnTouchListener { view: View, event: MotionEvent ->
                when (event.action) {
                    MotionEvent.ACTION_CANCEL -> {
                        customViewEventListener.invoke(
                            ViewEvent.OnTouchEvent(
                                smartRecyclerAdapter!!,
                                this,
                                bindingAdapterPosition,
                                view,
                                event
                            )
                        )
                    }
                }
                false
            }

            itemView.setOnClickListener { view ->
                customViewEventListener.invoke(
                    ViewEvent.OnClick(
                        smartRecyclerAdapter!!,
                        this,
                        bindingAdapterPosition,
                        view
                    )
                )
            }

            itemView.setOnLongClickListener { view ->
                customViewEventListener.invoke(
                    CustomEvent(
                        smartRecyclerAdapter!!,
                        this,
                        bindingAdapterPosition,
                        view
                    )
                )
                true
            }
        }

        override fun bind(item: Int) {
            (itemView as TextView).text = "Item $item"
        }
    }
}
