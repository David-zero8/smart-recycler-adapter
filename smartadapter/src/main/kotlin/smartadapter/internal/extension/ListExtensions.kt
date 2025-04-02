package smartadapter.internal.extension


/*
 * Created by Manne Öhlund on 2019-09-19.
 * Copyright (c) All rights reserved.
 */

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.ListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.full.allSupertypes

fun List<*>.isMutable() = this::class.allSupertypes.any { it.toString() == "kotlin.collections.MutableList<E>" }

fun <T> ListAdapter<T, *>.submitListWithLoading(
    lifecycleScope: LifecycleCoroutineScope,
    newList: List<T>,
    onLoadingStateChanged: (Boolean) -> Unit,
    onListUpdated: (() -> Unit)? = null
) {
    onLoadingStateChanged(true)
    lifecycleScope.launch(Dispatchers.Default) {
        submitList(newList) {
            lifecycleScope.launch(Dispatchers.Main) {
                onLoadingStateChanged(false)
                onListUpdated?.invoke()
            }
        }
    }
}

