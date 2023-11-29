package com.gillongname.processor.sample

import com.gillongname.annotations.GencyclerModel
import com.gillongname.annotations.ViewHolder

@ViewHolder
data class Sample(val text: String, override val layoutResId: Int = 555) : GencyclerModel
