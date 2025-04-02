package smartrecycleradapter.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/*
 * Created by Manne Öhlund on 2019-12-12.
 * Copyright (c) All rights reserved.
 */

fun CoroutineScope.runDelayed(delayMillis: Long = 800, action: () -> Unit) = launch {
    delay(delayMillis)
    action()
}