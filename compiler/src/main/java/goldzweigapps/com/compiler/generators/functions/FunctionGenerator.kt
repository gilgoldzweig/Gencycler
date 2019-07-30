package goldzweigapps.com.compiler.generators.functions

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName
import goldzweigapps.com.compiler.models.Adapter

abstract class FunctionGenerator(val adapter: Adapter) {

    fun generate(): FunSpec {
        return if (adapter.viewTypes.size == 1) {
            generateSingleViewTypeFunction()
        } else
            generateMultipleViewTypeFunction()
    }

    open fun superInterfaces(): List<TypeName> = emptyList()

    open fun superConstructorParameters(): List<ParameterSpec> = emptyList()

    /**
     * Generates the function when only a single view type is requested
     */
    abstract fun generateSingleViewTypeFunction(): FunSpec

    /**
     * Generates the function when multiple view types are requested
     */
    abstract fun generateMultipleViewTypeFunction(): FunSpec

}