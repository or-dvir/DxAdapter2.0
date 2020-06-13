package com.hotmail.or_dvir.dxadapterv2.swipeable

import com.hotmail.or_dvir.dxdragandswipe.swipe.IDxItemSwipeable
import com.hotmail.or_dvir.dxadapterv2.BaseItem
import kotlin.random.Random

class ItemSwipeable(text: String) : BaseItem(text), IDxItemSwipeable {
    val randomNum = Random.nextInt(100)
}
