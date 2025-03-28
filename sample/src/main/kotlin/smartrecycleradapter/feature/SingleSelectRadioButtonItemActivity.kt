package smartrecycleradapter.feature

/*
 * Created by Manne Öhlund on 2019-08-10.
 * Copyright (c) All rights reserved.
 */

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import io.github.zero8.smartrecycleradapter.sample.R
import smartadapter.SmartRecyclerAdapter
import smartadapter.viewevent.listener.OnClickEventListener
import smartadapter.viewevent.listener.OnSingleItemCheckListener
import smartadapter.viewevent.model.ViewEvent
import smartadapter.viewevent.viewmodel.ViewEventViewModel
import smartrecycleradapter.utils.showToast
import smartrecycleradapter.viewholder.SimpleSelectableRadioButtonViewHolder

class SingleSelectRadioButtonItemActivity : BaseSampleActivity() {

    class SingleItemCheckedViewModel : ViewEventViewModel<ViewEvent, OnSingleItemCheckListener>(
        OnSingleItemCheckListener(viewId = R.id.radioButton)
    )

    lateinit var smartRecyclerAdapter: SmartRecyclerAdapter
    private val singleItemCheckedViewModel: SingleItemCheckedViewModel by viewModels()
    private var deleteMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Single RadioButton Select"

        val items = (0..100).toMutableList()

        smartRecyclerAdapter = SmartRecyclerAdapter
            .items(items)
            .map(Integer::class, SimpleSelectableRadioButtonViewHolder::class)
            .add(singleItemCheckedViewModel.observe(this) {
                handleCheckEvent(it)
            })
            .add(OnClickEventListener {
                showToast("onClick ${it.position}")
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
        showToast("Item click ${it.position}\n" +
                "${singleItemCheckedViewModel.viewEventListener.selectedItemsCount} of " +
                "${smartRecyclerAdapter.itemCount} selected items")

        deleteMenuItem?.isVisible = singleItemCheckedViewModel.viewEventListener.selectedItemsCount > 0
    }
}
