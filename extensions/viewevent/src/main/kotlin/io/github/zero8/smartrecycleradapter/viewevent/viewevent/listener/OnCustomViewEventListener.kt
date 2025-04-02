package smartrecycleradapter.viewevent.listener

import io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter
import io.github.zero8.smartrecycleradapter.listener.OnCreateViewHolderListener
import io.github.zero8.smartrecycleradapter.viewholder.SmartViewHolder
import smartrecycleradapter.extension.SmartViewHolderBinder
import smartrecycleradapter.viewevent.model.ViewEvent
import smartrecycleradapter.viewevent.viewholder.CustomViewEventListenerHolder

/**
 * Contains the logic for passing itself to a [SmartViewHolder]
 * via [CustomViewEventListenerHolder] interface to enable posting of custom [ViewEvent].
 */
open class OnCustomViewEventListener(
    override val identifier: Any = OnCustomViewEventListener::class,
    override var eventListener: (ViewEvent) -> Unit
) : OnViewEventListener<ViewEvent>,
    SmartViewHolderBinder,
    OnCreateViewHolderListener {

    override fun onCreateViewHolder(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<Any>
    ) {
        if (viewHolder is CustomViewEventListenerHolder) {
            viewHolder.customViewEventListener = eventListener
        }
    }
}
