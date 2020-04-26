package com.hotmail.or_dvir.dxlibraries

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxdragandswipe.DxFeatureDrag
import com.hotmail.or_dvir.dxdragandswipe.DxItemTouchCallback
import com.hotmail.or_dvir.dxdragandswipe.DxItemTouchHelper
import com.hotmail.or_dvir.dxlibraries.clickable.AdapterClickable
import com.hotmail.or_dvir.dxlibraries.clickable.ItemClickable
import com.hotmail.or_dvir.dxlibraries.draggable.AdapterDraggable
import com.hotmail.or_dvir.dxlibraries.draggable.ItemDraggable
import com.hotmail.or_dvir.dxrecyclerview.DxScrollListener
import com.hotmail.or_dvir.dxrecyclerview.DxVisibilityListener
import com.hotmail.or_dvir.featureclicklisteners.DxFeatureClickListeners
import kotlinx.android.synthetic.main.activity_main.*

class ActivityMain : AppCompatActivity() {

    //todo
    // export each module as its own library!!!
    // remove unnecessary dependencies FOR EACH MODULE!!!!
    //      pay SPECIAL ATTENTION to modules that depends on one-another (e.g. DxRecyclerView and DxCountingIdlingResource)
    // go over all to-do's from ALL the modules
    // define project wide constants for versions of:
    //      EVERYTHING IN ALL BUILD.GRADLE FILES!!!!!
    // add documentation about which dependencies EACH MODULE forwards to the user
    // make sure to have proper documentation for all classes in ALL MODULES
    // add readme file to all modules
    // delete all example test files from all modules

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setLayoutManagerVertical()

        activityMain_btn.setOnClickListener {
//            mAdapter.setItems(List(5) { index -> ItemDraggable("item $index") })
        }

//        setScrollListeners()
//        setVisibilityListeners()
//        setClickListeners()
        setDragListeners(true, null)
//        setDragListeners(false, some id)
    }

    private fun setDragListeners(dragOnLongClick: Boolean, @IdRes dragHandleId: Int?) {
        val adapter = AdapterDraggable(List(100) { index -> ItemDraggable("item $index") })
        activityMain_rv.adapter = adapter

        //todo
        // set item touch callback
        // set item touch helper (with above callback)
        // attach to recycler view
        // NO NEED to add feature - its done inside DxItemTouchCallback

        val touchCallBack = DxItemTouchCallback(adapter).apply {
            fun getItemAtPosition(position: Int) =
                adapter.getDxAdapterItem<ItemDraggable>(position)

            dragFeature = DxFeatureDrag(
                onDragStart = { view, adapterPosition ->
                    val item = getItemAtPosition(adapterPosition)
                    Log.i("aaaaa", "drag start for ${item.text}")
                },
                onDragEnd = { view, adapterPosition ->
                    val item = getItemAtPosition(adapterPosition)
                    Log.i("aaaaa", "drag end for ${item.text}")
                },
                onItemMoved = { draggedView, draggedPosition, targetView, targetPosition ->
                    val dragged = getItemAtPosition(draggedPosition)
                    val target = getItemAtPosition(targetPosition)
                    Log.i("aaaaa", "replaced ${dragged.text} with ${target.text}")
                },
                dragOnLongClick = dragOnLongClick
            )
        }

        //todo
        // test drag on long click
        // test drag handle
        DxItemTouchHelper(touchCallBack).apply {
//            setDragHandleId(R.id.listItem_dragHandle)
            attachToRecyclerView(activityMain_rv)
        }
    }

    private fun setClickListeners() {
        val adapter = AdapterClickable(List(100) { index -> ItemClickable("item $index") })
        activityMain_rv.adapter = adapter

        val clickListeners = DxFeatureClickListeners().apply {

            fun getItemAtPosition(position: Int) =
                adapter.getDxAdapterItem<ItemClickable>(position)

            onItemClick = { view, adapterPosition ->
                val item = getItemAtPosition(adapterPosition)
                Log.i("aaaaa", "clicked ${item.text}")
            }

            onItemLongClick = { view, adapterPosition ->
                val item = getItemAtPosition(adapterPosition)
                Log.i("aaaaa", "long clicked ${item.text}")

                true
            }
        }

        adapter.addFeature(clickListeners)
    }

    private fun setVisibilityListeners() {
        //in this case it doesn't matter which adapter is used
        val adapter = AdapterClickable(List(100) { index -> ItemClickable("item $index") })

        activityMain_rv.onItemsVisibilityListener = DxVisibilityListener().apply {

            fun getItemAtPosition(position: Int) =
                adapter.getDxAdapterItem<ItemClickable>(position)

            onFirstItemVisible = { Log.i("aaaaa", getItemAtPosition(0).text) }
            onLastItemVisible = { Log.i("aaaaa", getItemAtPosition(1).text) }
        }
    }

    private fun setScrollListeners() {
        //in this case it doesn't matter which adapter is used
        val adapter = AdapterClickable(List(100) { index -> ItemClickable("item $index") })

        activityMain_rv.onScrollListener = DxScrollListener(1).apply {
            onScrollUp = { Log.i("aaaaa", "scroll up") }
            onScrollDown = { Log.i("aaaaa", "scroll down") }
            onScrollLeft = { Log.i("aaaaa", "scroll left") }
            onScrollRight = { Log.i("aaaaa", "scroll right") }
        }
    }

    @VisibleForTesting
    fun setLayoutManagerVertical() {
        activityMain_rv.apply {
            if (itemDecorationCount > 0) {
                removeItemDecorationAt(0)
            }

            addItemDecoration(
                DividerItemDecoration(this@ActivityMain, DividerItemDecoration.VERTICAL)
            )

            layoutManager = LinearLayoutManager(this@ActivityMain, RecyclerView.VERTICAL, false)
        }
    }

    @VisibleForTesting
    fun setLayoutManagerHorizontal() {
        activityMain_rv.apply {
            if (itemDecorationCount > 0) {
                removeItemDecorationAt(0)
            }

            addItemDecoration(
                DividerItemDecoration(this@ActivityMain, DividerItemDecoration.HORIZONTAL)
            )

            layoutManager = LinearLayoutManager(this@ActivityMain, RecyclerView.HORIZONTAL, false)
        }
    }
}
