package com.hotmail.or_dvir.dxadapterv2.draggable

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.recyclerview.widget.ItemTouchHelper
import com.hotmail.or_dvir.dxadapterv2.BaseAdapter
import com.hotmail.or_dvir.dxadapterv2.BaseFeatureActivity
import com.hotmail.or_dvir.dxadapterv2.R
import com.hotmail.or_dvir.dxdragandswipe.DxItemTouchCallback
import com.hotmail.or_dvir.dxdragandswipe.DxItemTouchHelper
import com.hotmail.or_dvir.dxdragandswipe.drag.DxFeatureDrag
import kotlinx.android.synthetic.main.activity_base.*

class ActivityDrag : BaseFeatureActivity() {

    lateinit var touchHelper: DxItemTouchHelper<ItemDraggable>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //comment/uncomment as desired
//        setDragFeature(true, null)
        setDragFeature(false, R.id.listItem_dragHandle)
//        setDragFeatureMixed()
    }

    @Suppress("SameParameterValue")
    private fun setDragFeature(dragOnLongClick: Boolean, @IdRes dragHandleId: Int?) {
        val adapter = AdapterDraggable(
            MutableList(100) { index -> ItemDraggable("item $index") }
        )

        setAdapter(adapter)

        val myDragFeature = DxFeatureDrag<ItemDraggable>(
            onDragStart = { view, adapterPosition, item, holder ->
                dragHandleId?.apply {
                    //if we want to use a drag handle, we need to manually start the drag
                    touchHelper.startDrag(holder)
                }

                Log.i("aaaaa", "drag start for ${item.text}")
            },
            onDragEnd = { view, adapterPosition, item, holder ->
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

        adapter.addFeature(myDragFeature)


        val touchCallBack = DxItemTouchCallback(adapter).apply {
            dragFeature = myDragFeature
        }

        touchHelper = DxItemTouchHelper(touchCallBack).apply {
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
                onDragStart = { view, adapterPosition, item, holder ->
                    //note that if we drag via long-click, we do NOT need to manually start the drag
                    Log.i("aaaaa", "drag start for ${item.text}")
                },
                onDragEnd = { view, adapterPosition, item, holder ->
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

    override fun onDestroy() {
        //IMPORTANT NOTE!!!
        //touchHelper (which extends DxItemTouchHelper which extends ItemTouchHelper)
        //holds a reference to the the recycler view.
        //if that reference is not cleared, it could cause a memory leak
        touchHelper.attachToRecyclerView(null)
        super.onDestroy()
    }
}