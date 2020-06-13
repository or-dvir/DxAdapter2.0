package com.hotmail.or_dvir.dxlibraries.clickable

import android.os.Bundle
import android.util.Log
import com.hotmail.or_dvir.dxclick.DxFeatureClick
import com.hotmail.or_dvir.dxlibraries.BaseFeatureActivity

class ActivityClick : BaseFeatureActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = AdapterClickable(
            MutableList(100) { index -> ItemClickable("item $index") }
        )

        setAdapter(adapter)

        val clickFeature = DxFeatureClick<ItemClickable>(
            onItemClick = { view, adapterPosition, item ->
                Log.i("aaaaa", "clicked ${item.text}")
            },
            onItemLongClick = { view, adapterPosition, item ->
                Log.i("aaaaa", "long clicked ${item.text}")
                true
            }
        )

        adapter.addFeature(clickFeature)
    }
}