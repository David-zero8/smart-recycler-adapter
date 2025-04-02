package smartrecycleradapter.viewholder

/*
 * Created by Manne Öhlund on 04/10/17.
 * Copyright © 2017. All rights reserved.
 */

import android.view.ViewGroup
import android.widget.TextView
import io.github.zero8.smartrecycleradapter.sample.R
import io.github.zero8.smartrecycleradapter.viewholder.SmartViewHolder

open class SmallHeaderViewHolder(parentView: ViewGroup) :
    SmartViewHolder<String>(parentView = parentView, layout = R.layout.header) {

    private val summary: TextView = itemView as TextView

    init {
        val params = summary.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(params.leftMargin, params.topMargin / 2, params.rightMargin, params.bottomMargin)
        summary.layoutParams = params
        summary.textSize = summary.textSize / 3
    }

    override fun bind(text: String) {
        summary.text = text
    }
}

open class HeaderViewHolder(parentView: ViewGroup) :
    SmartViewHolder<String>(parentView = parentView, layout = R.layout.header) {

    private val summary: TextView = itemView as TextView

    override fun bind(text: String) {
        summary.text = text
    }
}
