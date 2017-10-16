package goldzweigapps.com.compiler.dsl.contexts


import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import goldzweigapps.com.compiler.dsl.Parameter
import goldzweigapps.com.compiler.dsl.constructs.Accessor
import goldzweigapps.com.compiler.dsl.constructs.IAccessor
import goldzweigapps.com.compiler.dsl.helpers.ClassWrapper

class ClassBuilder(
        private val accessor: IAccessor = Accessor(),
        private val adding:(TypeSpec)->Unit
    ) {
    private lateinit var builder: TypeSpec.Builder

    val IAccessor.clazz get() = ClassBuilder(this) { builder.addType(it) }
    val IAccessor.func get() = FuncBuilder(this) { builder.addFunction(it) }
    val func get() = FuncBuilder { func -> builder.addFunction(func) }
    val prop get() = PropBuilder { prop -> builder.addProperty(prop) }

    fun primaryConstructor(vararg pair: Parameter) {
        val primBuilder = FunSpec.constructorBuilder()
        pair.forEach { (name, type) ->
            run {
                val modifiers = type.modifiers
                if (modifiers.contains(KModifier.FINAL)) {
                    modifiers.remove(KModifier.FINAL)
                    prop.invoke(name to ClassWrapper<Any>(type.clazz, modifiers)) {
                        init(name)
                    }
                }
                primBuilder.addParameter(name, type.clazz, *modifiers.toTypedArray())
            }
            builder.primaryConstructor(primBuilder.build())
        }
    }
    fun superParent(vararg pair: Parameter) {
        val primBuilder = FunSpec.constructorBuilder()
        pair.forEach { (name, type) ->
            run {
                val modifiers = type.modifiers
                if (modifiers.contains(KModifier.FINAL)) {
                    modifiers.remove(KModifier.FINAL)
                    prop.invoke(name to ClassWrapper<Any>(type.clazz, modifiers)) {
                        init(name)
                    }
                }
                primBuilder.addParameter(name, type.clazz, *modifiers.toTypedArray())
            }
            builder.primaryConstructor(primBuilder.build())
        }
    }

    operator fun invoke(name: String) = build(name) {}

    operator fun invoke(name: String, vararg pars: Parameter, init: ClassBuilder.() -> Unit) = build(name) {
        primaryConstructor(*pars)
        init(this)
    }

    private inline fun build(name: String, init: ClassBuilder.() -> Unit): TypeSpec? {
        builder = TypeSpec.classBuilder(name)
        builder.addModifiers(*accessor.list)
        init(this)
        return builder.build().also(adding)
    }
}