package goldzweigapps.com.compiler.dsl.contexts

import goldzweigapps.com.compiler.dsl.constructs.controllFlows.IRepeat
import goldzweigapps.com.compiler.dsl.constructs.controllFlows.IfClassStart
import goldzweigapps.com.compiler.dsl.constructs.controllFlows.IiFInterface
import goldzweigapps.com.compiler.dsl.constructs.controllFlows.Repeat
import goldzweigapps.com.compiler.dsl.helpers.BlockWrapper

class CodeBlockBuilder
    internal constructor(
        private val builder: BlockWrapper<*,*>
    ): IRepeat by Repeat(builder),
        IiFInterface by IfClassStart(builder){

    fun statement(first:String, vararg parts:Any){
        builder.statement(first, *parts)
    }

    fun build() = builder.build()
}