package smartrecycleradapter.feature.simpleitem

import android.view.ViewGroup
import android.widget.TextView
import io.github.zero8.smartrecycleradapter.sample.R
import io.github.zero8.smartrecycleradapter.viewholder.SmartViewHolder

/*
 * Created by Manne Öhlund on 2019-08-04.
 * Copyright (c) All rights reserved.
 */

open class SimpleItemViewHolder(parentView: ViewGroup) :
    SmartViewHolder<Int>(parentView, R.layout.simple_item) {

    override fun bind(item: Int) {
        (itemView as TextView).text = "Item $item"
    }
}
