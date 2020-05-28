package com.hotmail.or_dvir.dxlibraries.selectable

import com.hotmail.or_dvir.dxlibraries.BaseItem
import com.hotmail.or_dvir.dxselection.IDxItemSelectable

class ItemSelectable(text: String) : BaseItem(text), IDxItemSelectable {
    override var isSelected = false
}
