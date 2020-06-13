package com.hotmail.or_dvir.dxlibraries

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.hotmail.or_dvir.dxlibraries.clickable.ActivityClick
import com.hotmail.or_dvir.dxlibraries.draggable.ActivityDrag
import com.hotmail.or_dvir.dxlibraries.expandable.ActivityExpand
import com.hotmail.or_dvir.dxlibraries.scrollandvisibility.ActivityScrollAndVisibility
import com.hotmail.or_dvir.dxlibraries.selectable.ActivitySelect
import com.hotmail.or_dvir.dxlibraries.stickyheader.ActivityStickyHeader
import com.hotmail.or_dvir.dxlibraries.swipeable.ActivitySwipe
import kotlinx.android.synthetic.main.activity_main.*

open class ActivityMain : AppCompatActivity(), View.OnClickListener {

    //todo
    // should i add filtering feature? is that useful?
    // merge develop into master branch
    // export each module as its own library!!!
    // add readme file FOR EACH MODULE
    //      read each files documentation for special notes that should be added to the readme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityMain_ll_allButtons.children.forEach {
            if (it is Button) {
                it.setOnClickListener(this@ActivityMain)
            }
        }
    }

    override fun onClick(v: View?) {
        if (v == null) {
            throw Exception("clicked view was null")
        }

        val clazz =
            when (v.id) {
                R.id.activityMain_btn_click -> ActivityClick::class.java
                R.id.activityMain_btn_drag -> ActivityDrag::class.java
                R.id.activityMain_btn_expand -> ActivityExpand::class.java
                R.id.activityMain_btn_scrollAndVisibility -> ActivityScrollAndVisibility::class.java
                R.id.activityMain_btn_select -> ActivitySelect::class.java
                R.id.activityMain_btn_stickyHeader -> ActivityStickyHeader::class.java
                R.id.activityMain_btn_swipe -> ActivitySwipe::class.java
                else -> throw Exception("did you forget to add the button to this \"when\" statement?")
            }

        val intent = Intent(v.context, clazz)
        startActivity(intent)
    }
}
