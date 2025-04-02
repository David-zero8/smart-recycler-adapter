package io.github.zero8.smartrecycleradapter.viewholder

/*
 * Created by Manne Öhlund on 2019-06-16.
 * Copyright (c) All rights reserved.
 */

import io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter

/**
 * Receiver for [SmartRecyclerAdapter], can be use in [SmartViewHolder] extension that handles nested adapters.
 */
interface SmartAdapterHolder {
    var smartRecyclerAdapter: SmartRecyclerAdapter?
}
