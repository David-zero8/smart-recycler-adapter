package smartrecycleradapter.feature

/*
 * Created by Manne Öhlund on 2019-08-10.
 * Copyright (c) All rights reserved.
 */

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.recyclerview.widget.DiffUtil
import io.github.zero8.smartrecycleradapter.sample.R
import io.github.zero8.smartrecycleradapter.Position
import io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter
import io.github.zero8.smartrecycleradapter.SmartViewHolderType
import io.github.zero8.smartrecycleradapter.ViewId
import io.github.zero8.smartrecycleradapter.viewholder.SmartViewHolder
import smartrecycleradapter.viewevent.listener.OnMultiItemCheckListener
import smartrecycleradapter.viewevent.listener.OnMultiItemSelectListener
import smartrecycleradapter.viewevent.model.ViewEvent
import smartrecycleradapter.viewevent.viewmodel.ViewEventViewModel
import smartrecycleradapter.utils.showToast
import smartrecycleradapter.viewholder.SimpleSelectableRadioButtonViewHolder
import kotlin.reflect.KClass

class SingleSelectRadioButtonItemActivity : BaseSampleActivity() {

    class SingleItemCheckedViewModel : ViewEventViewModel<ViewEvent, OnSingleRadioButtonCheckListener>(
        OnSingleRadioButtonCheckListener(viewId = R.id.constraintLauoutRoot)
    )

    lateinit var smartRecyclerAdapter: SmartRecyclerAdapter
    private val singleItemCheckedViewModel: SingleItemCheckedViewModel by viewModels()
    private var deleteMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Single RadioButton Select"

        val items = (0..100).map { CheckItem(false, it) }.toMutableList()

        smartRecyclerAdapter = SmartRecyclerAdapter
            .items(items)
            .setDiffCallback(predicate)
            .map(CheckItem::class, SimpleSelectableRadioButtonViewHolder::class)
            .add(singleItemCheckedViewModel.observe(this) {
                handleCheckEvent(it)
            })
            .into(binding.recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.delete, menu)
        deleteMenuItem = menu?.findItem(R.id.delete)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.delete -> {
                singleItemCheckedViewModel.viewEventListener.removeSelections()
                item.isVisible = false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleCheckEvent(it: ViewEvent) {
        showToast("Item click ${(smartRecyclerAdapter.getItems()[it.position] as CheckItem).index}\n" +
                "${singleItemCheckedViewModel.viewEventListener.selectedItemsCount} of " +
                "${smartRecyclerAdapter.itemCount} selected items")

        val list = smartRecyclerAdapter.getItems().mapIndexed { index, item ->
            (item as CheckItem).copy(checked = singleItemCheckedViewModel.viewEventListener.isSelected(index), index = item.index)
        }.toMutableList()
        smartRecyclerAdapter.setItems(list)

        deleteMenuItem?.isVisible = singleItemCheckedViewModel.viewEventListener.selectedItemsCount > 0
    }

    val predicate = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when (oldItem) {
                is CheckItem -> true
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when (oldItem) {
                is CheckItem -> newItem is CheckItem && oldItem.index == newItem.index && oldItem.checked == newItem.checked
                else -> false
            }
        }
    }

}

data class CheckItem(var checked: Boolean, var index: Int)


open class OnSingleRadioButtonCheckListener(
    override val identifier: Any = OnMultiItemCheckListener::class,
    override val viewHolderType: SmartViewHolderType = SmartViewHolder::class,
    selectableItemType: KClass<*> = Any::class,
    @IdRes viewId: ViewId = io.github.zero8.smartrecycleradapter.R.id.undefined
) : OnMultiItemSelectListener(
    enableOnLongClick = false,
    selectableItemType = selectableItemType,
    viewId = viewId
) {

    override fun setSelected(
        adapter: SmartRecyclerAdapter,
        viewHolder: SmartViewHolder<Any>
    ) {

    }

    /**
     * Adds the position to the data set and [.disable]s any old positions.
     * @param position the adapter position
     */
    override fun enable(position: Position) {
        for (oldPositions in selectedItems) {
            disable(oldPositions)
        }
        clear()
        super.enable(position)
    }

    /**
     * Removes the position from the data set and calls [SmartRecyclerAdapter.smartNotifyItemChanged].
     * @param position the adapter position
     */
    override fun disable(position: Position) {
        super.disable(position)
    }
}

