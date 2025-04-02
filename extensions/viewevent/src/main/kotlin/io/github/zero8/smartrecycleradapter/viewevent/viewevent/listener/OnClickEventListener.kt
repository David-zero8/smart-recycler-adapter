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
import smartrecycleradapter.viewevent.viewholder.OnItemClickEventListener
import io.github.zero8.smartrecycleradapter.ViewId
/**
 * Contains the logic for the multi view holder views click for recycler adapter positions.
 */
open class OnClickEventListener(
    override val viewHolderType: SmartViewHolderType = SmartViewHolder::class,
    @IdRes
    override vararg val viewIds: ViewId = intArrayOf(R.id.undefined),
    override val identifier: Any = OnClickEventListener::class,
    override var eventListener: (ViewEvent.OnClick) -> Unit
) : OnViewEventListener<ViewEvent.OnClick>,
    SmartViewHolderBinder,
    OnCreateViewHolderListener {

    override fun onCreateViewHolder(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<Any>
    ) {
        viewIds.forEach {
            with(findView(it, viewHolder)) {
                setOnClickListener { view ->
                    val event = ViewEvent.OnClick(
                        adapter,
                        viewHolder,
                        viewHolder.bindingAdapterPosition,
                        view
                    )
                    (viewHolder as? OnItemClickEventListener)?.let {
                        viewHolder.onViewEvent(event)
                    }
                    eventListener.invoke(event)
                }
            }
        }
    }
}
