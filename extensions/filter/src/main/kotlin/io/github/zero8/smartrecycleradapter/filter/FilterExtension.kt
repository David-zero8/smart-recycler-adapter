package io.github.zero8.smartrecycleradapter.filter

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter
import smartrecycleradapter.extension.SmartRecyclerAdapterBinder
import kotlin.reflect.KClass

class FilterExtension(
    override val identifier: Any = FilterExtension::class,
    val filterPredicate: (item: Any, query: CharSequence) -> Boolean,
    val targetFilterTypes: List<KClass<*>> = listOf(),
    val fastReset: Boolean = false,
    val loadingStateListener: (isLoading: Boolean) -> Unit = {}
) : SmartRecyclerAdapterBinder {

    private lateinit var smartRecyclerAdapter: SmartRecyclerAdapter
    private var originalItems: MutableList<Any> = mutableListOf()
    private var filterJob: Job? = null

    fun filter(
        lifecycleScope: LifecycleCoroutineScope,
        constraint: CharSequence?,
        result: ((Result<List<Any>>) -> Unit)? = {}
    ) {
        cancelFilterJob()

        if (constraint == null) {
            return
        }

        if (fastReset && (constraint.isBlank())) {
            smartRecyclerAdapter.setItems(originalItems)
            result?.invoke(Result.failure(Exception("Empty query, set initial list and returning!")))
            return
        }

        loadingStateListener.invoke(true)

        filterJob = lifecycleScope.launch(Dispatchers.IO) {
            val filterItems = originalItems.filter {
                if (targetFilterTypes.isEmpty() || it::class in targetFilterTypes) {
                    filterPredicate.invoke(it, constraint)
                } else {
                    true
                }
            }
            withContext(Dispatchers.Main) {
                loadingStateListener.invoke(false)
                if (filterJob?.isCancelled == false) {
                    result?.invoke(Result.success(filterItems))
                }
            }
        }
    }

    fun cancelFilterJob() {
        filterJob?.cancel()
    }

    override fun bind(smartRecyclerAdapter: SmartRecyclerAdapter) {
        this.smartRecyclerAdapter = smartRecyclerAdapter
        originalItems = smartRecyclerAdapter.getItems()
    }

    fun setOriginalItems(newOriginalItems: List<Any>) {
        originalItems = newOriginalItems.toMutableList()
    }
}