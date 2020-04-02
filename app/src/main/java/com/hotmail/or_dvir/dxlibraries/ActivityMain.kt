package com.hotmail.or_dvir.dxlibraries

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxrecyclerview.R
import kotlinx.android.synthetic.main.activity_main.*

class ActivityMain : AppCompatActivity() {
    var mAdapter = MyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapter.setItems(listOf(MyItem("one"), MyItem("two")))

        activityMain_rv.apply {
            adapter = mAdapter
            setLayoutManagerVertical()
        }
    }

    @VisibleForTesting
    fun setLayoutManagerVertical() {
        activityMain_rv.apply {
            if (itemDecorationCount > 0) {
                removeItemDecorationAt(0)
            }
            addItemDecoration(
                DividerItemDecoration(this@ActivityMain, DividerItemDecoration.VERTICAL)
            )
            layoutManager = LinearLayoutManager(this@ActivityMain, RecyclerView.VERTICAL, false)
        }
    }

    @VisibleForTesting
    fun setLayoutManagerHorizontal() {
        activityMain_rv.apply {
            if (itemDecorationCount > 0) {
                removeItemDecorationAt(0)
            }
            addItemDecoration(
                DividerItemDecoration(this@ActivityMain, DividerItemDecoration.HORIZONTAL)
            )
            layoutManager = LinearLayoutManager(this@ActivityMain, RecyclerView.HORIZONTAL, false)
        }
    }

//    override fun onResume() {
//        setLayoutManagerHorizontal()
//        super.onResume()
//    }
}
