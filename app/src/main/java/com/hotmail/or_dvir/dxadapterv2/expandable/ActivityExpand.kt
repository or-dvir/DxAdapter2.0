package com.hotmail.or_dvir.dxadapterv2.expandable

import android.os.Bundle
import android.util.Log
import com.hotmail.or_dvir.dxadapterv2.BaseFeatureActivity
import com.hotmail.or_dvir.dxclick.DxFeatureClick
import com.hotmail.or_dvir.dxexpansion.DxFeatureExpansion

class ActivityExpand : BaseFeatureActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = AdapterExpandable(
            MutableList(10) { index ->
                ItemExpandable("item $index").apply {
                    //disable expansion on every 5th item
                    if (index % 5 == 0) {
                        isExpansionEnabled = false
                    }
                }
            }
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

        //note that the click feature is automatically added in this case
        adapter.addFeature(expandFeature)
    }
}