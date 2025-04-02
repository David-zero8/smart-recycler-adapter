package io.github.zero8.smartrecycleradapter.listener

/*
 * Created by Manne Öhlund on 2019-07-23.
 * Copyright © 2019. All rights reserved.
 */

import io.github.zero8.smartrecycleradapter.SmartEndlessScrollRecyclerAdapter
import io.github.zero8.smartrecycleradapter.viewholder.LoadMoreViewHolder

/**
 * Type alias lambda callback for [SmartEndlessScrollRecyclerAdapter] to intercept when the adapter
 * has scrolled to the last item. The [smartrecycleradapter.viewholder.LoadMoreViewHolder] will show without any
 * mapped data type.
 */
typealias OnLoadMoreListener = (adapter: SmartEndlessScrollRecyclerAdapter, loadMoreViewHolder: LoadMoreViewHolder) -> Unit
