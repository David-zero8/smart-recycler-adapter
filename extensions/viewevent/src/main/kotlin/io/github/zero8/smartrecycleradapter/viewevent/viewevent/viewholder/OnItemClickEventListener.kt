package smartrecycleradapter.viewevent.viewholder

import smartrecycleradapter.viewevent.model.ViewEvent

/**
 * Smart adapter item view click listener.
 */
interface OnItemClickEventListener {

    fun onViewEvent(event: ViewEvent.OnClick)
}
