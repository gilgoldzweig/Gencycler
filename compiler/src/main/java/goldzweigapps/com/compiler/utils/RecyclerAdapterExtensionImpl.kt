package goldzweigapps.com.compiler.utils
import com.squareup.kotlinpoet.*
import goldzweigapps.com.annotations.interfaces.GencyclerDataType
import goldzweigapps.com.compiler.generators.Generator

//abstract class RecyclerAdapterExtensionImpl(var elements: ArrayList<GencyclerDataType>):
//        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    //region replacement functions
//    open fun setItems(newGenericList: List<GencyclerDataType>) {
//        elements = ArrayList(newGenericList)
//        if (isUiThread()) notifyDataSetChanged()
//    }
//
//    open fun setItem(position: Int, newGenericItem: GencyclerDataType) {
//        elements[position] = newGenericItem
//        if (isUiThread()) notifyItemChanged(position)
//    }
//    //endregion replacement functions
//
//    //region insertion functions
//    open fun add(itemToAdd: GencyclerDataType, position: Int = elements.size) {
//        elements.add(position, itemToAdd)
//        if (isUiThread()) notifyItemInserted(position)
//    }
//
//    open fun addRange(itemsToAdd: List<GencyclerDataType>, positionToInsert: Int = elements.size) {
//        elements.addAll(positionToInsert, itemsToAdd)
//        if (isUiThread()) notifyItemRangeInserted(positionToInsert, itemsToAdd.size)
//    }
//    //endregion insertion functions
//
//    //region removal functions
//    open fun removeRange(itemsToRemove: List<GencyclerDataType>) {
//        val startPosition = elements.indexOf(itemsToRemove[0])
//        elements.removeAll(itemsToRemove)
//        if (isUiThread()) notifyItemRangeRemoved(startPosition, itemsToRemove.size)
//    }
//
//    open fun removeRange(rangeToRemove: IntRange) {
//        removeRange(elements.subList(rangeToRemove.start, rangeToRemove.last))
//    }
//
//    open fun remove(itemToRemove: GencyclerDataType) {
//        val removePosition = elements.indexOf(itemToRemove)
//        if (removePosition != -1) {
//            elements.removeAt(removePosition)
//            if (isUiThread()) notifyItemRemoved(removePosition)
//        }
//    }
//
//    open fun remove(removePosition: Int) {
//        if (removePosition != -1) {
//            elements.removeAt(removePosition)
//            if (isUiThread()) notifyItemRemoved(removePosition)
//        }
//    }
//
//    open fun clear() {
//        elements.clear()
//        if (isUiThread()) notifyDataSetChanged()
//    }
//    //endregion removal functions
//
//    //region empty checks
//    open fun isEmpty() = elements.isEmpty()
//
//    open fun isNotEmpty() = !isEmpty()
//    //endregion empty checks
//
//    //region operators
//    //region minus operators
//    open operator fun minus(rangeToRemove: IntRange) = removeRange(rangeToRemove)
//
//    open operator fun minus(itemsToRemove: List<GencyclerDataType>) = removeRange(itemsToRemove)
//    open operator fun minus(itemToRemove: GencyclerDataType) = remove(itemToRemove)
//    open operator fun minus(removePosition: Int) = remove(removePosition)
//    //endregion minus operators
//
//    //region plus operators
//    open operator fun plus(itemsToAdd: List<GencyclerDataType>) = addRange(itemsToAdd)
//
//    open operator fun plus(itemToAdd: GencyclerDataType) = add(itemToAdd)
//    open operator fun plus(itemToAddInPosition: Pair<GencyclerDataType, Int>) =
//            with(itemToAddInPosition) { add(first, second) }
//    //endregion plus operators
//
//    //region get operators
//    open operator fun get(position: Int) = elements[position]
//
//    open operator fun get(item: GencyclerDataType) = elements.indexOf(item)
//    //endregion get operators
//    //endregion operators
//}
//

val elementsConst = "elements"

val position = ParameterSpec.builder("position", Int::class)
        .build()
val view = ParameterSpec.builder("view", ClassName.bestGuess("android.view.View"))
        .build()

val positionWithDefault = ParameterSpec.builder("position", Int::class)
        .defaultValue("elements.size")
        .build()
val element = ParameterSpec.builder("element", GencyclerDataType::class)
        .build()

val elementList = ParameterSpec.builder("elementList", elementsList)
        .build()

val elementsConstructor = ParameterSpec.builder(elementsConst, elements)
        .addModifiers(KModifier.FINAL)
        .build()

val setItemsStatment = """
    $elementsConst = ArrayList(elementList)
    if (isUiThread()) notifyDataSetChanged()
    """.trimIndent()

val setItemStatment = """
        $elementsConst[position] = element
        if (isUiThread()) notifyItemChanged(position)
    """.trimIndent()

val addItemStatment = """
    $elementsConst.add(position, element)
    if (isUiThread()) notifyItemInserted(position)
    """.trimIndent()

val addItemRangeStatment = """
        $elementsConst.addAll(position, elementList)
        if (isUiThread()) notifyItemRangeInserted(position, elementList.size)
    """.trimIndent()
val isUiThreadStatment = """
       return if (%T.VERSION.SDK_INT >= %T.VERSION_CODES.M)
            %T.getMainLooper().isCurrentThread
        else
            Thread.currentThread() == %T.getMainLooper().thread

""".trimIndent()

class GenerateRecyclerAdapterImpl: Generator {
    fun FunSpec.Builder.addElementWithPosition(withDefault: Boolean = true) = also {
        if (withDefault) {
            addParameter(positionWithDefault)
        } else {
            addParameter(position)
        }
        addParameter(element)
    }

    fun FunSpec.Builder.addElementsWithPosition(withDefault: Boolean = true) = also {
        if (withDefault) {
            addParameter(positionWithDefault)
        } else {
            addParameter(position)
        }
        addParameter(elementList)
    }

    override fun generate() = FileSpec.builder(PACKAGE_NAME, recyclerAdapterExtension)
            .addType(TypeSpec.classBuilder(recyclerAdapterExtension)
                    .addModifiers(KModifier.ABSTRACT)
                    .superclass(RecyclerViewAdapterType)
                    .primaryConstructor(FunSpec.constructorBuilder()
                            .addParameter("var elements", elements)
                            .build())
                    .addFunction(FunSpec.builder("setItems")
                            .addComment("region replacement functions")
                            .addParameter(elementList)
                            .addModifiers(KModifier.OPEN)
                            .addStatement(setItemsStatment)
                            .build())
                    .addFunction(FunSpec.builder("setItem")
                            .addComment("endregion replacement functions")
                            .addElementWithPosition(false)
                            .addModifiers(KModifier.OPEN)
                            .addStatement(setItemStatment)
                            .build())
                    .addFunction(FunSpec.builder("add")
                            .addComment("region insertion functions")
                            .addElementWithPosition()
                            .addModifiers(KModifier.OPEN)
                            .addStatement(addItemStatment)
                            .build())
                    .addFunction(FunSpec.builder("addRange")
                            .addComment("endregion insertion functions")
                            .addElementsWithPosition()
                            .addModifiers(KModifier.OPEN)
                            .addStatement(addItemRangeStatment)
                            .build())
                    .addFunction(FunSpec.builder("isUiThread")
                            .addCode(CodeBlock.of(isUiThreadStatment, build, build, looper, looper))
                            .returns(BOOLEAN)
                            .build())
                    .build())
            .build()

    class Fun(name: String, init: Fun.() -> Unit) {
        private val builder: FunSpec.Builder = FunSpec.builder(name)

        init {
            apply(init)
        }


        var parameters: List<ParameterSpec> = emptyList()
            set(value) {
                builder.addParameters(value)
            }
        var modifiers: List<KModifier> = emptyList()
            set(value) {
                builder.addModifiers(value)
            }
        var statment: String = ""
            set(value) {
                builder.addStatement(value)
            }
        var returns: TypeName? = null
            set(value) {
                value?.let { builder.returns(it) }
            }

        fun addElementWithPosition(withDefault: Boolean = true) = with(builder) {
            if (withDefault) {
                builder.addParameter(positionWithDefault)
            } else {
                addParameter(position)
            }
            addParameter(element)
        }

        fun get() = builder.build()

    }
}