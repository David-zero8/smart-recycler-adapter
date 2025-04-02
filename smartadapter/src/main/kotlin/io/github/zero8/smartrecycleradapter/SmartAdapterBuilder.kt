package io.github.zero8.smartrecycleradapter

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.zero8.smartrecycleradapter.internal.extension.isMutable
import io.github.zero8.smartrecycleradapter.internal.extension.name
import io.github.zero8.smartrecycleradapter.viewholder.SmartAdapterHolder
import io.github.zero8.smartrecycleradapter.widget.ViewTypeResolver
import smartrecycleradapter.extension.ItemTouchBinder
import smartrecycleradapter.extension.SmartExtensionIdentifier
import kotlin.reflect.KClass

/**
 * Builder for [SmartRecyclerAdapter].
 */
open class SmartAdapterBuilder {
    internal var items: MutableList<Any> = mutableListOf()
    internal var layoutManager: RecyclerView.LayoutManager? = null
    internal var viewTypeResolver: ViewTypeResolver? = null
    internal val viewHolderMapper = HashMap<String, SmartViewHolderType>()
    internal val smartExtensions = mutableMapOf<Any, SmartExtensionIdentifier>()
    internal var diffCallback: DiffUtil.ItemCallback<Any> = SmartRecyclerAdapter.Companion.DEFAULT_DIFF_CALLBACK


    internal open fun getSmartRecyclerAdapter() = SmartRecyclerAdapter(diffCallback, items.let {
        (if (it.isMutable()) it else it.toMutableList())
    })

    fun map(itemType: KClass<*>, viewHolderType: SmartViewHolderType): SmartAdapterBuilder {
        viewHolderMapper[itemType.name] = viewHolderType
        return this
    }

    fun setItems(items: List<Any>): SmartAdapterBuilder {
        this.items = (if (items.isMutable()) items else items.toMutableList()) as MutableList<Any>
        return this
    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager): SmartAdapterBuilder {
        this.layoutManager = layoutManager
        return this
    }

    private fun getLayoutManager(context: Context): RecyclerView.LayoutManager {
        if (layoutManager == null) {
            layoutManager = LinearLayoutManager(context).apply {
                isItemPrefetchEnabled = true
                initialPrefetchItemCount = 10
            }
        }
        return layoutManager!!
    }

    fun setViewTypeResolver(viewTypeResolver: ViewTypeResolver): SmartAdapterBuilder {
        this.viewTypeResolver = viewTypeResolver
        return this
    }

    fun setDiffCallback(diffCallback: DiffUtil.ItemCallback<Any>): SmartAdapterBuilder {
        this.diffCallback = diffCallback
        return this
    }

    /**
     * Adds [smartrecycleradapter.extension.SmartRecyclerAdapterBinder] to the adapter with [SmartExtensionIdentifier.identifier] as key.
     *
     * @param extension extension
     * @return SmartAdapterBuilder
     */
    fun add(extension: SmartExtensionIdentifier): SmartAdapterBuilder {
        if (extension.identifier != extension::class
            && !smartExtensions.containsKey(extension.identifier)) {
            smartExtensions[extension.identifier] = extension
        } else if (smartExtensions.containsKey(extension.identifier)) {
            Log.e("SmartAdapterBuilder", "SmartAdapterBuilder already contains the key '${extension.identifier}', please consider override the identifier to be able to fetch the extension easily")
            smartExtensions[extension.hashCode()] = extension
        } else {
            smartExtensions[extension.identifier] = extension
        }
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : SmartRecyclerAdapter> into(recyclerView: RecyclerView): T {
        val smartRecyclerAdapter = getSmartRecyclerAdapter()
        smartRecyclerAdapter.setDataTypeViewHolderMapper(viewHolderMapper)
        smartRecyclerAdapter.viewTypeResolver = viewTypeResolver
        recyclerView.layoutManager = getLayoutManager(recyclerView.context)
        smartExtensions.values.forEach {
            smartRecyclerAdapter.add(it)
            (it as? SmartAdapterHolder)?.smartRecyclerAdapter = smartRecyclerAdapter
            (it as? ItemTouchBinder<*>)?.bind(smartRecyclerAdapter, recyclerView)
        }
        recyclerView.adapter = smartRecyclerAdapter
        return smartRecyclerAdapter as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : SmartRecyclerAdapter> create(): T {
        val smartRecyclerAdapter = getSmartRecyclerAdapter()
        smartRecyclerAdapter.setDataTypeViewHolderMapper(viewHolderMapper)
        smartRecyclerAdapter.viewTypeResolver = viewTypeResolver
        smartExtensions.values.forEach {
            smartRecyclerAdapter.add(it)
            (it as? SmartAdapterHolder)?.smartRecyclerAdapter = smartRecyclerAdapter
        }
        return smartRecyclerAdapter as T
    }
}