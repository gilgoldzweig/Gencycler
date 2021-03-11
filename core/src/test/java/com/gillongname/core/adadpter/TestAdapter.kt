package com.gillongname.core.adadpter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gillongname.annotations.GencyclerModel
import com.gillongname.core.GencyclerListAdapter

class TestAdapter(elements: ArrayList<TestGencyclerModel>) :
    GencyclerListAdapter<TestGencyclerModel, TestViewHolder>(elements) {


    /**
     * Shouldn't be called, used so I can mock things
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder =
        TestViewHolder(parent)

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) = Unit

    override fun getItemCount(): Int = 100
}


class TestViewHolder(view: View) : RecyclerView.ViewHolder(view)

data class TestGencyclerModel(val id: Int = -1) : GencyclerModel