package com.gillongname.gencycler.models

import com.gillongname.annotations.GencyclerModel
import com.gillongname.annotations.ViewHolder
import com.gillongname.gencycler.R

@ViewHolder
data class Sample(val text: String, override val layoutResId: Int = R.layout.sample) : GencyclerModel {}
