package com.hotmail.or_dvir.dxlibraries.swipeable

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import com.hotmail.or_dvir.dxdragandswipe.DxItemTouchCallback
import com.hotmail.or_dvir.dxlibraries.FeatureActivity
import kotlinx.android.synthetic.main.activity_base.*

class ActivitySwipe : FeatureActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = AdapterSwipeable(
            MutableList(10) { index -> ItemSwipeable("item $index") }
        )
        setAdapter(adapter)

        val touchCallBack = DxItemTouchCallback(adapter).apply {
            swipeFeature = MySwipeFeature(
                context = this@ActivitySwipe,
                onSwipeStart = { view, adapterPosition, item ->
                    Log.i("aaaaa", "swipe start for ${item.text}")
                },
                onSwipeEnd = { view, adapterPosition, item ->
                    Log.i("aaaaa", "swipe end for ${item.text}")
                },
                onItemSwiped = { view, adapterPosition, direction, item ->
                    //IMPORTANT NOTE:
                    //due to the way ItemTouchCallback works, you MUST do something with the item!
                    //(e.g. remove, reset). if you don't, listeners will be called for wrong parameters
                    //this resets the item
                    adapter.notifyItemChanged(adapterPosition)
                    Log.i("aaaaa", "${item.text} swiped")
                },
                swipeDirections = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            )
        }

        ItemTouchHelper(touchCallBack).attachToRecyclerView(activityBase_rv)
    }
}