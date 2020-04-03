package com.hotmail.or_dvir.dxlibraries

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxrecyclerview.DxVisibilityListener
import com.hotmail.or_dvir.dxrecyclerview.R
import kotlinx.android.synthetic.main.activity_main.*

class ActivityMain : AppCompatActivity() {
    var mAdapter = MyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        mAdapter.setItems(listOf(MyItem("one"), MyItem("two")))

        activityMain_rv.apply {
            adapter = mAdapter
            setLayoutManagerVertical()
            mAdapter.items = (listOf(MyItem("one"), MyItem("two")))

//            onItemsVisibilityListener = DxVisibilityListener().apply {
//                onFirstItemVisible = { Log.i("aaaaa", mAdapter.items[0].text) }
//                onLastItemVisible = { Log.i("aaaaa", mAdapter.items[1].text) }
//            }

            when called from onCreate() the recycler view scroll listener is called.
            when called from test, scroll listener is called before the visibility listener is set,
            and then the on scroll is not called anymore which means the visibility listener is not
            triggered

            why does it work from onCreate but not from test?

            mAdapter.items = (listOf(MyItem("1"), MyItem("2")))
            mAdapter.items = (listOf(MyItem("3"), MyItem("4")))
            mAdapter.items = (listOf(MyItem("5"), MyItem("6")))
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
