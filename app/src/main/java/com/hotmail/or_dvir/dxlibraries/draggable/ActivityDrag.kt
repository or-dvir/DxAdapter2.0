package com.hotmail.or_dvir.dxlibraries.draggable

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.recyclerview.widget.ItemTouchHelper
import com.hotmail.or_dvir.dxdragandswipe.DxItemTouchCallback
import com.hotmail.or_dvir.dxdragandswipe.DxItemTouchHelper
import com.hotmail.or_dvir.dxdragandswipe.drag.DxFeatureDrag
import com.hotmail.or_dvir.dxlibraries.FeatureActivity
import com.hotmail.or_dvir.dxlibraries.BaseAdapter
import kotlinx.android.synthetic.main.activity_base.*

class ActivityDrag : FeatureActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //comment/uncomment as desired
        setDragFeature(true, null)
//        setDragFeature(false, R.id.listItem_dragHandle)
//        setDragFeatureMixed()
    }

    @Suppress("SameParameterValue")
    private fun setDragFeature(dragOnLongClick: Boolean, @IdRes dragHandleId: Int?) {
        val adapter = AdapterDraggable(
            MutableList(100) { index -> ItemDraggable("item $index") }
        )

        setAdapter(adapter)

        val touchCallBack = DxItemTouchCallback(adapter).apply {
            dragFeature = DxFeatureDrag(
                onDragStart = { view, adapterPosition, item ->
                    Log.i("aaaaa", "drag start for ${item.text}")
                },
                onDragEnd = { view, adapterPosition, item ->
                    Log.i("aaaaa", "drag end for ${item.text}")
                },

                onItemMoved = { draggedView, draggedPosition, draggedItem,
                                targetView, targetPosition, targetItem ->
                    Log.i(
                        "aaaaa",
                        "replaced ${draggedItem.text}($draggedPosition) " +
                                "with ${targetItem.text}($targetPosition)"
                    )
                },
                dragDirections = ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                dragOnLongClick = dragOnLongClick
            )
        }

        DxItemTouchHelper(touchCallBack).apply {
            dragHandleId?.apply { setDragHandleId(this) }
            attachToRecyclerView(activityBase_rv)
        }
    }

    private fun setDragFeatureMixed() {
        val items = mutableListOf(
            ItemNonDraggable("non-draggable"),
            ItemDraggable("draggable")
        )
        val adapter = BaseAdapter(items)
        setAdapter(adapter)

        val touchCallBack = DxItemTouchCallback(adapter).apply {
            dragFeature = DxFeatureDrag(
                onDragStart = { view, adapterPosition, item ->
                    Log.i("aaaaa", "drag start for ${item.text}")
                },
                onDragEnd = { view, adapterPosition, item ->
                    Log.i("aaaaa", "drag end for ${item.text}")
                },

                onItemMoved = { draggedView, draggedPosition, draggedItem,
                                targetView, targetPosition, targetItem ->
                    Log.i(
                        "aaaaa",
                        "replaced ${draggedItem.text}($draggedPosition) " +
                                "with ${targetItem.text}($targetPosition)"
                    )
                },
                dragDirections = ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                dragOnLongClick = true
            )
        }

        DxItemTouchHelper(touchCallBack).attachToRecyclerView(activityBase_rv)
    }
}