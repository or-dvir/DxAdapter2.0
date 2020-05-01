package com.hotmail.or_dvir.dxdragandswipe

import androidx.recyclerview.widget.ItemTouchHelper

enum class DxDirection(internal val itemTouchHelperInt: Int) {
    UP(ItemTouchHelper.UP),
    DOWN(ItemTouchHelper.DOWN),
    UP_DOWN(ItemTouchHelper.UP or ItemTouchHelper.DOWN),
    LEFT(ItemTouchHelper.LEFT),
    RIGHT(ItemTouchHelper.RIGHT),
    LEFT_RIGHT(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
}