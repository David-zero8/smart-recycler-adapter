package smartrecycleradapter.feature

/*
 * Created by Manne Öhlund on 2020-10-10.
 * Copyright (c) All rights reserved.
 */

import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter
import io.github.zero8.smartrecycleradapter.stickyheader.StickyHeaderItemDecorationExtension
import smartrecycleradapter.feature.simpleitem.SimpleHeaderViewHolder
import smartrecycleradapter.feature.simpleitem.SimpleItemViewHolder
import smartrecycleradapter.utils.showToast

class StickyHeaderActivity : BaseSampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Sticky header"

        val items = (0..200).toMutableList().toIntArray().mapIndexed { index, item ->
            when {
                index % 5 == 0 -> arrayOf(item.toString(), item)
                else -> arrayOf(item)
            }
        }.toTypedArray().flatten()

        SmartRecyclerAdapter
            .items(items)
            .map(String::class, SimpleHeaderViewHolder::class)
            .map(Integer::class, SimpleItemViewHolder::class)
            .add(StickyHeaderItemDecorationExtension(
                headerItemType = String::class
            ) { motionEvent, itemPosition ->
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    showToast("Header $itemPosition clicked")
                }
            })
            .into<SmartRecyclerAdapter>(binding.recyclerView)

        SimpleHeaderViewHolder.color = Color.DKGRAY
    }
}
