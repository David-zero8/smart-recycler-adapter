package smartrecycleradapter.viewevent.listener

import androidx.annotation.IdRes
import io.github.zero8.smartrecycleradapter.R
import io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter
import io.github.zero8.smartrecycleradapter.SmartViewHolderType
import io.github.zero8.smartrecycleradapter.listener.OnCreateViewHolderListener
import io.github.zero8.smartrecycleradapter.viewholder.SmartViewHolder
import smartrecycleradapter.extension.SmartViewHolderBinder
import smartrecycleradapter.extension.findView
import smartrecycleradapter.viewevent.model.ViewEvent
import smartrecycleradapter.viewevent.viewholder.OnItemLongClickEventListener
import io.github.zero8.smartrecycleradapter.ViewId

/**
 * Contains the logic for the multi view holder views click for recycler adapter positions.
 */
open class OnLongClickEventListener(
    override val viewHolderType: SmartViewHolderType = SmartViewHolder::class,
    @IdRes override vararg val viewIds: ViewId = intArrayOf(R.id.undefined),
    override val identifier: Any = OnLongClickEventListener::class,
    override var eventListener: (ViewEvent.OnLongClick) -> Unit
) : OnViewEventListener<ViewEvent.OnLongClick>,
    SmartViewHolderBinder,
    OnCreateViewHolderListener {

    override fun onCreateViewHolder(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<Any>
    ) {
        viewIds.forEach {
            with(findView(it, viewHolder)) {
                setOnLongClickListener { view ->
                    val event = ViewEvent.OnLongClick(
                        adapter,
                        viewHolder,
                        viewHolder.bindingAdapterPosition,
                        view
                    )
                    if (viewHolder is OnItemLongClickEventListener) {
                        viewHolder.onViewEvent(event)
                    }
                    eventListener.invoke(event)
                    true
                }
            }
        }
    }
}
