package com.hotmail.or_dvir.dxlibraries

import android.os.Bundle
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxrecyclerview.DxScrollListener
import kotlinx.android.synthetic.main.activity_main.*

class ActivityMain : AppCompatActivity() {
    var mAdapter = MyAdapter()

    //todo
    // export each module as its own library!!!
    // remove unnecessary dependencies FOR EACH MODULE!!!!
    //      pay SPECIAL ATTENTION to modules that depends on one-another (e.g. DxRecyclerView and DxCountingIdlingResource)
    // go over all to-do's from ALL the modules
    // define project wide constants for versions of:
    //      EVERYTHING IN ALL BUILD.GRADLE FILES!!!!!
    // add documentation about which dependencies EACH MODULE forwards to the user
    // make sure to have proper documentation for all classes in ALL MODULES
    // add readme file to all modules

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setLayoutManagerVertical()

        mAdapter.items = List(100) { index -> MyItem("item $index") }

        activityMain_rv.apply {
            adapter = mAdapter

//            onScrollListener = DxScrollListener(1).apply {
//                onScrollUp = { Log.i("aaaaa", "scroll up") }
//                onScrollDown = { Log.i("aaaaa", "scroll down") }
//                onScrollLeft = { Log.i("aaaaa", "scroll left") }
//                onScrollRight = { Log.i("aaaaa", "scroll right") }
//            }

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
