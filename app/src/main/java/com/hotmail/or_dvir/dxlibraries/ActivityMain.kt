package com.hotmail.or_dvir.dxlibraries

import android.os.Bundle
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class ActivityMain : AppCompatActivity() {
    var mAdapter = MyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityMain_rv.apply {
            adapter = mAdapter
            setLayoutManagerVertical()
            mAdapter.items = (listOf(MyItem("one"), MyItem("two")))

//            onItemsVisibilityListener = DxVisibilityListener().apply {
//                onFirstItemVisible = { Log.i("aaaaa", mAdapter.items[0].text) }
//                onLastItemVisible = { Log.i("aaaaa", mAdapter.items[1].text) }
//            }
//
//            postDelayed({
//                onItemsVisibilityListener = DxVisibilityListener().apply {
//                    onFirstItemVisible = { Log.i("aaaaa", mAdapter.items[0].text) }
//                    onLastItemVisible = { Log.i("aaaaa", mAdapter.items[1].text) }
//                }
//
//                mAdapter.items = (listOf(MyItem("3"), MyItem("4")))
//            }, 3000)
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
}
