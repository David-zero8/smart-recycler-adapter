package smartrecycleradapter.feature

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import smartadapter.Position
import smartadapter.SmartRecyclerAdapter
import smartadapter.viewevent.dragdrop.AutoDragAndDropBinder
import smartadapter.viewevent.listener.OnClickEventListener
import smartrecycleradapter.extension.GridAutoLayoutManager
import smartrecycleradapter.models.MovieData
import smartrecycleradapter.models.MovieModel
import smartrecycleradapter.utils.AssetsUtils
import smartrecycleradapter.utils.showToast
import smartrecycleradapter.viewholder.HeaderViewHolder
import smartrecycleradapter.viewholder.ThumbViewHolder

/*
 * Created by Manne Öhlund on 2019-08-11.
 * Copyright (c) All rights reserved.
 */

class GridActivity : BaseSampleActivity() {
    private lateinit var smartRecyclerAdapter: SmartRecyclerAdapter
    private val movieData: MovieData by lazy {
        AssetsUtils.loadStyleFromAssets<MovieData>(this, "main-movie-data.json")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Grid + Drag & Drop"

        val gridAutoLayoutManager = GridAutoLayoutManager(this, 100)
        gridAutoLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Position): Int {
                return if ((binding.recyclerView.adapter as SmartRecyclerAdapter).getItem(position) is String) gridAutoLayoutManager.spanCount else 1
            }
        }

        smartRecyclerAdapter = SmartRecyclerAdapter.empty()
            .setDiffCallback(object : DiffUtil.ItemCallback<Any>() {
                override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                    return when (oldItem) {
                        is String -> newItem is String && oldItem == newItem
                        is MovieModel -> newItem is MovieModel && oldItem.title == newItem.title && oldItem.category == newItem.category && oldItem.size == newItem.size
                        else -> false
                    }
                }

                override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                    return when (oldItem) {
                        is String -> newItem is String && oldItem == newItem
                        is MovieModel -> newItem is MovieModel && oldItem == newItem
                        else -> false
                    }
                }
            })
            .map(String::class, HeaderViewHolder::class)
            .setViewTypeResolver { item, position ->
                when (item) {
                    is MovieModel -> when (item.category) {
                        "coming-soon" -> ComingSoonThumbViewHolder::class
                        "action" -> ActionThumbViewHolder::class
                        "anim" -> AnimateThumbViewHolder::class
                        "sci-fi" -> SciFiThumbViewHolder::class
                        else -> null
                    }

                    else -> null
                }
            }
            .setLayoutManager(gridAutoLayoutManager)
            .add(OnClickEventListener(ThumbViewHolder::class) {
                showToast("Movie ${it.position}")
            })
            .add(
                AutoDragAndDropBinder(
                    longPressDragEnabled = true,
                    viewHolderTypes = listOf(
                        ComingSoonThumbViewHolder::class,
                        ActionThumbViewHolder::class,
                        AnimateThumbViewHolder::class,
                        SciFiThumbViewHolder::class
                    )
                ) {
                    supportActionBar?.subtitle =
                        "onItemMoved from ${it.viewHolder.bindingAdapterPosition} to ${it.targetViewHolder.bindingAdapterPosition}"
                }
            )
            .into<SmartRecyclerAdapter>(binding.recyclerView)

        // Set adapter data
        smartRecyclerAdapter.addItem("Coming soon", notifyDataSetChanged = false)
        smartRecyclerAdapter.addItems(movieData.categories.find { it.id == "coming-soon" }!!.items, notifyDataSetChanged = false)
        smartRecyclerAdapter.addItem("Action", notifyDataSetChanged = false)
        smartRecyclerAdapter.addItems(movieData.categories.find { it.id == "action" }!!.items, notifyDataSetChanged = false)
        smartRecyclerAdapter.addItem("Animated", notifyDataSetChanged = false)
        smartRecyclerAdapter.addItems(movieData.categories.find { it.id == "anim" }!!.items, notifyDataSetChanged = false)
        smartRecyclerAdapter.addItem("Sci-Fi", notifyDataSetChanged = false)
        smartRecyclerAdapter.addItems(movieData.categories.find { it.id == "sci-fi" }!!.items, notifyDataSetChanged = false)
        smartRecyclerAdapter.commitPendingItems()
    }

    class ComingSoonThumbViewHolder(parentView: ViewGroup) : ThumbViewHolder(parentView)
    class ActionThumbViewHolder(parentView: ViewGroup) : ThumbViewHolder(parentView)
    class AnimateThumbViewHolder(parentView: ViewGroup) : ThumbViewHolder(parentView)
    class SciFiThumbViewHolder(parentView: ViewGroup) : ThumbViewHolder(parentView)
}
