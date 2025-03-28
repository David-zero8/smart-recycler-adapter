package smartrecycleradapter.feature

/*
 * Created by Manne Öhlund on 2020-09-23.
 * Copyright (c) All rights reserved.
 */

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import io.github.zero8.smartrecycleradapter.sample.R
import smartadapter.SmartRecyclerAdapter
import smartadapter.viewevent.listener.OnMultiItemSelectListener
import smartadapter.viewevent.model.ViewEvent
import smartadapter.viewevent.viewholder.OnItemSelectedEventListener
import smartadapter.viewevent.viewmodel.ViewEventViewModel
import smartadapter.viewholder.SmartViewHolder

class MultipleExpandableItemActivity : BaseSampleActivity() {

    class MultiItemSelectViewModel : ViewEventViewModel<ViewEvent, OnMultiItemSelectListener>(
        OnMultiItemSelectListener(
            enableOnLongClick = false,
            viewHolderType = SimpleExpandableItemViewHolder::class,
            selectableItemType = Integer::class,
            viewId = R.id.itemTitle
        )
    )

    private val multiItemSelectViewModel: MultiItemSelectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Multi Expandable Item"

        val items = (0..100).toMutableList()

        SmartRecyclerAdapter
            .items(items)
            .map(Integer::class, SimpleExpandableItemViewHolder::class)
            .add(multiItemSelectViewModel.observe(this) {
                supportActionBar?.subtitle =
                    "${multiItemSelectViewModel.viewEventListener.selectedItemsCount} / ${items.size} expanded"
            })
            .into<SmartRecyclerAdapter>(binding.recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        with(menuInflater) {
            inflate(R.menu.expand, menu)
            inflate(R.menu.collapse, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.expand_all -> multiItemSelectViewModel.viewEventListener.enableAll()
            R.id.collapse_all -> multiItemSelectViewModel.viewEventListener.disableAll()
        }
        return super.onOptionsItemSelected(item)
    }
}

class SimpleExpandableItemViewHolder(parentView: ViewGroup) :
    SmartViewHolder<Int>(parentView, R.layout.simple_expandable_item),
    OnItemSelectedEventListener {

    private val title: TextView = itemView.findViewById(R.id.itemTitle)
    private val subItem: LinearLayout = itemView.findViewById(R.id.subItemContainer)
    private val subItemTitle: TextView = itemView.findViewById(R.id.subItemTitle)

    override fun bind(item: Int) {
        title.text = "Item $item"
        subItemTitle.text = "Sub item of '$item'"
    }

    override fun onItemSelect(event: ViewEvent.OnItemSelected) {
        when (event.isSelected) {
            true -> {
                title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_less_black_24dp, 0)
                subItem.visibility = View.VISIBLE
            }
            false -> {
                title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0)
                subItem.visibility = View.GONE
            }
        }
    }
}
