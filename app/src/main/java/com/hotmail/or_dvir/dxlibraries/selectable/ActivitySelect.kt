package com.hotmail.or_dvir.dxlibraries.selectable

import android.os.Bundle
import android.util.Log
import com.hotmail.or_dvir.dxclick.DxFeatureClick
import com.hotmail.or_dvir.dxlibraries.BaseFeatureActivity
import com.hotmail.or_dvir.dxselection.DxFeatureSelection

class ActivitySelect : BaseFeatureActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = AdapterSelectable(
            MutableList(10) { index -> ItemSelectable("item $index") }
        )
        setAdapter(adapter)

        val clickFeature = DxFeatureClick<ItemSelectable>(
            onItemClick = { _, _, _ -> },
            onItemLongClick = { _, _, _ -> true }
        )

        val selectFeature = DxFeatureSelection(
            adapter,
            clickFeature,
            defaultClickBehavior = true,
            onItemSelectionChanged = { adapterPosition, isSelected, item ->
                Log.i("aaaaa", "${item.text} selected: $isSelected")
            },
            onSelectionModeChanged = { isSelectionModeActive ->
                Log.i("aaaaa", "in selection mode? $isSelectionModeActive")
            }
        )

        adapter.addFeature(selectFeature)
    }
}