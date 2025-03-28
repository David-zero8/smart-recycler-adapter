package smartadapter.viewholder

/*
 * Created by Manne Öhlund on 2019-06-10.
 * Copyright (c) All rights reserved.
 */

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

import androidx.recyclerview.widget.RecyclerView
import smartadapter.SmartRecyclerAdapter

/**
 * Extension of [RecyclerView.ViewHolder] containing data item binding method.
 * @param <T> Data item type
 */
abstract class SmartViewHolder<T : Any>(view: View) : RecyclerView.ViewHolder(view), BindableViewHolder<T> {
    constructor(parentView: ViewGroup, @LayoutRes layout: Int) : this(
        LayoutInflater.from(parentView.context).inflate(layout, parentView, false)
    )
}

abstract class BindingSmartViewHolderr<T : Any, T2: ViewDataBinding> : SmartViewHolder<T>,
    SmartAdapterHolder {
    override var smartRecyclerAdapter: SmartRecyclerAdapter? = null
    var binding: T2

    constructor(parent: T2) : super(parent.root){
        binding = parent
    }
}
