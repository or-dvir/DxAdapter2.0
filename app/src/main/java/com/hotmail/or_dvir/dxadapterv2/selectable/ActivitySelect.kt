package com.hotmail.or_dvir.dxadapterv2.selectable

import android.os.Bundle
import android.util.Log
import com.hotmail.or_dvir.dxclick.DxFeatureClick
import com.hotmail.or_dvir.dxadapterv2.BaseFeatureActivity
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

        //note that the click feature is automatically added in this case
        adapter.addFeature(selectFeature)

        selectFeature.select(listOf(0,2,4,6,8))
        selectFeature.deselect(listOf(2,4))
    }
}