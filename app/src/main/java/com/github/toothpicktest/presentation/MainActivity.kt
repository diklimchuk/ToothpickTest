package com.github.toothpicktest.presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.toothpicktest.R.layout
import kotlinx.android.synthetic.main.activity_main.images
import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setSupportActionBar(toolbar)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        images.adapter = ImagesAdapter(this)
    }
}
