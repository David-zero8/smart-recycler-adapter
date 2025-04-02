package smartrecycleradapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import io.github.zero8.smartrecycleradapter.sample.R
import io.github.zero8.smartrecycleradapter.sample.databinding.SimpleRadiobuttonItemBinding
import smartadapter.viewholder.BindingSmartViewHolder
import smartadapter.viewholder.SmartViewHolder
import smartrecycleradapter.feature.CheckItem
import smartrecycleradapter.feature.simpleitem.SimpleItemViewHolder

/*
 * Created by Manne Öhlund on 2019-08-14.
 * Copyright (c) All rights reserved.
 */

class SimpleSelectableItemViewHolder(parentView: ViewGroup) : SimpleItemViewHolder(parentView)

class SimpleSelectableCheckBoxViewHolder(parentView: ViewGroup) :
    SmartViewHolder<Int>(parentView, R.layout.simple_checkbox_item) {

    private var textView: TextView = itemView.findViewById(R.id.textView)
    private var checkBox: CheckBox = itemView.findViewById(R.id.checkBox)

    override fun bind(item: Int) {
        textView.text = "Item $item"
    }
}

class SimpleSelectableSwitchViewHolder(parentView: ViewGroup) :
    SmartViewHolder<Int>(parentView, R.layout.simple_switch_item) {

    private var textView: TextView = itemView.findViewById(R.id.textView)
    private var switch: SwitchCompat = itemView.findViewById(R.id.switchButton)

    override fun bind(item: Int) {
        textView.text = "Item $item"
    }
}

//use dataBinding
class SimpleSelectableRadioButtonViewHolder(parentView: ViewGroup) :
    BindingSmartViewHolder<CheckItem, SimpleRadiobuttonItemBinding>(
        SimpleRadiobuttonItemBinding.inflate(LayoutInflater.from(parentView.context), parentView, false)
    ) {
    override fun bind(item: CheckItem) {
        binding.textView.text = "Item ${item.index}"
        binding.radioButton.isChecked = item.checked
        binding.radioButton.isSelected = item.checked
    }
}
