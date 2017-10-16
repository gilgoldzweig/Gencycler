package goldzweigapps.com.compiler.dsl.helpers

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import kotlin.reflect.KClass


interface BlockWrapper<out RETURN, out SELF>{
    fun statement(first:String, vararg parts:Any)
    fun beginControlFlow(controlFlow: String, vararg args: Any): SELF
    fun nextControlFlow(controlFlow: String, vararg args: Any) : SELF
    fun endControlFlow() : SELF
    fun addCode(codeBlock: CodeBlock) : SELF
    fun build() : RETURN
}

class CodeBlockWrapper private constructor(
        private val builder: CodeBlock.Builder = CodeBlock.builder()
) : BlockWrapper<CodeBlock, CodeBlock.Builder>{
    override fun addCode(codeBlock: CodeBlock) = builder.add(codeBlock)

    override fun beginControlFlow(controlFlow: String, vararg args: Any)
            = builder.beginControlFlow(controlFlow,*args)

    override fun nextControlFlow(controlFlow: String, vararg args: Any)
            = builder.nextControlFlow(controlFlow, *args)

    override fun endControlFlow()
            = builder.endControlFlow()

    constructor(): this(CodeBlock.builder())

    override fun statement(first:String, vararg parts:Any){
        builder.addStatement(first, *parts)
    }
    override fun build() = builder.build()
}




class FuncBlockWrapper internal constructor(
        private val builder: FunSpec.Builder
) : BlockWrapper<FunSpec, FunSpec.Builder>
{
    fun <T: Any> returns(clazz: KClass<T>) = builder.returns(clazz)
    fun returns(clazz: TypeName) = builder.returns(clazz)

    override fun addCode(codeBlock: CodeBlock) = builder.addCode(codeBlock)

    override fun statement(first: String, vararg parts: Any) {
        builder.addStatement(first, *parts)
    }

    override fun beginControlFlow(controlFlow: String, vararg args: Any)
            = builder.beginControlFlow(controlFlow, *args)

    override fun nextControlFlow(controlFlow: String, vararg args: Any)
            = builder.nextControlFlow(controlFlow, *args)

    override fun endControlFlow() = builder.endControlFlow()

    override fun build() = builder.build()

    fun addParameter(name: String, clazz: KClass<*>, vararg mods : KModifier)
            = builder.addParameter(name, clazz, *mods)

    fun addParameter(name: String, clazz: TypeName, vararg mods : KModifier)
            = builder.addParameter(name, clazz, *mods)
}