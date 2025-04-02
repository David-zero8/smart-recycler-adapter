package smartrecycleradapter.viewevent.viewholder

/*
 * Created by Manne Öhlund on 2020-01-08.
 * Copyright (c) All rights reserved.
 */

import smartrecycleradapter.viewevent.listener.OnCustomViewEventListener
import smartrecycleradapter.viewevent.model.ViewEvent

/**
 * Lets a view holder handle events with custom event ids.
 * Implement this interface in your [SmartViewHolder] extension.
 */
interface CustomViewEventListenerHolder {

    /**
     * Will be set from corresponding [OnCustomViewEventListener]
     *
     * @see OnCustomViewEventListener
     */
    var customViewEventListener: (ViewEvent) -> Unit
}