package goldzweigapps.com.compiler.dsl.helpers

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import java.lang.reflect.Type
import kotlin.reflect.KClass

class ClassWrapper<T:Any>(val clazz: TypeName,
                          val modifiers: MutableList<KModifier> = mutableListOf()) {
    constructor(clazz: KClass<T>,
                modifiers: MutableList<KModifier> = mutableListOf()) : this(clazz.asTypeName(), modifiers)
}
