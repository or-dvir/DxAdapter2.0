package com.hotmail.or_dvir.dxlibraries

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import com.hotmail.or_dvir.dxclick.DxFeatureClick
import com.hotmail.or_dvir.dxdragandswipe.DxItemTouchCallback
import com.hotmail.or_dvir.dxdragandswipe.DxItemTouchHelper
import com.hotmail.or_dvir.dxdragandswipe.drag.DxFeatureDrag
import com.hotmail.or_dvir.dxlibraries.clickable.AdapterClickable
import com.hotmail.or_dvir.dxlibraries.clickable.ItemClickable
import com.hotmail.or_dvir.dxlibraries.draggable.AdapterDraggable
import com.hotmail.or_dvir.dxlibraries.draggable.ItemDraggable
import com.hotmail.or_dvir.dxlibraries.draggable.ItemNonDraggable
import com.hotmail.or_dvir.dxlibraries.selectable.AdapterSelectable
import com.hotmail.or_dvir.dxlibraries.selectable.ItemSelectable
import com.hotmail.or_dvir.dxlibraries.stickyheader.AdapterStickyHeader
import com.hotmail.or_dvir.dxlibraries.stickyheader.ItemHeader
import com.hotmail.or_dvir.dxlibraries.stickyheader.MyStickyHeaderFeature
import com.hotmail.or_dvir.dxlibraries.swipeable.AdapterSwipeable
import com.hotmail.or_dvir.dxlibraries.swipeable.ItemSwipeable
import com.hotmail.or_dvir.dxlibraries.swipeable.MySwipeFeature
import com.hotmail.or_dvir.dxrecyclerview.DxScrollListener
import com.hotmail.or_dvir.dxrecyclerview.DxVisibilityListener
import com.hotmail.or_dvir.dxselection.DxFeatureSelection
import com.hotmail.or_dvir.dxstickyheader.DxStickyHeaderItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.annotations.TestOnly

class ActivityMain : AppCompatActivity() {

    //todo
    // make all features open so the user can extend them and simply use in the activity...
    //      this should make the code a lot more readable
    // add documentation for everything (mostly copy from DxAdapter)
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

//        setScrollListeners()
//        setVisibilityListeners()
//        setClickListeners()
//        setDragListeners(true, null)
//        setDragListeners(false, R.id.listItem_dragHandle)
//        setDragListenersMixed()
//        setSwipeListeners()
//        setSelectionListeners()
//        setStickyHeader()
    }

    private fun setStickyHeader() {
        val items = mutableListOf<BaseItem>()

        var headerCounter = 1
        for (i in 1..100) {
            if (i % 10 == 0) {
                items.add(ItemHeader("Header $headerCounter"))
                headerCounter++
            } else {
                items.add(BaseItem("item $i"))
            }
        }

        val adapter = AdapterStickyHeader(items)
        setAdapter(adapter)

        val featureHeader = MyStickyHeaderFeature(adapter)
        //todo when documenting add note that this is not really needed but should
        // do it for future updates
        adapter.addFeature(featureHeader)
        activityMain_rv.addItemDecoration(DxStickyHeaderItemDecoration(featureHeader))
    }

    private fun setSelectionListeners() {
        val adapter = AdapterSelectable(
            MutableList(10) { index -> ItemSelectable("item $index") }
        )
        setAdapter(adapter)

        val clickFeature = DxFeatureClick<ItemSelectable>(
            onItemClick = { _, _, _ -> },
            onItemLongClick = { _, _, _ -> true }
        )

        val selectFeature = DxFeatureSelection<ItemSelectable>(
            adapter,
            clickFeature,
            onItemSelectionChanged = { adapterPosition, isSelected, item ->
                Log.i("aaaaa", "${item.text} selected: $isSelected")
            },
            onSelectionModeChanged = { isSelectionModeActive ->
                Log.i("aaaaa", "in selection mode? $isSelectionModeActive")
            }
        )

        adapter.addFeature(selectFeature)
    }

    private fun setSwipeListeners() {
        val adapter = AdapterSwipeable(
            MutableList(10) { index -> ItemSwipeable("item $index") }
        )
        setAdapter(adapter)

        val touchCallBack = DxItemTouchCallback(adapter).apply {
            swipeFeature = MySwipeFeature(
                context = this@ActivityMain,
                onSwipeStart = { view, adapterPosition, item ->
                    Log.i("aaaaa", "swipe start for ${item.text}")
                },
                onSwipeEnd = { view, adapterPosition, item ->
                    Log.i("aaaaa", "swipe end for ${item.text}")
                },
                onItemSwiped = { view, adapterPosition, direction, item ->
                    //IMPORTANT NOTE:
                    //do to the way ItemTouchCallback works, you MUST do something with the item!
                    //(e.g. remove, reset). if you don't, listeners will be called for wrong items
                    //todo add this note in documentation
                    //this resets the item
//                    adapter.removeItem(adapterPosition)
                    adapter.notifyItemChanged(adapterPosition)
                    Log.i("aaaaa", "${item.text} swiped")
                },
                swipeDirections = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            )
        }

        ItemTouchHelper(touchCallBack).attachToRecyclerView(activityMain_rv)
    }

    private fun setDragListenersMixed() {
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

        DxItemTouchHelper(touchCallBack).attachToRecyclerView(activityMain_rv)
    }

    @Suppress("SameParameterValue")
    private fun setDragListeners(dragOnLongClick: Boolean, @IdRes dragHandleId: Int?) {
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
            attachToRecyclerView(activityMain_rv)
        }
    }

    private fun setClickListeners() {
        val adapter = AdapterClickable(
            MutableList(100) { index -> ItemClickable("item $index") }
        )

        setAdapter(adapter)

        val clickListeners = DxFeatureClick<ItemClickable>(
            onItemClick = { view, adapterPosition, item ->
                Log.i("aaaaa", "clicked ${item.text}")
            },
            onItemLongClick = { view, adapterPosition, item ->
                Log.i("aaaaa", "long clicked ${item.text}")
                true
            }
        )

        adapter.addFeature(clickListeners)
    }

    private fun setVisibilityListeners() {
        //in this case it doesn't matter which adapter is used
        val adapter = AdapterClickable(MutableList(100) { index -> ItemClickable("item $index") })
        setAdapter(adapter)

        activityMain_rv.onItemsVisibilityListener = DxVisibilityListener().apply {
            onFirstItemVisible = { Log.i("aaaaa", adapter.getItem(0).text) }
            onLastItemVisible = { Log.i("aaaaa", adapter.getItem(1).text) }
        }
    }

    private fun setScrollListeners() {
        //in this case it doesn't matter which adapter is used
        val adapter = AdapterClickable(MutableList(100) { index -> ItemClickable("item $index") })
        setAdapter(adapter)

        activityMain_rv.onScrollListener = DxScrollListener(1).apply {
            onScrollUp = { Log.i("aaaaa", "scroll up") }
            onScrollDown = { Log.i("aaaaa", "scroll down") }
            onScrollLeft = { Log.i("aaaaa", "scroll left") }
            onScrollRight = { Log.i("aaaaa", "scroll right") }
        }
    }

    @VisibleForTesting
    fun setAdapter(adapter: DxAdapter<*, *>) {
        activityMain_rv.adapter = adapter
    }

    @TestOnly
    fun setAdapterItems(items: MutableList<IDxBaseItem>) {
        (activityMain_rv.adapter as BaseSampleAdapter<IDxBaseItem, *>).setItems(items)
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
