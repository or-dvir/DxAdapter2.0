package com.hotmail.or_dvir.dxadapter2.scrollandvisibility

import android.os.Bundle
import android.util.Log
import com.hotmail.or_dvir.dxadapter2.BaseFeatureActivity
import com.hotmail.or_dvir.dxadapter2.clickable.AdapterClickable
import com.hotmail.or_dvir.dxadapter2.clickable.ItemClickable
import com.hotmail.or_dvir.dxrecyclerview.DxScrollListener
import com.hotmail.or_dvir.dxrecyclerview.DxVisibilityListener
import kotlinx.android.synthetic.main.activity_base.*

class ActivityScrollAndVisibility : BaseFeatureActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapterSize = 10
        //in this case it doesn't matter which adapter is used
        val adapter = AdapterClickable(MutableList(adapterSize) { index -> ItemClickable("item $index") })
        setAdapter(adapter)

        activityBase_rv.onItemsVisibilityListener = DxVisibilityListener(
            onFirstItemVisible = { Log.i("aaaaa", "first item visible") },
            onFirstItemInvisible = { Log.i("aaaaa", "first item invisible") },
            onLastItemVisible = { Log.i("aaaaa", "last item visible") },
            onLastItemInvisible = { Log.i("aaaaa", "last item invisible") }
        )

        activityBase_rv.onScrollListener = DxScrollListener(1).apply {
            onScrollUp = { Log.i("aaaaa", "scroll up") }
            onScrollDown = { Log.i("aaaaa", "scroll down") }
            onScrollLeft = { Log.i("aaaaa", "scroll left") }
            onScrollRight = { Log.i("aaaaa", "scroll right") }
        }
    }
}