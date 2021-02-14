package com.gillongname.annotations.adapter

import com.gillongname.annotations.GencyclerModel
import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Adapter(
    vararg val value: KClass<out GencyclerModel>,
    val name: String = ""
)
