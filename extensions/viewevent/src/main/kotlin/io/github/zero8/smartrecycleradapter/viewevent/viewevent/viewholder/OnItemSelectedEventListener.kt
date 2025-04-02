package smartrecycleradapter.viewevent.viewholder

import smartrecycleradapter.viewevent.model.ViewEvent

/**
 * Smart adapter item view selection listener.
 */
interface OnItemSelectedEventListener {

    /**
     * Called when event bound view triggers an triggers an [ViewEvent.OnItemSelected] event.
     * Overrides the [OnMultiItemSelectObserver.isSelected] call.
     */
    fun onItemSelect(event: ViewEvent.OnItemSelected)
}
