package smartrecycleradapter.viewevent.model

import android.view.MotionEvent
import android.view.View
import io.github.zero8.smartrecycleradapter.Position
import io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter
import io.github.zero8.smartrecycleradapter.viewholder.SmartViewHolder
import smartrecycleradapter.viewevent.listener.OnViewEventListener
import smartrecycleradapter.viewevent.swipe.Direction

/**
 * ViewEvent is the data type passed through the [OnViewEventListener.eventListener]
 */
open class ViewEvent(
    val adapter: SmartRecyclerAdapter,
    val viewHolder: SmartViewHolder<*>,
    val position: Position,
    val view: View
) {
    class OnTouchEvent(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<*>,
        position: Position,
        view: View,
        val event: MotionEvent
    ) : ViewEvent(adapter, viewHolder, position, view)

    class OnClick(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<*>,
        position: Position,
        view: View
    ) : ViewEvent(adapter, viewHolder, position, view)

    class OnLongClick(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<*>,
        position: Position,
        view: View
    ) : ViewEvent(adapter, viewHolder, position, view)

    class OnItemSelected(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<*>,
        position: Position,
        view: View,
        val isSelected: Boolean
    ) : ViewEvent(adapter, viewHolder, position, view)

    class OnItemMoved(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<*>,
        position: Position,
        view: View,
        val targetViewHolder: SmartViewHolder<*>
    ) : ViewEvent(adapter, viewHolder, position, view)

    class OnItemSwiped(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<*>,
        position: Position,
        view: View,
        val direction: Direction
    ) : ViewEvent(adapter, viewHolder, position, view)
}