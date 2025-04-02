package smartrecycleradapter.feature

import android.os.Bundle
import androidx.activity.viewModels
import io.github.zero8.smartrecycleradapter.sample.R
import io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter
import smartrecycleradapter.viewevent.listener.OnSingleItemSelectListener
import smartrecycleradapter.viewevent.model.ViewEvent
import smartrecycleradapter.viewevent.viewmodel.ViewEventViewModel
import smartrecycleradapter.utils.showToast


/*
 * Created by Manne Öhlund on 2019-08-23.
 * Copyright (c) All rights reserved.
 */

class SingleExpandableItemActivity : BaseSampleActivity() {

    class SingleItemSelectViewModel : ViewEventViewModel<ViewEvent, OnSingleItemSelectListener>(
        OnSingleItemSelectListener(
            viewHolderType = SimpleExpandableItemViewHolder::class,
            viewId = R.id.itemTitle
        )
    )

    private val singleItemSelectViewModel: SingleItemSelectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Single Expandable Item"

        val items = (0..100).toMutableList()

        SmartRecyclerAdapter
            .items(items)
            .map(Integer::class, SimpleExpandableItemViewHolder::class)
            .add(singleItemSelectViewModel.observe(this) {
                showToast("onSelect ${it.position}")
            })
            .into<SmartRecyclerAdapter>(binding.recyclerView)
    }
}
