package goldzweigapps.com.compiler.dsl.contexts

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import goldzweigapps.com.compiler.dsl.constructs.IAccessor
import goldzweigapps.com.compiler.dsl.helpers.ClassWrapper
import kotlin.reflect.KClass

class FileBuilder(pack: String, name: String) {

    infix fun <T : Any> String.of(clazz: KClass<T>) = this to ClassWrapper(clazz)
    infix fun String.of(clazz: TypeName) = this to ClassWrapper<Any>(clazz)

    infix fun <T : Any> String.valOf(clazz: KClass<T>) = this to ClassWrapper(clazz, mutableListOf(KModifier.FINAL))
    infix fun String.valOf(clazz: TypeName) = this to ClassWrapper<Any>(clazz, mutableListOf(KModifier.FINAL))

    infix fun <T : Any> String.vararg(clazz: KClass<T>) = this to ClassWrapper(clazz, mutableListOf(KModifier.VARARG))
    infix fun String.vararg(clazz: TypeName) = this to ClassWrapper<Any>(clazz, mutableListOf(KModifier.VARARG))



    private val builder = FileSpec.builder(pack, name)

    operator fun  <T: Any> KClass<T>.invoke(builder: CodeBlockBuilder.()->Unit) = this to builder
    infix fun <T : Any> FuncBuilder.returns(pair: Pair<KClass<T>, CodeBlockBuilder.() -> Unit>): FunSpec {
        val (clazz, builder) = pair
        returns(clazz)
        return build(builder)
    }


    val IAccessor.clazz get() = ClassBuilder(this){builder.addType(it)}
    val IAccessor.func get() = FuncBuilder(this){builder.addFunction(it)}
    val clazz get()= ClassBuilder{builder.addType(it)}
    val func get() = FuncBuilder{builder.addFunction(it)}

    internal fun build(): FileSpec = builder.build()
}

fun file(pack: String, name: String, init: FileBuilder.()->Unit): FileSpec {
    val fileBuilder = FileBuilder(pack, name)
    init(fileBuilder)
    return fileBuilder.build()
}