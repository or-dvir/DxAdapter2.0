package com.hotmail.or_dvir.dxadapterv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseItem
import kotlinx.android.synthetic.main.activity_base.*
import org.jetbrains.annotations.TestOnly

open class BaseFeatureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setLayoutManagerVertical()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return false
    }

    fun setAdapter(adapter: DxAdapter<*, *>) {
        activityBase_rv.adapter = adapter
    }

    @TestOnly
    fun setAdapterItems(items: MutableList<IDxBaseItem>) {
        (activityBase_rv.adapter as BaseSampleAdapter<IDxBaseItem, *>).setItems(items)
    }

    @VisibleForTesting
    fun setLayoutManagerVertical() {
        activityBase_rv.apply {
            if (itemDecorationCount > 0) {
                removeItemDecorationAt(0)
            }

            addItemDecoration(
                DividerItemDecoration(this@BaseFeatureActivity, DividerItemDecoration.VERTICAL)
            )

            layoutManager = LinearLayoutManager(this@BaseFeatureActivity, RecyclerView.VERTICAL, false)
        }
    }

    @VisibleForTesting
    fun setLayoutManagerHorizontal() {
        activityBase_rv.apply {
            if (itemDecorationCount > 0) {
                removeItemDecorationAt(0)
            }

            addItemDecoration(
                DividerItemDecoration(this@BaseFeatureActivity, DividerItemDecoration.HORIZONTAL)
            )

            layoutManager = LinearLayoutManager(this@BaseFeatureActivity, RecyclerView.HORIZONTAL, false)
        }
    }
}