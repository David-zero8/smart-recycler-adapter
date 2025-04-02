package smartrecycleradapter.feature

import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import io.github.zero8.smartrecycleradapter.sample.R
import io.github.zero8.smartrecycleradapter.sample.databinding.ActivityFilterItemBinding
import smartadapter.SmartRecyclerAdapter
import smartadapter.filter.FilterExtension
import smartadapter.get
import smartadapter.internal.extension.submitListWithLoading
import smartadapter.viewevent.listener.OnClickEventListener
import smartrecycleradapter.feature.simpleitem.SimpleItemViewHolder
import smartrecycleradapter.utils.showToast
import smartrecycleradapter.viewholder.SmallHeaderViewHolder
import kotlin.random.Random

/*
 * Created by Manne Öhlund on 2019-08-11.
 * Copyright (c) All rights reserved.
 */

class SimpleFilterActivity : BaseSampleActivity() {
    lateinit var bindingFilter: ActivityFilterItemBinding

    lateinit var smartAdapter: SmartRecyclerAdapter

    private val predicate = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when(oldItem) {
                is Int -> oldItem == newItem
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when(oldItem) {
                is Int -> oldItem == newItem
                else -> false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingFilter = ActivityFilterItemBinding.inflate(layoutInflater)
        setContentView(bindingFilter.root)

        setSupportActionBar(bindingFilter.toolbar)

        supportActionBar?.title = "Simple Filter"

        val items = (0..300).map { Random.nextInt(100, 10000) }.toMutableList()

        smartAdapter = SmartRecyclerAdapter.items(items)
            .setDiffCallback(predicate)
            .map(String::class, SmallHeaderViewHolder::class)
            .map(Int::class, SimpleFilterItemViewHolder::class)
            .add(OnClickEventListener {
                showToast("Nr ${it.adapter.getItem(it.position)} @ pos ${it.position + 1}")
            })
            .add(
                FilterExtension(
                    filterPredicate = { item, constraint ->
                        when (item) {
                            is Int -> item.toString().contains(constraint)
                            else -> true
                        }
                    }
                ) {
                    bindingFilter.toolbarProgressBar.visibility = if (it) View.VISIBLE else View.GONE
                }
            )
            .into(bindingFilter.recyclerView)

        setOriginalItems(smartAdapter.getItems())

        // Set search view filter
        bindingFilter.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return true
            }
        })
    }

    fun filter(query: String?) {
        val filterExtension: FilterExtension = smartAdapter.get()
        filterExtension.filter(lifecycleScope, query) {
            if (it.isSuccess) {
                // 호출 부분
                smartAdapter.submitListWithLoading(
                    lifecycleScope,
                    newList = it.getOrDefault(listOf()),
                    onLoadingStateChanged = { isLoading ->
                        bindingFilter.toolbarProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    },
                    onListUpdated = {

                    }
                )
            } else {
                bindingFilter.recyclerView.scrollToPosition(0)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.refresh, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.refresh -> refresh()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refresh() {
        val items = (0..300).map { Random.nextInt(100, 10000) }.toMutableList()
        smartAdapter.setItems(items) {
            setOriginalItems(smartAdapter.getItems())
        }
    }

    fun setOriginalItems(items: List<Any>) {
        val filterExtension: FilterExtension = smartAdapter.get()
        filterExtension.setOriginalItems(items)
    }
}

class SimpleFilterItemViewHolder(view: ViewGroup) : SimpleItemViewHolder(view) {

    init {
        (itemView as TextView).typeface = Typeface.MONOSPACE
    }

    override fun bind(item: Int) {
        (itemView as TextView).text = "$item"
    }
}
