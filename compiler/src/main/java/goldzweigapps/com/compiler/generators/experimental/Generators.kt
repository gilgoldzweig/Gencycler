package goldzweigapps.com.compiler.generators.experimental

import com.squareup.kotlinpoet.*
import goldzweigapps.com.annotations.annotations.GencyclerHolderImpl
import goldzweigapps.com.annotations.annotations.GencyclerViewHolderImpl
import goldzweigapps.com.annotations.annotations.experimental.GencyclerHolder
import goldzweigapps.com.annotations.interfaces.GencyclerDataType
import goldzweigapps.com.compiler.utils.*
import java.io.IOException

/**
 * Created by gilgoldzweig on 25/10/2017.
 */
class Generators(private val viewHolders: List<GencyclerHolderImpl>) {
    //region abstract adapter methods
    fun generateOnCreateViewHolder(): FunSpec {

        val getItemViewType = FunSpec.builder(METHOD_CREATE_VIEW_HOLDER)
                .addParameter("parent", ViewGroup)
                .addParameter("viewType", Int::class)
                .returns(RecyclerViewViewHolder)
                .addModifiers(KModifier.OVERRIDE)
        val layoutSwitch = CodeBlock.builder()
        layoutSwitch.add("return when(viewType) {")
        var supportedTypes = ""
        for (holder in viewHolders) {
            val dataType = ClassName.bestGuess(holder.classType)
            var rClass = ClassName.bestGuess(holder.rClass)
            supportedTypes += "${dataType.simpleName()}, "
            layoutSwitch.add("\n        %T.layout.${holder.layoutName} -> ${dataType.simpleName()}$METHOD_VIEW_HOLDER_CUSTOM(inflater.inflate(viewType, parent, false))", rClass)
        }
        supportedTypes = supportedTypes.removeLastChars(2)
        layoutSwitch.add("\n        else -> throw %T(\"unsupported type, only ($supportedTypes) are supported\")",
                IOException::class.asClassName())
        layoutSwitch.add("\n}")
        getItemViewType.addCode(layoutSwitch.build())
        return getItemViewType.build()
    }

    fun generateOnBindViewHolder(): FunSpec {

        val getItemViewType = FunSpec.builder(METHOD_BIND_VIEW_HOLDER)
                .addParameter("holder", RecyclerViewViewHolder)
                .addParameter(position)
                .addModifiers(KModifier.OVERRIDE)
        val layoutSwitch = CodeBlock.builder()
        layoutSwitch.add("when (holder) {")
        for (holder in viewHolders) {
            val dataType = ClassName.bestGuess(holder.classType)
            layoutSwitch.add("""

                is ${dataType.simpleName()}${METHOD_VIEW_HOLDER_CUSTOM} ->
                        holder.${METHOD_BIND_CUSTOM}${dataType.simpleName()}${METHOD_VIEW_HOLDER_CUSTOM}(position, elementList[position] as ${dataType.simpleName()})

            """.trimIndent())
        }
        layoutSwitch.add("\n}\n")
        getItemViewType.addCode(layoutSwitch.build())
        return getItemViewType.build()
    }

    fun generateItemCount() = FunSpec.builder(GET_ITEM_COUNT)
            .addModifiers(KModifier.OVERRIDE)
            .returns(Int::class)
            .addStatement("return elements.size")
            .build()

    fun generateItemViewType(): FunSpec {
        val getItemViewType = FunSpec.builder(GET_ITEM_VIEW_TYPE)
                .addParameter(position)
                .returns(Int::class)
                .addModifiers(KModifier.OVERRIDE)
        val layoutSwitch = CodeBlock.builder()
        var supportedTypes = ""
        layoutSwitch.add("return when(elementList[position]) {\n")
        for (holder in viewHolders) {
            val dataType = ClassName.bestGuess(holder.classType)
            supportedTypes += "${dataType.simpleName()}, "
            layoutSwitch.add("\n        is %T -> R.layout.%L", dataType, holder.layoutName)
        }
        supportedTypes = supportedTypes.removeLastChars(2)
        layoutSwitch.add("\n        else -> throw %T(\"unsupported type at \$position, only ($supportedTypes) are supported\")",
                IOException::class.asClassName())
        layoutSwitch.add("\n}")
        getItemViewType.addCode(layoutSwitch.build())
        return getItemViewType.build()
    }

    fun generateOnBindAbstractViewHolder(): List<FunSpec> {
        val abstractOnBindViewHolders = ArrayList<FunSpec>()
        for (holder in viewHolders) {
            val dataType = ClassName.bestGuess(holder.classType)
            val viewHolder = "${dataType.simpleName()}${METHOD_VIEW_HOLDER_CUSTOM}"
            val onBindViewHolder = FunSpec.builder("$viewHolder.${METHOD_BIND_CUSTOM}$viewHolder")
                    .addModifiers(KModifier.ABSTRACT)
                    .addParameter(position)
                    .addParameter("element", dataType)
            abstractOnBindViewHolders.add(onBindViewHolder.build())
        }
        return abstractOnBindViewHolders
    }

    fun generateViewHolders(): List<TypeSpec> {
        val viewHolders = ArrayList<TypeSpec>()
        for (holder in this.viewHolders) {
            val dataType = ClassName.bestGuess(holder.classType)
            val viewHolder = TypeSpec.classBuilder("${dataType.simpleName()}${METHOD_VIEW_HOLDER_CUSTOM}")
                    .primaryConstructor(FunSpec.constructorBuilder()
                            .addParameter("view", ClassName.bestGuess("android.view.View"))
                            .build())
                    .superclass(RecyclerViewViewHolder)
                    .addSuperclassConstructorParameter("view")
                    .addProperties(holder.viewFields.map {
                        PropertySpec
                                .builder(it.name, ClassName.bestGuess(it.viewType))
                                .initializer("view.findViewById(R.id.${it.resId})")
                                .build()
                    })
            viewHolders.add(viewHolder.build())
        }
        return viewHolders
    }
    //endregion abstract adapter methods
}
//region helper fields
val elementsConst = "elements"

val position = ParameterSpec.builder("position", Int::class)
        .build()

val positionWithDefault = ParameterSpec.builder("position", Int::class)
        .defaultValue("elements.size")
        .build()
val element = ParameterSpec.builder("element", GencyclerDataType::class)
        .build()

val elementList = ParameterSpec.builder("elementList", elementsList)
        .build()
//endregion helper fields
//region utils
private fun <T> T.same(func: () -> Any) = also { func.invoke() }
private fun FunSpec.Builder.addElementWithPosition(withDefault: Boolean = true) = same {
    addParameter(element)
    if (withDefault) {
        addParameter(positionWithDefault)
    } else {
        addParameter(position)
    }

}
private fun String.removeLastChars(countFromEnd: Int) = if (length > countFromEnd)
    removeRange(length - countFromEnd, length) else this
//endregion utils
//region adapter extension methods
private fun generateSetItemsFunction() =
        FunSpec.builder("setItems")
                .addModifiers(KModifier.OPEN)
                .addParameter(elementList)
                .addStatement("""
                    $elementsConst = ArrayList(elementList)
                    if (isUiThread()) notifyDataSetChanged()
                    """.trimIndent())
                .build()

private fun generateSetItemFunction() =
        FunSpec.builder("setItem")
                .addModifiers(KModifier.OPEN)
                .addElementWithPosition(false)
                .addStatement("""
                        $elementsConst[position] = element
                        if (isUiThread()) notifyItemChanged(position)
                        """.trimIndent())
                .build()

private fun generateAddFunction() =
        FunSpec.builder("add")
                .addModifiers(KModifier.OPEN)
                .addElementWithPosition()
                .addStatement("""
                    $elementsConst.add(position, element)
                    if (isUiThread()) notifyItemInserted(position)
                    """.trimIndent())
                .build()

private fun generateAddRangeFunction() =
        FunSpec.builder("addRange")
                .addModifiers(KModifier.OPEN)
                .addParameter("rangeToInsert", elementsList)
                .addParameter(positionWithDefault)
                .addStatement("""
    $elementsConst.addAll(position, rangeToInsert)
    if (isUiThread()) notifyItemInserted(position)
    """.trimIndent())
                .build()

private fun generateRemoveRangeFunction() =
        FunSpec.builder("removeRange")
                .addModifiers(KModifier.OPEN)
                .addParameter("itemsToRemove", elementsList)
                .addStatement("""
                 val startPosition = elements.indexOf(itemsToRemove[0])
                 elements.removeAll(itemsToRemove)
                 if (isUiThread()) notifyItemRangeRemoved(startPosition, itemsToRemove.size)
            """.trimIndent())
                .build()

private fun generateRemovePositionRangeFunction() =
        FunSpec.builder("removeRange")
                .addModifiers(KModifier.OPEN)
                .addParameter("rangeToRemove", IntRange::class)
                .addStatement("removeRange(elements.subList(rangeToRemove.start, rangeToRemove.last))")
                .build()

private fun generateRemoveElementFunction() =
        FunSpec.builder("remove")
                .addModifiers(KModifier.OPEN)
                .addParameter("itemToRemove", GencyclerDataType::class)
                .addStatement("""
                     val removePosition = elements.indexOf(itemToRemove)
                     if (removePosition != -1) {
                         elements.removeAt(removePosition)
                     if (isUiThread()) notifyItemRemoved(removePosition)
        }
                """.trimIndent())
                .build()

private fun generateRemovePositionFunction() =
        FunSpec.builder("remove")
                .addModifiers(KModifier.OPEN)
                .addParameter("removePosition", Int::class)
                .addStatement("""
                       if (removePosition != -1) {
                            elements.removeAt(removePosition)
                            if (isUiThread()) notifyItemRemoved(removePosition)
                       }
                """.trimIndent())
                .build()

private fun generateClearFunction() =
        FunSpec.builder("clear")
                .addModifiers(KModifier.OPEN)
                .addStatement("""
                    elements.clear()
                    if (isUiThread()) notifyDataSetChanged()
                """.trimIndent())
                .build()

private fun generateIsEmptyFunction() =
        FunSpec.builder("isEmpty")
                .addModifiers(KModifier.OPEN)
                .returns(Boolean::class)
                .addStatement("return elements.isEmpty()")
                .build()

private fun generateIsNotEmptyFunction() =
        FunSpec.builder("isNotEmpty")
                .addModifiers(KModifier.OPEN)
                .returns(Boolean::class)
                .addStatement("return !isEmpty()")
                .build()

private fun generateMinusIntRangeOperator() =
        FunSpec.builder("minus")
                .addModifiers(KModifier.OPEN, KModifier.OPERATOR)
                .addParameter("rangeToRemove", IntRange::class)
                .addStatement("return removeRange(rangeToRemove)")
                .build()

private fun generateMinusElementRangeOperator() =
        FunSpec.builder("minus")
                .addModifiers(KModifier.OPEN, KModifier.OPERATOR)
                .addParameter("itemsToRemove", elementsList)
                .addStatement("return removeRange(itemsToRemove)")
                .build()

private fun generateMinusElementOperator() =
        FunSpec.builder("minus")
                .addModifiers(KModifier.OPEN, KModifier.OPERATOR)
                .addParameter("itemToRemove", GencyclerDataType::class)
                .addStatement("return remove(itemToRemove)")
                .build()

private fun generateMinusPositionOperator() =
        FunSpec.builder("minus")
                .addModifiers(KModifier.OPEN, KModifier.OPERATOR)
                .addParameter("removePosition", Int::class)
                .addStatement("return remove(removePosition)")
                .build()

private fun generatePlusElementRangeOperator() =
        FunSpec.builder("plus")
                .addModifiers(KModifier.OPEN, KModifier.OPERATOR)
                .addParameter("itemsToAdd", elementsList)
                .addStatement("return addRange(itemsToAdd)")
                .build()

private fun generatePlusElementOperator() =
        FunSpec.builder("plus")
                .addModifiers(KModifier.OPEN, KModifier.OPERATOR)
                .addParameter("itemToAdd", GencyclerDataType::class)
                .addStatement("return add(itemToAdd)")
                .build()

private fun generatePlusElementPositionPairOperator() =
        FunSpec.builder("plus")
                .addModifiers(KModifier.OPEN, KModifier.OPERATOR)
                .addParameter("itemToAddInPosition", ParameterizedTypeName.get(Pair::class,
                        GencyclerDataType::class, Int::class))
                .addStatement("return with(itemToAddInPosition) { add(first, second) }")
                .build()

private fun generateGetElementByPositionOperator() =
        FunSpec.builder("get")
                .addModifiers(KModifier.OPEN, KModifier.OPERATOR)
                .addParameter(position)
                .addStatement("return elements[position]")
                .build()

private fun generateGetPositionByElementOperator() =
        FunSpec.builder("get")
                .addModifiers(KModifier.OPEN, KModifier.OPERATOR)
                .addParameter("item", GencyclerDataType::class)
                .addStatement("return elements.indexOf(item)")
                .build()

private fun generateContainsOperator() =
        FunSpec.builder("contains")
                .addModifiers(KModifier.OPEN, KModifier.OPERATOR)
                .addParameter("item", GencyclerDataType::class)
                .addStatement("return elements.contains(item)")
                .build()

private fun generateIsUiThreadFunction() = FunSpec.builder("isUiThread")
        .addCode(CodeBlock.of("""
       return if (%T.VERSION.SDK_INT >= %T.VERSION_CODES.M)
            %T.getMainLooper().isCurrentThread
        else
            Thread.currentThread() == %T.getMainLooper().thread

""".trimIndent(), build, build, looper, looper))
        .returns(BOOLEAN)
        .build()

private fun generateViewHolderOnCLickFunction() =
        FunSpec.builder("${RecyclerViewViewHolder.simpleName()}.onClick")
                .addParameter("onClick", LambdaTypeName.get(null, View, returnType = UNIT))
                .addStatement("""itemView.setOnClickListener {
        onClick.invoke(itemView)
    }
                """.trimIndent())
                .build()

private fun generateViewHolderOnLongCLickFunction() =
        FunSpec.builder("${RecyclerViewViewHolder.simpleName()}.onLongClick")
                .addParameter("onLongClick", LambdaTypeName.get(null, View, returnType = UNIT))
                .addStatement("""itemView.setOnLongClickListener {
        onLongClick.invoke(itemView)
        false
    }
                """.trimIndent())
                .build()

fun generateExtensionClass() = FileSpec.builder(PACKAGE_NAME, recyclerAdapterExtension)
        .addType(TypeSpec.classBuilder(recyclerAdapterExtension)
                .addModifiers(KModifier.ABSTRACT)
                .superclass(RecyclerViewAdapterType)
                .primaryConstructor(FunSpec.constructorBuilder()
                        .addParameter("var elements", elements)
                        .build())
                .addFunction(generateSetItemsFunction())
                .addFunction(generateSetItemFunction())
                .addFunction(generateAddFunction())
                .addFunction(generateAddRangeFunction())
                .addFunction(generateRemoveRangeFunction())
                .addFunction(generateRemovePositionRangeFunction())
                .addFunction(generateRemoveElementFunction())
                .addFunction(generateRemovePositionFunction())
                .addFunction(generateClearFunction())
                .addFunction(generateIsEmptyFunction())
                .addFunction(generateIsNotEmptyFunction())
                .addFunction(generateMinusIntRangeOperator())
                .addFunction(generateMinusElementRangeOperator())
                .addFunction(generateMinusElementOperator())
                .addFunction(generateMinusPositionOperator())
                .addFunction(generatePlusElementRangeOperator())
                .addFunction(generatePlusElementOperator())
                .addFunction(generatePlusElementPositionPairOperator())
                .addFunction(generateGetElementByPositionOperator())
                .addFunction(generateGetPositionByElementOperator())
                .addFunction(generateContainsOperator())
                .addFunction(generateIsUiThreadFunction())
                .addFunction(generateViewHolderOnCLickFunction())
                .addFunction(generateViewHolderOnLongCLickFunction())
                .build())
        .build()

//endregion adapter extension methods