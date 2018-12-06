//package goldzweigapps.com.compiler.generators
//
//import com.squareup.kotlinpoet.*
//import goldzweigapps.com.annotations.annotations.GencyclerDataContainer
//import goldzweigapps.com.compiler.models.ViewHolder
//import goldzweigapps.com.compiler.utils.*
//import java.io.IOException
//
//
//class Generators(private val rClass: ClassName,
//                 private val viewHolders: Set<ViewHolder>) {
//
//    //region abstract adapter methods
//    fun generateOnCreateViewHolder(): FunSpec {
//
//        val getItemViewType = FunSpec.builder(METHOD_CREATE_VIEW_HOLDER)
//                .addParameter("parent", ViewGroup)
//                .addParameter("viewType", Int::class)
//                .returns(RecyclerViewViewHolder)
//                .addModifiers(KModifier.OVERRIDE)
//
//        if (viewHolders.size == 1) {
//            val dataType = viewHolders[0]
//            CodeBlock.builder()
//                    .
//        }
//
//        val layoutSwitch = CodeBlock.builder()
//
//        layoutSwitch.add("return when(viewType) {")
//
//        var supportedTypes = ""
//        for (holder in viewHolders) {
//            val dataType = ClassName.bestGuess(holder.classType)
//            supportedTypes += "${dataType.simpleName()}, "
//            layoutSwitch.add("\n        %T.layout.${holder.layoutName} -> ${dataType.simpleName()}$METHOD_VIEW_HOLDER_CUSTOM(inflater.inflate(viewType, parent, false))", rClass)
//        }
//
//        supportedTypes = supportedTypes.removeLastChars(2)
//        layoutSwitch.add("\n        else -> throw %T(\"unsupported type, only ($supportedTypes) are supported\")",
//                IOException::class.asClassName())
//        layoutSwitch.add("\n}")
//        getItemViewType.addCode(layoutSwitch.build())
//        return getItemViewType.build()
//    }
//
//    fun generateOnBindViewHolder(): FunSpec {
//
//        val getItemViewType = FunSpec.builder(METHOD_BIND_VIEW_HOLDER)
//                .addParameter("holder", RecyclerViewViewHolder)
//                .addParameter(position)
//                .addModifiers(KModifier.OVERRIDE)
//        val layoutSwitch = CodeBlock.builder()
//        layoutSwitch.add("when (holder) {")
//        for (holder in viewHolders) {
//            val dataType = ClassName.bestGuess(holder.classType)
//            layoutSwitch.add("""
//
//                is ${dataType.simpleName()}$METHOD_VIEW_HOLDER_CUSTOM ->
//                        holder.$METHOD_BIND_CUSTOM${dataType.simpleName()}$METHOD_VIEW_HOLDER_CUSTOM(position, elements[position] as ${dataType.simpleName()})
//
//            """.trimIndent())
//
//        }
//        layoutSwitch.add("\n}\n")
//        getItemViewType.addCode(layoutSwitch.build())
//        return getItemViewType.build()
//    }
//
//    fun generateItemCount() = FunSpec.builder(GET_ITEM_COUNT)
//            .addModifiers(KModifier.OVERRIDE)
//            .returns(Int::class)
//            .addStatement("return elements.size")
//            .build()
//
//    fun generateItemViewType(): FunSpec {
//        val getItemViewType = FunSpec.builder(GET_ITEM_VIEW_TYPE)
//                .addParameter(position)
//                .returns(Int::class)
//                .addModifiers(KModifier.OVERRIDE)
//        val layoutSwitch = CodeBlock.builder()
//        var supportedTypes = ""
//        layoutSwitch.add("val element = elements[position]\n")
//        layoutSwitch.add("return when {\n")
//        for (holder in viewHolders) {
//            val dataType = ClassName.bestGuess(holder.classType)
//            supportedTypes += "${dataType.simpleName()}, "
//            if (holder.unique) {
//                layoutSwitch.add("\n        (element as %T).${holder.uniqueName} == \"${holder.uniqueValue}\" -> R.layout.%L", dataType, holder.layoutName)
//            } else {
//                layoutSwitch.add("\n        element is %T -> R.layout.%L", dataType, holder.layoutName)
//            }
//
//        }
//        supportedTypes = supportedTypes.removeLastChars(2)
//        layoutSwitch.add("\n        else -> throw %T(\"unsupported type at \$position, only ($supportedTypes) are supported\")",
//                IOException::class.asClassName())
//        layoutSwitch.add("\n}")
//        getItemViewType.addCode(layoutSwitch.build())
//        return getItemViewType.build()
//    }
//
//    fun generateOnBindAbstractViewHolder(): List<FunSpec> {
//        val abstractOnBindViewHolders = ArrayList<FunSpec>()
//        for (holder in viewHolders) {
//            val dataType = ClassName.bestGuess(holder.classType)
//            val viewHolder = "${dataType.simpleName()}${METHOD_VIEW_HOLDER_CUSTOM}"
//            val onBindViewHolder = FunSpec.builder("$viewHolder.${METHOD_BIND_CUSTOM}$viewHolder")
//                    .addModifiers(KModifier.ABSTRACT)
//                    .addParameter(position)
//                    .addParameter("element", dataType)
//            abstractOnBindViewHolders.add(onBindViewHolder.build())
//        }
//        return abstractOnBindViewHolders
//    }
//
//    fun generateViewHolders(): List<TypeSpec> {
//        val viewHolders = ArrayList<TypeSpec>()
//        for (holder in this.viewHolders) {
//            val dataType = ClassName.bestGuess(holder.classType)
//            val viewHolder = TypeSpec.classBuilder("${dataType.simpleName()}${METHOD_VIEW_HOLDER_CUSTOM}")
//                    .primaryConstructor(FunSpec.constructorBuilder()
//                            .addParameter("view", ClassName.bestGuess("android.view.View"))
//                            .build())
//                    .superclass(RecyclerViewViewHolder)
//                    .addSuperclassConstructorParameter("view")
//                    .addProperties(holder.viewFields.map {
//                        PropertySpec
//                                .builder(it.name, ClassName.bestGuess(it.viewType))
//                                .initializer("view.findViewById(R.id.${it.resId})")
//                                .build()
//                    })
//            viewHolders.add(viewHolder.build())
//        }
//        return viewHolders
//    }
//    //endregion abstract adapter methods
//}
//
////region helper fields
//val elementsConst = "elements"
//
//val position = ParameterSpec.builder("position", Int::class)
//        .build()
//
//val positionWithDefault = ParameterSpec.builder("position", Int::class)
//        .defaultValue("elements.size")
//        .build()
//val element = ParameterSpec.builder("element", GencyclerDataContainer::class)
//        .build()
//
//val elementList = ParameterSpec.builder("elementList", elements)
//        .build()
//
////endregion helper fields
////region utils
//private fun <T> T.same(func: () -> Any) = also { func.invoke() }
//
//private fun FunSpec.Builder.addElementWithPosition(withDefault: Boolean = true) = same {
//    addParameter(element)
//    if (withDefault) {
//        addParameter(positionWithDefault)
//    } else {
//        addParameter(position)
//    }
//
//}
//
//private fun String.removeLastChars(countFromEnd: Int) = if (length > countFromEnd)
//    removeRange(length - countFromEnd, length) else this
//
////endregion utils