package com.gillongname.gencycler.models

import com.gillongname.annotations.GencyclerModel
import com.gillongname.annotations.ViewHolder
import com.gillongname.gencycler.R

@ViewHolder(R.layout.sample)
data class Sample(val text: String) : GencyclerModel
