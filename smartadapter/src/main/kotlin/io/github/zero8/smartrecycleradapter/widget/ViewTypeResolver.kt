package io.github.zero8.smartrecycleradapter.widget

/*
 * Created by Manne Öhlund on 02/04/17.
 * Copyright © 2017 All rights reserved.
 */

import io.github.zero8.smartrecycleradapter.Position
import io.github.zero8.smartrecycleradapter.SmartViewHolderType

/**
 * Type alias custom lambda listener for resolving of view type by source data.
 */
typealias ViewTypeResolver = (item: Any, position: Position) -> SmartViewHolderType?
