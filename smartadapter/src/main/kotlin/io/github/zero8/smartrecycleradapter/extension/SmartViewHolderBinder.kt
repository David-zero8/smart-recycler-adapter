package smartrecycleradapter.extension

import android.view.View
import androidx.annotation.IdRes
import io.github.zero8.smartrecycleradapter.R
import io.github.zero8.smartrecycleradapter.SmartViewHolderType
import io.github.zero8.smartrecycleradapter.ViewId
import io.github.zero8.smartrecycleradapter.viewholder.SmartViewHolder

interface SmartViewHolderBinder : SmartExtensionIdentifier {

    /**
     * Default implementation returns [SmartViewHolder] class which
     * [io.github.zero8.smartrecycleradapter.SmartRecyclerAdapter] will bind to all [SmartViewHolder] extensions.
     *
     * Can be overridden to a specific target [SmartViewHolder] extension.
     */
    val viewHolderType: SmartViewHolderType
        get() = SmartViewHolder::class

    /**
     * Default implementation returns [R.id.undefined] which will point to the root view of the view in the view holder.
     *
     * Can be overridden to target specific view ids.
     */
    val viewIds: IntArray
        get() = intArrayOf(R.id.undefined)
}

fun SmartViewHolderBinder.findView(
    @IdRes id: ViewId,
    smartViewHolder: SmartViewHolder<Any>
): View = when (id) {
    R.id.undefined -> smartViewHolder.itemView
    else -> smartViewHolder.itemView.findViewById<View>(id)
}.also {
    if (it == null) {
        val viewIdName = smartViewHolder.itemView.resources.getResourceName(id)
        throw RuntimeException("View not found by id '$viewIdName=$id' in ${smartViewHolder::class.java.simpleName}")
    }
}
