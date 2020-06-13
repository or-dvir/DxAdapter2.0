package com.hotmail.or_dvir.dxlibraries.swipeable

import com.hotmail.or_dvir.dxdragandswipe.swipe.IDxItemSwipeable
import com.hotmail.or_dvir.dxlibraries.BaseItem
import kotlin.random.Random

class ItemSwipeable(text: String) : BaseItem(text), IDxItemSwipeable {
    val randomNum = Random.nextInt(100)
}
