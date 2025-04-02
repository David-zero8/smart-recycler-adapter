package smartrecycleradapter.viewevent.listener

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import io.github.zero8.smartrecycleradapter.R
import io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter
import io.github.zero8.smartrecycleradapter.SmartViewHolderType
import smartrecycleradapter.extension.SmartViewHolderBinder
import smartrecycleradapter.extension.findView
import smartrecycleradapter.viewevent.model.ViewEvent
import smartrecycleradapter.viewevent.viewholder.OnItemTouchEventListener
import io.github.zero8.smartrecycleradapter.ViewId
import io.github.zero8.smartrecycleradapter.listener.OnCreateViewHolderListener
import io.github.zero8.smartrecycleradapter.viewholder.SmartViewHolder

/**
 * Contains the logic for the multi view holder views click for recycler adapter positions.
 */
@SuppressLint("ClickableViewAccessibility")
open class OnTouchEventListener(
    override val viewHolderType: SmartViewHolderType = SmartViewHolder::class,
    @IdRes
    override vararg val viewIds: ViewId = intArrayOf(R.id.undefined),
    override val identifier: Any = OnTouchEventListener::class,
    override var eventListener: (ViewEvent.OnTouchEvent) -> Unit
) : OnViewEventListener<ViewEvent.OnTouchEvent>,
    SmartViewHolderBinder,
    OnCreateViewHolderListener {

    override fun onCreateViewHolder(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<Any>
    ) {
        viewIds.forEach {
            with(findView(it, viewHolder)) {
                setOnTouchListener { view, motionEvent ->
                    val event = ViewEvent.OnTouchEvent(
                        adapter,
                        viewHolder,
                        viewHolder.bindingAdapterPosition,
                        view,
                        motionEvent
                    )
                    (viewHolder as? OnItemTouchEventListener)?.let {
                        viewHolder.onViewEvent(event)
                    }
                    eventListener.invoke(event)
                    false
                }
            }
        }
    }
}
