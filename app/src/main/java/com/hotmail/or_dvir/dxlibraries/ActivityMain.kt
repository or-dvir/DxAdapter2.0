package com.hotmail.or_dvir.dxlibraries

import android.os.Bundle
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxlibraries.adapters.BaseSampleAdapter
import com.hotmail.or_dvir.dxlibraries.adapters.clickable.ClickableAdapter
import com.hotmail.or_dvir.dxlibraries.adapters.clickable.ClickableItem
import com.hotmail.or_dvir.dxrecyclerview.DxScrollListener
import com.hotmail.or_dvir.dxrecyclerview.DxVisibilityListener
import com.hotmail.or_dvir.featureclicklisteners.DxFeatureClickListeners
import kotlinx.android.synthetic.main.activity_main.*

class ActivityMain : AppCompatActivity() {
    lateinit var mAdapter: BaseSampleAdapter<*>

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
    // delete all example test files from all modules

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setLayoutManagerVertical()

        val adapter =
            ClickableAdapter(List(100) { index ->
                ClickableItem("item $index")
            })

//        setScrollListeners()
//        setVisibilityListeners()
//        setClickListeners()

        setAdapter(adapter)

        activityMain_btn.setOnClickListener {
            mAdapter.setItems(List(5) { index -> ClickableItem("item $index") })
        }
    }

    fun setAdapter(adapter: BaseSampleAdapter<*>) {
        mAdapter = adapter
        activityMain_rv.adapter = mAdapter
    }

    private fun setClickListeners() {
        val clickListeners = DxFeatureClickListeners().apply {

            fun getItemAtPosition(position: Int) =
                mAdapter.getDxAdapterItem<ClickableItem>(position)

            onItemClick = { view, adapterPosition ->
                val item = getItemAtPosition(adapterPosition)
                Log.i("aaaaa", "clicked ${item.text}")
            }

            onItemLongClick = { view, adapterPosition ->
                val item = getItemAtPosition(adapterPosition)
                Log.i("aaaaa", "long clicked ${item.text}")

                true
            }
        }

        mAdapter.addFunctionality(clickListeners)
    }

    private fun setVisibilityListeners() {
        activityMain_rv.onItemsVisibilityListener = DxVisibilityListener().apply {

            fun getItemAtPosition(position: Int) =
                mAdapter.getDxAdapterItem<ClickableItem>(position)

            onFirstItemVisible = { Log.i("aaaaa", getItemAtPosition(0).text) }
            onLastItemVisible = { Log.i("aaaaa", getItemAtPosition(1).text) }
        }
    }

    private fun setScrollListeners() {
        activityMain_rv.onScrollListener = DxScrollListener(1).apply {
            onScrollUp = { Log.i("aaaaa", "scroll up") }
            onScrollDown = { Log.i("aaaaa", "scroll down") }
            onScrollLeft = { Log.i("aaaaa", "scroll left") }
            onScrollRight = { Log.i("aaaaa", "scroll right") }
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
