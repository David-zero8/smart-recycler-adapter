package io.github.zero8.smartrecycleradapter

/*
 * Created by Manne Öhlund on 2019-06-25.
 * Copyright © 2019 All rights reserved.
 */

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.HashMap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.zero8.smartrecycleradapter.internal.mapper.ViewHolderMapper
import io.github.zero8.smartrecycleradapter.listener.OnAttachedToRecyclerViewListener
import io.github.zero8.smartrecycleradapter.listener.OnBindViewHolderListener
import io.github.zero8.smartrecycleradapter.listener.OnCreateViewHolderListener
import io.github.zero8.smartrecycleradapter.listener.OnDetachedFromRecyclerViewListener
import io.github.zero8.smartrecycleradapter.listener.OnViewAttachedToWindowListener
import io.github.zero8.smartrecycleradapter.listener.OnViewDetachedFromWindowListener
import io.github.zero8.smartrecycleradapter.listener.OnViewRecycledListener
import io.github.zero8.smartrecycleradapter.viewholder.SmartAdapterHolder
import io.github.zero8.smartrecycleradapter.viewholder.SmartViewHolder
import io.github.zero8.smartrecycleradapter.widget.ViewTypeResolver
import smartrecycleradapter.extension.SmartExtensionIdentifier
import smartrecycleradapter.extension.SmartRecyclerAdapterBinder
import smartrecycleradapter.extension.SmartViewHolderBinder
import kotlin.reflect.KClass

/**
 * Type alias for SmartViewHolder, Kotlin class type.
 */
typealias SmartViewHolderType = KClass<out SmartViewHolder<*>>

/**
 * Type alias for adapter data items, Kotlin class type.
 */
typealias ItemType = KClass<*>

/**
 * Type alias for view id, ex: R.id.my_id.
 */
typealias ViewId = Int

/**
 * Type alias for resolved view type in [RecyclerView.Adapter].
 */
typealias ViewType = Int

/**
 * Type alias for position in [RecyclerView.Adapter].
 */
typealias Position = Int

/**
 * SmartRecyclerAdapter is the core implementation of the library.
 * It handles all the implementations of the [ISmartRecyclerAdapter] functionality.
 */
@Suppress("UNCHECKED_CAST")
open class SmartRecyclerAdapter(diffCallback: DiffUtil.ItemCallback<Any> = DEFAULT_DIFF_CALLBACK,items: MutableList<Any>)
    : ListAdapter<Any, SmartViewHolder<Any>>(diffCallback), ISmartRecyclerAdapter {

    companion object {
        val DEFAULT_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                return when (oldItem) {
                    is Int -> newItem is Int && oldItem == newItem
                    is Long -> newItem is Long && oldItem == newItem
                    is Float -> newItem is Float && oldItem == newItem
                    is Double -> newItem is Double && oldItem == newItem
                    is String -> newItem is String && oldItem == newItem
                    is Char -> newItem is Char && oldItem == newItem
                    is Boolean -> newItem is Boolean && oldItem == newItem
                    is Integer -> newItem is Integer && oldItem == newItem
                    else -> false
                }
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return when (oldItem) {
                    is Int -> newItem is Int && oldItem == newItem
                    is Long -> newItem is Long && oldItem == newItem
                    is Float -> newItem is Float && oldItem == newItem
                    is Double -> newItem is Double && oldItem == newItem
                    is String -> newItem is String && oldItem == newItem
                    is Char -> newItem is Char && oldItem == newItem
                    is Boolean -> newItem is Boolean && oldItem == newItem
                    is Integer -> newItem is Integer && oldItem == newItem
                    else -> false
                }
            }
        }

        fun items(items: List<Any>): SmartAdapterBuilder = SmartAdapterBuilder().setItems(items)
        fun empty(): SmartAdapterBuilder = SmartAdapterBuilder()
    }

    // 기존 SmartRecyclerAdapter와 동일하게 유지
    override var smartItemCount: Int = 0
    override var viewHolderMapper: ViewHolderMapper = ViewHolderMapper()
    override var viewTypeResolver: ViewTypeResolver? = null
    final override val smartExtensions = mutableMapOf<Any, SmartExtensionIdentifier>()
    private var pendingList: MutableList<Any>? = null

    init {
        submitList(items)
        updateItemCount()
    }


    /**
     * ListAdapter 내부 목록(currentList)에 대한 사이즈 반환
     */
    override fun getItemCount(): Int {
        // ListAdapter가 관리하는 현재 목록
        smartItemCount = currentList.size
        return smartItemCount
    }

    override fun <T : Any> getItemCount(type: KClass<out T>): Int {
        // 예: currentList 중 해당 클래스 타입과 일치하는 아이템만 카운트
        return currentList.count { it::class == type }
    }

    /**
     * onCreateViewHolder 구현
     * (기존 SmartRecyclerAdapter에서 사용하던 확장(extension) 로직을 그대로 유지)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartViewHolder<Any> {
        val smartViewHolder = viewHolderMapper.createViewHolder<SmartViewHolder<Any>>(parent, viewType)
        if (smartViewHolder is SmartAdapterHolder && smartViewHolder.smartRecyclerAdapter == null) {
            smartViewHolder.smartRecyclerAdapter = this
        }
        smartExtensions.values.forEach { extension ->
            if (extension is SmartViewHolderBinder
                && (extension.viewHolderType == SmartViewHolder::class
                        || extension.viewHolderType.isInstance(smartViewHolder))
                && extension is OnCreateViewHolderListener
            ) {
                extension.onCreateViewHolder(this, smartViewHolder)
            }
        }
        return smartViewHolder
    }

    /**
     * onBindViewHolder 구현
     */
    override fun onBindViewHolder(holder: SmartViewHolder<Any>, position: Int) {
        val item = getItem(position) // ListAdapter가 제공하는 메서드
        holder.bind(item)
        smartExtensions.values.forEach { extension ->
            if (extension is SmartViewHolderBinder
                && (extension.viewHolderType == SmartViewHolder::class
                        || extension.viewHolderType.isInstance(holder))
                && extension is OnBindViewHolderListener
            ) {
                extension.onBindViewHolder(this, holder)
            }
        }
    }

    /**
     * getItemViewType 구현
     */
    override fun getItemViewType(position: Int): Int {
        return viewHolderMapper.getItemViewType(viewTypeResolver, getItem(position), position)
    }

    /**
     * viewHolder 재활용, attach/detach 등
     * 기존 SmartRecyclerAdapter와 동일하게 유지
     */
    override fun onViewRecycled(holder: SmartViewHolder<Any>) {
        super.onViewRecycled(holder)
        holder.unbind()
        smartExtensions.values.forEach { extension ->
            if (extension is SmartViewHolderBinder
                && (extension.viewHolderType == SmartViewHolder::class
                        || extension.viewHolderType.isInstance(holder))
                && extension is OnViewRecycledListener
            ) {
                extension.onViewRecycled(this, holder)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        smartExtensions.values.forEach { extension ->
            (extension as? OnAttachedToRecyclerViewListener)?.onAttachedToRecyclerView(recyclerView)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        smartExtensions.values.forEach { extension ->
            (extension as? OnDetachedFromRecyclerViewListener)?.onDetachedFromRecyclerView(recyclerView)
        }
    }

    override fun onViewAttachedToWindow(holder: SmartViewHolder<Any>) {
        super.onViewAttachedToWindow(holder)
        (holder as? OnViewAttachedToWindowListener)?.onViewAttachedToWindow(holder)
        smartExtensions.values.forEach { extension ->
            (extension as? OnViewAttachedToWindowListener)?.onViewAttachedToWindow(holder)
        }
    }

    override fun onViewDetachedFromWindow(holder: SmartViewHolder<Any>) {
        super.onViewDetachedFromWindow(holder)
        (holder as? OnViewDetachedFromWindowListener)?.onViewDetachedFromWindow(holder)
        smartExtensions.values.forEach { extension ->
            (extension as? OnViewDetachedFromWindowListener)?.onViewDetachedFromWindow(holder)
        }
    }

    /**
     * ISmartRecyclerAdapter의 주요 메서드 구현
     * => 내부적으로 ListAdapter의 currentList를 복사하여 조작 후 submitList() 사용
     */
    override fun getItems(): MutableList<Any> {
        return currentList.toMutableList()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getItems(type: KClass<out T>): ArrayList<T> {
        return currentList.filter { it::class == type } as ArrayList<T>
    }

    override fun getItem(index: Int): Any {
        return super.getItem(index)
    }

    override fun <T : Any> getItemCast(index: Int): T {
        return getItem(index) as T
    }

    override fun setItems(items: MutableList<*>, commitCallback : () -> Unit) {
        // 완전히 새로운 목록을 전달
        submitList(items as List<Any>) {
            commitCallback.invoke()
        }
        clearPendingItems()
    }

    override fun setItems(items: MutableList<*>) {
        setItems(items, {})
    }

    override fun setItems(items: MutableList<*>, notifyDataSetChanged: Boolean, commitCallback : ()-> Unit) {
        // ListAdapter에서 notifyDataSetChanged 파라미터는 의미가 없지만
        // 호환성을 위해 동일한 시그니처 유지
        submitList(items as List<Any>) {
            commitCallback.invoke()
        }
        clearPendingItems()
    }

    override fun setItems(items: MutableList<*>, notifyDataSetChanged: Boolean) {
        setItems(items, notifyDataSetChanged, {})
    }

    override fun addItem(item: Any) {
        addItem(item, true)
    }

    override fun addItem(item: Any, notifyDataSetChanged: Boolean) {
        getOrCreatePendingList().add(item)
        if (notifyDataSetChanged) {
            commitPendingItems()
        }
    }

    override fun addItem(index: Int, item: Any) {
        addItem(index, item, true)
    }

    override fun addItem(index: Int, item: Any, notifyDataSetChanged: Boolean) {
        getOrCreatePendingList().add(index, item)
        if (notifyDataSetChanged) {
            commitPendingItems()
        }
    }

    override fun addItems(items: List<Any>) {
        addItems(items, true)
    }

    override fun addItems(items: List<Any>, notifyDataSetChanged: Boolean) {
        getOrCreatePendingList().addAll(items)
        if (notifyDataSetChanged) {
            commitPendingItems()
        }
    }

    override fun addItems(index: Int, items: List<Any>) {
        addItems(index, items, true)
    }

    override fun addItems(index: Int, items: List<Any>, notifyDataSetChanged: Boolean) {
        getOrCreatePendingList().addAll(index, items)
        if (notifyDataSetChanged) {
            commitPendingItems()
        }
    }

    // 현재리스트를 기반으로 pendingList가 없으면 생성하거나 가져오는 함수
    private fun getOrCreatePendingList(): MutableList<Any> {
        if (pendingList == null) {
            pendingList = currentList.toMutableList()
        }
        return pendingList!!
    }

    // 변경사항을 실제 화면에 적용
    fun commitPendingItems(commitCallback : ()-> Unit = {}) {
        pendingList?.let {
            submitList(it) {
                commitCallback.invoke()
            }
            clearPendingItems()
        }
    }

    // 팬딩리스트 클리어 (취소 시)
    fun clearPendingItems() {
        pendingList = null
    }

    fun swapItems(fromPosition: Int, toPosition: Int, notifyImmediately: Boolean = false) {
        val list = getOrCreatePendingList()

        if (fromPosition !in list.indices || toPosition !in list.indices) return

        val item = list.removeAt(fromPosition)
        list.add(toPosition, item)

        if (notifyImmediately) {
            commitPendingItems()
        }
    }

    override fun removeItem(index: Int): Boolean {
        return removeItem(index, true)
    }

    override fun removeItem(index: Int, notifyDataSetChanged: Boolean): Boolean {
        val newList = currentList.toMutableList()
        if (newList.isNotEmpty()) {
            newList.removeAt(index)
            submitList(newList)
            clearPendingItems()
            return true
        }
        return false
    }

    override fun removeItem(index: Int, commitCallback : (Boolean)-> Unit) {
        return removeItem(index = index, notifyDataSetChanged = true, commitCallback = commitCallback)
    }

    override fun removeItem(index: Int, notifyDataSetChanged: Boolean, commitCallback : (Boolean)-> Unit) {
        val newList = getOrCreatePendingList()
        if (newList.isNotEmpty()) {
            newList.removeAt(index)
            if (notifyDataSetChanged) {
                submitList(newList) {
                    commitCallback(true)
                }
                clearPendingItems()
            }

        } else {
            commitCallback(false)
        }
    }



    override fun replaceItem(index: Int, item: Any) {
        replaceItem(index, item, true)
    }

    override fun replaceItem(index: Int, item: Any, notifyDataSetChanged: Boolean) {
        val newList = getOrCreatePendingList()
        if (index in newList.indices) {
            newList[index] = item
            if (notifyDataSetChanged) {
                commitPendingItems()
            }
        }
    }

    override fun clear() {
        submitList(emptyList())
        clearPendingItems()
    }

    override fun updateItemCount() {
        smartItemCount = currentList.size
    }

    override fun map(itemType: ItemType, viewHolderType: SmartViewHolderType) {
        viewHolderMapper.addMapping(itemType, viewHolderType)
    }

    internal fun setDataTypeViewHolderMapper(dataTypeViewHolderMapper: HashMap<String, SmartViewHolderType>) {
        viewHolderMapper.setDataTypeViewHolderMapper(dataTypeViewHolderMapper)
    }

    override fun add(extension: SmartExtensionIdentifier) {
        (extension as? SmartRecyclerAdapterBinder)?.bind(this)
        if (extension.identifier != extension::class && !smartExtensions.containsKey(extension.identifier)) {
            smartExtensions[extension.identifier] = extension
        } else if (smartExtensions.containsKey(extension.identifier)) {
            Log.e("SmartAdapterBuilder", "SmartAdapterBuilder already contains the key '${extension.identifier}', please consider override the identifier to be able to fetch the extension easily")
            smartExtensions[extension.hashCode()] = extension
        } else {
            smartExtensions[extension.identifier] = extension
        }
    }
}

/**
 * Helper method to resolve target [SmartRecyclerAdapterBinder]
 */
inline fun <reified T : SmartExtensionIdentifier> SmartRecyclerAdapter.get(identifier: Any? = null): T {
    identifier?.let {
        return smartExtensions[it] as T
    }
    return smartExtensions[T::class] as T
}