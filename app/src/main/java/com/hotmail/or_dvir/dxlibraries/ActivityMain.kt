package com.hotmail.or_dvir.dxlibraries

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxrecyclerview.DxScrollListener
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

            onItemsVisibilityListener = DxVisibilityListener().apply {
                onFirstItemVisible = { Log.i("aaaaa", "first listener") }
//                onLastItemVisible = { Log.i("aaaaa", mAdapter.items[1].text) }
            }
//            onScrollListener = DxScrollListener(0).apply {
//                onScrollDown = { Log.i("aaaaa", "down") }
//                onScrollUp = { Log.i("aaaaa", "up") }
//                onScrollLeft = { Log.i("aaaaa", "left") }
//                onScrollRight = { Log.i("aaaaa", "right") }
//            }

            found the answer! (kind of)
            the key is to invoke the listeners in the recyclerview class whenever they are set
                    that way they will also be trigerred if they are set at a later stage on the recycler view

            mAdapter.items = (listOf(MyItem("1"), MyItem("2")))
            mAdapter.items = (listOf(MyItem("3"), MyItem("4")))
            mAdapter.items = (listOf(MyItem("5"), MyItem("6")))

            postDelayed({
                onItemsVisibilityListener = DxVisibilityListener().apply {
                    onFirstItemVisible = { Log.i("aaaaa", mAdapter.items[0].text) }
                    onLastItemVisible = { Log.i("aaaaa", mAdapter.items[1].text) }
                }
            }, 3000)
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
