package goldzweigapps.com.compiler.dsl.contexts

import com.squareup.kotlinpoet.FunSpec
import goldzweigapps.com.compiler.dsl.Parameter
import goldzweigapps.com.compiler.dsl.constructs.Accessor
import goldzweigapps.com.compiler.dsl.constructs.IAccessor
import goldzweigapps.com.compiler.dsl.helpers.FuncBlockWrapper
import kotlin.reflect.KClass


class FuncBuilder(
        private val accessor: IAccessor = Accessor(),
        private val callBack: (FunSpec)->Unit
){
    private lateinit var builder: FuncBlockWrapper
    private val codeBlockBuilder get()= CodeBlockBuilder(builder)

    operator fun invoke(name: String) = this.also {
        builder = FuncBlockWrapper(FunSpec.builder(name).addModifiers(*accessor.list))
    }
    operator fun invoke(name: String, vararg params: Parameter) = this.apply {
        builder = FuncBlockWrapper(FunSpec.builder(name).also {
            params.forEach {
                (name, type) ->
                it.addParameter(name, type.clazz, *type.modifiers.toTypedArray())
            }
            it.addModifiers(*accessor.list)
        })
    }

    internal fun <T: Any>  returns(clazz: KClass<T>) = builder.returns(clazz)

    internal fun build(builder: FunSpec.Builder, codeBlockBuildScript: CodeBlockBuilder.()->Unit): FunSpec {
        this.builder = FuncBlockWrapper(builder)
        codeBlockBuilder.let{
            codeBlockBuildScript(it)
            return@let it.build()
        }
        return builder.build().also(callBack)
    }


    operator fun invoke(name: String, buildScript: CodeBlockBuilder.()->Unit)= build(name, buildScript)


    operator fun invoke(name: String, vararg params: Parameter, buildScript: CodeBlockBuilder.()->Unit) = build(name, buildScript){
        builder.apply {
            params.forEach {
                (name,type)->addParameter(name, type.clazz, *type.modifiers.toTypedArray())
            }
        }
    }

    private fun build(name: String, codeBlockBuildScript: CodeBlockBuilder.()->Unit, buildScript: FuncBuilder.()->Unit = {}): FunSpec?{
        builder = FuncBlockWrapper(FunSpec.builder(name).addModifiers(*accessor.list))
        codeBlockBuilder.let{
            codeBlockBuildScript(it)
            buildScript(this)
            return@let it.build()
        }
        return builder.build().also(callBack)
    }

    fun  build(builderScript: CodeBlockBuilder.() -> Unit): FunSpec {
        codeBlockBuilder.let{
            builderScript(it)
            return@let it.build()
        }
        return builder.build().also(callBack)
    }
}