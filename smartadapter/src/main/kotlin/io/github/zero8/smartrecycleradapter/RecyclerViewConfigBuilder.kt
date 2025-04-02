package io.github.zero8.smartrecycleradapter

import androidx.recyclerview.widget.RecyclerView
import io.github.zero8.smartrecycleradapter.viewholder.SmartViewHolder


typealias RecyclerViewBinder = (viewHolder: SmartViewHolder<*>, recyclerView: RecyclerView) -> Unit