package smartrecycleradapter.feature

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DiffUtil
import io.github.zero8.smartrecycleradapter.sample.R
import smartadapter.SmartRecyclerAdapter
import smartrecycleradapter.feature.simpleitem.SimpleItemViewHolder
import kotlin.random.Random.Default.nextInt

/*
 * Created by Manne Öhlund on 2019-08-23.
 * Copyright (c) All rights reserved.
 */

class DiffUtilActivity : BaseSampleActivity() {

    val maxItemCount = nextInt(20, 100)
    val items = (0..maxItemCount).toMutableList()

    private lateinit var smartRecyclerAdapter: SmartRecyclerAdapter

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
        supportActionBar?.title = "Diff Util Sample"

        smartRecyclerAdapter = SmartRecyclerAdapter
            .items(items)
            .setDiffCallback(predicate)
            .map(Integer::class, SimpleItemViewHolder::class)
            .into(binding.recyclerView)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scroll_top, menu)
        menuInflater.inflate(R.menu.sort, menu)
        menuInflater.inflate(R.menu.shuffle, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.scroll_top -> {
                binding.recyclerView.smoothScrollToPosition(0)
            }
            R.id.sort -> {
                smartRecyclerAdapter.submitList(items.toMutableList<Any>())
                binding.recyclerView.scrollToPosition(0)
            }
            R.id.shuffle -> {
                smartRecyclerAdapter.submitList(
                    items.shuffled()
                        .filter {
                            it % nextInt(1, maxItemCount/5) == 1
                        }
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
