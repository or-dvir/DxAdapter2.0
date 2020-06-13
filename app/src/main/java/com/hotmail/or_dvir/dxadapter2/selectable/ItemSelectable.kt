package com.hotmail.or_dvir.dxadapter2.selectable

import com.hotmail.or_dvir.dxadapter2.BaseItem
import com.hotmail.or_dvir.dxselection.IDxItemSelectable

class ItemSelectable(text: String) : BaseItem(text), IDxItemSelectable {
    override var isSelected = false
}
