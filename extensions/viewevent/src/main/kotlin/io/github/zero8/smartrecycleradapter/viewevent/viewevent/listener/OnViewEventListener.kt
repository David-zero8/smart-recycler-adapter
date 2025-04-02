package smartrecycleradapter.viewevent.listener

import smartrecycleradapter.viewevent.model.ViewEvent

/**
 * Defines the base [ViewEvent] passing interface.
 */
interface OnViewEventListener<T : ViewEvent> {

    /**
     * Event listener taking an [ViewEvent] param
     */
    var eventListener: (T) -> Unit
}