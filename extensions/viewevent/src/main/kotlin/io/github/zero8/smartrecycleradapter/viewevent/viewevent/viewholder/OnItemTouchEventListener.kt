package smartrecycleradapter.viewevent.viewholder

import smartrecycleradapter.viewevent.model.ViewEvent

/**
 * Smart adapter item view touch listener.
 */
interface OnItemTouchEventListener {

    fun onViewEvent(event: ViewEvent.OnTouchEvent)
}
