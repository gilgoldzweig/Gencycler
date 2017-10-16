package goldzweigapps.com.compiler.dsl.constructs.controllFlows

import goldzweigapps.com.compiler.dsl.constructs.controllFlows.IfClassStart.IfClassEnd
import goldzweigapps.com.compiler.dsl.contexts.CodeBlockBuilder
import goldzweigapps.com.compiler.dsl.helpers.BlockWrapper

interface IiFInterface {
    fun ifp(format: String, vararg parts: Any, statements: CodeBlockBuilder.() -> Unit) : IfClassEnd
    fun ifn(format: String, vararg parts: Any, statements: CodeBlockBuilder.() -> Unit) : IfClassEnd
    fun If(format: String, vararg parts: Any, statements: CodeBlockBuilder.() -> Unit) : IfClassEnd
}

class IfClassStart(private val builder: BlockWrapper<*, *>) : IiFInterface {
    override fun ifp(format: String, vararg parts: Any, statements: CodeBlockBuilder.() -> Unit)
            = buildFirst(statements, "if(($format)==true)", *parts)

    override fun ifn(format: String, vararg parts: Any, statements: CodeBlockBuilder.() -> Unit) =
            buildFirst(statements,"if(($format)==false)", *parts)

    override fun If(format: String, vararg parts: Any, statements: CodeBlockBuilder.() -> Unit)
            = buildFirst(statements,"if($format)", *parts)

    private fun buildFirst(statements: CodeBlockBuilder.() -> Unit, format: String, vararg parts: Any): IfClassEnd {
        builder.beginControlFlow(format, *parts)
        CodeBlockBuilder(builder).let {
            statements(it)
            it.build()
        }
        return IfClassEnd()
    }

    inner class IfClassEnd {
        fun end() {
            builder.endControlFlow()
        }

        fun orElseP(format: String, vararg parts: Any, statements: CodeBlockBuilder.() -> Unit)
                = elseIf("true", format, statements, *parts)

        fun orElseN(format: String, vararg parts: Any, statements: CodeBlockBuilder.() -> Unit)
                = elseIf("false", format, statements, *parts)

        fun orElse(format: String, vararg parts: Any, statements: CodeBlockBuilder.() -> Unit): IfClassEnd {
            builder.nextControlFlow("or else($format)", *parts)
            buildSecond(statements)
            return this
        }

        private fun elseIf(shouldEqual: String, format: String, statements: CodeBlockBuilder.() -> Unit, vararg parts: Any): IfClassEnd {
            builder.nextControlFlow("else if(($format)==$shouldEqual)", *parts)
            buildSecond(statements)
            return this
        }

        infix fun orElse(statements: CodeBlockBuilder.() -> Unit) {
            builder.nextControlFlow("else")
            buildSecond(statements)
            builder.endControlFlow()
        }

        private fun buildSecond(statements: CodeBlockBuilder.() -> Unit) {
            CodeBlockBuilder(builder).let {
                statements(it)
                it.build()
            }
        }
    }
}