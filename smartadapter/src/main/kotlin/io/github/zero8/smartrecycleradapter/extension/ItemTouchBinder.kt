package smartrecycleradapter.extension

import androidx.recyclerview.widget.RecyclerView
import io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter

/**
 * ItemTouchBinder is the basic interface to implement in extension libraries
 * to bind with [SmartRecyclerAdapter] & [RecyclerView] on adapter creation.
 */
interface ItemTouchBinder<T> {

    /**
     * Builds and binds the drag and drop mechanism to target recycler view
     */
    fun bind(
        smartRecyclerAdapter: SmartRecyclerAdapter,
        recyclerView: RecyclerView
    ) : T
}
