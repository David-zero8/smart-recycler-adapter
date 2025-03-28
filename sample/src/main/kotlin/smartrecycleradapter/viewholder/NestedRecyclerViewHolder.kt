package smartrecycleradapter.viewholder

/*
 * Created by Manne Öhlund on 2019-06-25.
 * Copyright (c) All rights reserved.
 */

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.zero8.smartrecycleradapter.sample.databinding.NestedRecyclerViewBinding
import smartadapter.nestedadapter.SmartNestedRecyclerViewHolder
import smartadapter.viewholder.BindingSmartViewHolderr
import smartrecycleradapter.models.MovieCategory

open class NestedRecyclerViewHolder(parentView: ViewGroup) :
    BindingSmartViewHolderr<MovieCategory, NestedRecyclerViewBinding>(
        NestedRecyclerViewBinding.inflate(LayoutInflater.from(parentView.context), parentView, false)
    ), SmartNestedRecyclerViewHolder {

    override val recyclerView: RecyclerView = binding.nestedRecyclerView

    override fun bind(item: MovieCategory) {
        binding.title.text = item.title
    }
}

class ComingSoonMoviesViewHolder(parentView: ViewGroup) : NestedRecyclerViewHolder(parentView)

class MyWatchListViewHolder(parentView: ViewGroup) : NestedRecyclerViewHolder(parentView)

class ActionMoviesViewHolder(parentView: ViewGroup) : NestedRecyclerViewHolder(parentView)

class AdventureMoviesViewHolder(parentView: ViewGroup) : NestedRecyclerViewHolder(parentView)

class AnimatedMoviesViewHolder(parentView: ViewGroup) : NestedRecyclerViewHolder(parentView)

class SciFiMoviesViewHolder(parentView: ViewGroup) : NestedRecyclerViewHolder(parentView)

class RecentlyPlayedMoviesViewHolder(parentView: ViewGroup) : NestedRecyclerViewHolder(parentView) {

    init {
        binding.more.visibility = GONE
    }
}
