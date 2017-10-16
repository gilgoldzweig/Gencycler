package goldzweigapps.com.compiler.dsl.contexts

import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.PropertySpec.Builder
import com.squareup.kotlinpoet.PropertySpec.Companion.builder
import goldzweigapps.com.compiler.dsl.Parameter

class PropBuilder(private val build: (PropertySpec)->Unit){
    lateinit var builder: Builder
    operator fun invoke(parameter: Parameter, buildScript: PropBuilder.()->Unit): PropertySpec?{
        val(name, classWrapper) = parameter
        val modifiers =classWrapper.modifiers
        builder = builder(name, classWrapper.clazz,*modifiers.toTypedArray())
        buildScript(this)
        return builder.build().apply(build)
    }

    fun init(name:String){
        builder.initializer(name)
    }
}