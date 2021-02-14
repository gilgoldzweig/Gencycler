package com.gillongname.processor.sample

import com.gillongname.annotations.GencyclerModel
import com.gillongname.annotations.ViewHolder

@ViewHolder(555)
data class Sample(val text: String) : GencyclerModel
