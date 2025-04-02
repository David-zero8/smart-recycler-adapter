package smartrecycleradapter.viewholders

/*
 * Created by Manne Öhlund on 2019-07-16.
 * Copyright (c) All rights reserved.
 */

import android.view.View

import smartrecycleradapter.viewholder.SmartViewHolder

open class BindableTestViewHolder(view: View) : SmartViewHolder<Any>(view) {

    override fun bind(item: Any) { }

    override fun unbind() { }
}
