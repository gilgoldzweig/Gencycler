package com.gillongname.annotations

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Adapter(vararg val value: KClass<in GencyclerModel>)
