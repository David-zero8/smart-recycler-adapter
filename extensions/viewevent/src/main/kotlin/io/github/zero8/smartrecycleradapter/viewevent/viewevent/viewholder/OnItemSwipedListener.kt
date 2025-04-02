package smartrecycleradapter.viewevent.viewholder

import smartrecycleradapter.viewevent.model.ViewEvent
import smartrecycleradapter.viewevent.swipe.SwipeEventBinder

interface OnItemSwipedListener {

    /**
     * Called when event bound view triggers an [ViewEvent.OnItemSwiped] event.
     *
     * @see [SwipeEventBinder]
     */
    fun onItemSwiped(event: ViewEvent.OnItemSwiped)
}
