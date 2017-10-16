package goldzweigapps.com.compiler.dsl.constructs.controllFlows

import goldzweigapps.com.compiler.dsl.contexts.CodeBlockBuilder
import goldzweigapps.com.compiler.dsl.helpers.BlockWrapper

interface IRepeat {
    fun repeat(condition: String, vararg parts: Any, statements: CodeBlockBuilder.() -> Unit)
    fun repeat(statements: CodeBlockBuilder.() -> Unit): Repeat.Condition
    fun init(string: String): Repeat
    fun repeat(from: Int, to: Int, step: Int = 1, statements: CodeBlockBuilder.() -> Unit)
    fun repeat(range: IntRange, statements: CodeBlockBuilder.() -> Unit) = repeat(range.first, range.last, range.step, statements)
    fun repeat(amount: Int, statements: CodeBlockBuilder.() -> Unit) = repeat(1, amount, 1, statements)
}

open class Repeat(private val builder: BlockWrapper<*, *>) : IRepeat {
    override fun repeat(from: Int, to: Int, step: Int, statements: CodeBlockBuilder.()->Unit){
        if (step == 1) {
            if (from == 1){
                builder.beginControlFlow("repeat(%L)", to)
            } else {
                builder.beginControlFlow("for(index in %L .. %L)", from, to)
            }
        } else {
            builder.beginControlFlow("for(index in %L .. %L step %L)", from, to, step)
        }
        endCodeBlock(statements)
    }

    override fun repeat(condition: String, vararg parts: Any, statements: CodeBlockBuilder.() -> Unit){
        builder.beginControlFlow("while($condition)", *parts)
        endCodeBlock(statements)
    }

    private fun endCodeBlock(statements: CodeBlockBuilder.() -> Unit){
        CodeBlockBuilder(builder).let {
            statements(it)
            it.build()
        }
        builder.endControlFlow()
    }
    override fun repeat(statements: CodeBlockBuilder.() -> Unit) = Condition(statements)
    inner class Condition(private val statements: CodeBlockBuilder.() -> Unit) {
        fun forEver() {
            this@Repeat.repeat(condition = "true", parts = *arrayOf(), statements = statements)
        }

        fun asLongAs(condition: String, vararg parts: Any){
            builder.beginControlFlow("do")
            CodeBlockBuilder(builder).let{
                statements(it)
                it.build()
            }
            builder.endControlFlow()
            builder.statement("while($condition)", *parts)
        }
    }

    override fun init(string: String)= Repeat(builder)
}