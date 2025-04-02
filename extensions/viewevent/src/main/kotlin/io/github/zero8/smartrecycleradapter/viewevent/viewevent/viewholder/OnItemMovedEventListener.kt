package smartrecycleradapter.viewevent.viewholder

import smartrecycleradapter.viewevent.model.ViewEvent

interface OnItemMovedEventListener {

    /**
     * Called when event bound view triggers an triggers an [ViewEvent.OnItemMoved] event.
     */
    fun onItemMoved(event: ViewEvent.OnItemMoved)
}
