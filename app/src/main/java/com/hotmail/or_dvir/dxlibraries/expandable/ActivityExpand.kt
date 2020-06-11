package com.hotmail.or_dvir.dxlibraries.expandable

import android.os.Bundle
import android.util.Log
import com.hotmail.or_dvir.dxclick.DxFeatureClick
import com.hotmail.or_dvir.dxexpansion.DxFeatureExpansion
import com.hotmail.or_dvir.dxlibraries.FeatureActivity

class ActivityExpand : FeatureActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = AdapterExpandable(
            MutableList(10) { index -> ItemExpandable("item $index") }
        )
        setAdapter(adapter)

        val clickFeature = DxFeatureClick<ItemExpandable>(
            onItemClick = { _, _, _ -> },
            onItemLongClick = { _, _, _ -> true }
        )

        val expandFeature = DxFeatureExpansion(
            adapter,
            clickFeature,
            defaultClickBehavior = true,
            onlyOneItemExpanded = true, //change as desired
            onItemExpansionStateChanged = { adapterPosition, isExpanded, item ->
                val expandedOrCollapsed =
                    if (isExpanded) {
                        "expanded"
                    } else {
                        "collapsed"
                    }

                Log.i("aaaaa", "item ${item.text} $expandedOrCollapsed");
            }
        )

        adapter.addFeature(expandFeature)
    }
}