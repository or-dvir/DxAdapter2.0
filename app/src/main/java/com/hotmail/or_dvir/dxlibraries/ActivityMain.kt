package com.hotmail.or_dvir.dxlibraries

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseFeature
import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import com.hotmail.or_dvir.dxlibraries.clickable.AdapterClickable
import com.hotmail.or_dvir.dxlibraries.clickable.ItemClickable
import com.hotmail.or_dvir.dxlibraries.clickable.ItemNonClickable
import com.hotmail.or_dvir.dxrecyclerview.DxScrollListener
import com.hotmail.or_dvir.featureclicklisteners.DxFeatureClick
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
    // make all classes open so they can be extended

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setLayoutManagerVertical()

//        setScrollListeners()
//        setVisibilityListeners()
//        setClickListeners()
//        setDragListeners(true, null)
//        setDragListeners(false, R.id.listItem_dragHandle)
//        setSwipeListeners()
    }

//    private fun setSwipeListeners() {
//        val adapter = AdapterSwipeable(
//            MutableList(10) { index -> ItemSwipeable("item $index") }
//        )
//        setAdapter(adapter)
//
//        val touchCallBack = DxItemTouchCallback(adapter).apply {
//            fun getItemAtPosition(position: Int) =
//                adapter.getDxAdapterItem<ItemSwipeable>(position)
//
//            swipeFeature = MySwipeFeature(
//                context = this@ActivityMain,
//                onSwipeStart = { view, adapterPosition ->
//                    val item = getItemAtPosition(adapterPosition)
//                    Log.i("aaaaa", "swipe start for ${item.text}")
//                },
//                onSwipeEnd = { view, adapterPosition ->
//                    val item = getItemAtPosition(adapterPosition)
//                    Log.i("aaaaa", "swipe end for ${item.text}")
//                },
//                onItemSwiped = { view, adapterPosition, direction ->
//                    val item = getItemAtPosition(adapterPosition)
//                    //IMPORTANT NOTE:
//                    //do to the way ItemTouchCallback works, you MUST do something with the item!
//                    //(e.g. remove, reset). if you don't, listeners will be called for wrong items
//                    //todo add this note in documentation
//                    //this resets the item
////                    adapter.removeItem(adapterPosition)
//                    adapter.notifyItemChanged(adapterPosition)
//                    Log.i("aaaaa", "${item.text} swiped")
//                },
//                swipeDirections = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
//            )
//        }
//
//        ItemTouchHelper(touchCallBack).attachToRecyclerView(activityMain_rv)
//    }
//
//    private fun setDragListeners(dragOnLongClick: Boolean, @IdRes dragHandleId: Int?) {
//        val adapter = AdapterDraggable(
//            MutableList(100) { index -> ItemDraggable("item $index") }
//        )
//
//        setAdapter(adapter)
//
//        val touchCallBack = DxItemTouchCallback(adapter).apply {
//            fun getItemAtPosition(position: Int) =
//                adapter.getDxAdapterItem<ItemDraggable>(position)
//
//            dragFeature = DxFeatureDrag(
//                onDragStart = { view, adapterPosition ->
//                    val item = getItemAtPosition(adapterPosition)
//                    Log.i("aaaaa", "drag start for ${item.text}")
//                },
//                onDragEnd = { view, adapterPosition ->
//                    val item = getItemAtPosition(adapterPosition)
//                    Log.i("aaaaa", "drag end for ${item.text}")
//                },
//                onItemMoved = { draggedView, draggedPosition, targetView, targetPosition ->
//                    val dragged = getItemAtPosition(draggedPosition)
//                    val target = getItemAtPosition(targetPosition)
//                    Log.i(
//                        "aaaaa",
//                        "replaced ${dragged.text}($draggedPosition) with ${target.text}($targetPosition)"
//                    )
//                },
//                dragDirections = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
//                dragOnLongClick = dragOnLongClick
//            )
//        }
//
//        //todo
//        // test drag on long click
//        // test drag handle
//        DxItemTouchHelper(touchCallBack).apply {
//            dragHandleId?.apply { setDragHandleId(this) }
//            attachToRecyclerView(activityMain_rv)
//        }
//    }

    private fun setClickListeners() {
        val adapter = AdapterClickable(MutableList(100) { index -> ItemClickable("item $index") })
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

//    private fun setVisibilityListeners() {
//        //in this case it doesn't matter which adapter is used
//        val adapter = AdapterClickable(MutableList(100) { index -> ItemClickable("item $index") })
//        setAdapter(adapter)
//
//        activityMain_rv.onItemsVisibilityListener = DxVisibilityListener().apply {
//
//            fun getItemAtPosition(position: Int) =
//                adapter.getDxAdapterItem<ItemClickable>(position)
//
//            onFirstItemVisible = { Log.i("aaaaa", getItemAtPosition(0).text) }
//            onLastItemVisible = { Log.i("aaaaa", getItemAtPosition(1).text) }
//        }
//    }

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

    BEFORE YOU DO ANY OF THE OTHER THINGS, TEST MySwipeFeature with the new generic ITEM!!
    this is where is the upper bound problem is

    go through ALL EXISTING FILES and make changes to add generic ITEM to them
    IN ALL FEATURES, make sure you call the listeners ONLY if the item matches the type (e.g. only trigger click listeners for items of type IDxItemClickable)
    modify all test files
    FOR ALL TEST FILES: add tests for mixed items in the same adapter (e.g. clickable and non-clickable)

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
