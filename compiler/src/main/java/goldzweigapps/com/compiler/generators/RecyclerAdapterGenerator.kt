package goldzweigapps.com.compiler.generators

import com.squareup.kotlinpoet.*
import goldzweigapps.com.compiler.models.Adapter
import goldzweigapps.com.compiler.consts.Names
import goldzweigapps.com.compiler.consts.Packages

import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
import goldzweigapps.com.compiler.consts.Methods
import goldzweigapps.com.compiler.consts.Parameters
import goldzweigapps.com.compiler.models.ViewType
import goldzweigapps.com.compiler.utils.simpleEnumName
import goldzweigapps.com.compiler.utils.simpleParameterName


class RecyclerAdapterGenerator {

    private val mutableList = ClassName(Packages.KOTLIN_COLLECTIONS, "MutableList")
    private val contextClassName = ClassName(Packages.ANDROID_CONTENT, "Context")

    private val recyclerAdapterSuperClass = ClassName(Packages.GENCYCLER,
            "GencyclerRecyclerAdapter")


    private val viewHolderClassName = ClassName(Packages.ANDROID_VIEW, "ViewGroup")

    private val adapterKDoc = """
                    RecyclerView adapter with all the boilerplate code generate

                    @param context Used to inflate the layout of the ViewHolder

                    @param elements The data: Each element is attached to a Generated ViewHolder

                    @param updateUi The Parent contains helper methods to make the usage easy,
                           True: the Parent will make a UI Thread test and will call the appropriate notify method
                           False: The task will be performed but the adapter won't be notified


                    @see GencyclerRecyclerAdapter: Parent
                    @see GencyclerHolder: ViewHolder


                """.trimIndent()

    private val abstractBindFuncKDoc = """
                    The generate custom onBind method created for %T

                    @param %L Our generated ViewHolder
                    @param %L The current item
                    @param position The current position


                    @see %T
                    @see %T

                """.trimIndent()


    fun generate(adapter: Adapter): FileSpec {

        val adapterFileBuilder = FileSpec.builder(Packages.GENCYCLER, adapter.name)
                .addAnnotation(AnnotationSpec.builder(JvmName::class)
                        .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                        .addMember("\"${adapter.name}\"")
                        .build())

        val adapterClassBuilder = TypeSpec.classBuilder(adapter.name)
                .addKdoc(adapterKDoc)

        val adapterConstructor = FunSpec.constructorBuilder()
                .addParameter(ParameterSpec.builder(Names.CONTEXT, contextClassName)
                        .build())

        val setupFunctions = ArrayList<FunSpec>()
        val abstractFunctions = ArrayList<FunSpec>()


        if (adapter.viewTypes.size > 1) { //When multiple ViewHolders we're provided

            adapterClassBuilder
                    .superclass(recyclerAdapterSuperClass
                            .plusParameter(Parameters.GENERIC_DATA_CONTAINER)
                            .plusParameter(Parameters.VIEW_HOLDER_SUPER_CLASS))

            adapterConstructor
                    .addParameter(ParameterSpec.builder(Names.ELEMENTS,
                            mutableList.parameterizedBy(Parameters.GENERIC_DATA_CONTAINER))
                            .defaultValue("ArrayList()")
                            .build())


            val itemViewTypeCodeBuilder = CodeBlock.builder()
            val createViewHolderCodeBuilder = CodeBlock.builder()
            val bindViewHolderCodeBuilder = CodeBlock.builder()

            val supportedViewTypesSimpleName = ArrayList<String>()

            for (viewType in adapter.viewTypes) {
                val viewHolder = viewType.viewHolderType
                val dataType = viewType.dataContainerType

                val viewHolderEnumName = viewHolder.simpleEnumName
                val viewHolderSimpleName = viewHolder.simpleName
                val dataTypeSimpleName = dataType.simpleName

                createViewHolderCodeBuilder
                        .insertCreateViewHolderCase(viewType, viewHolderEnumName)

                bindViewHolderCodeBuilder
                        .insertBindViewHolderCase(viewType, viewHolderSimpleName)

                itemViewTypeCodeBuilder
                        .insertItemViewTypeCase(viewType, viewHolderEnumName)


                supportedViewTypesSimpleName.add(dataTypeSimpleName)

                abstractFunctions.add(generateAbstractBindFunction(viewType))

            }

            setupFunctions.add(
                    generateCreateViewHolderFunc(
                            codeBlockBuilder = createViewHolderCodeBuilder,
                            supportViewTypesNames = supportedViewTypesSimpleName))

            setupFunctions.add(generateBindViewHolderFunc(
                    codeBlockBuilder = bindViewHolderCodeBuilder))

            setupFunctions.add(generateItemViewTypeFunc(
                    codeBlockBuilder = itemViewTypeCodeBuilder,
                    supportViewTypesNames = supportedViewTypesSimpleName))

        } else if (adapter.viewTypes.size == 1) { //When only a single ViewHolder was provided

            val viewType = adapter.viewTypes.first()

            val viewHolder = viewType.viewHolderType
            val dataType = viewType.dataContainerType


            adapterClassBuilder
                    .superclass(recyclerAdapterSuperClass
                            .plusParameter(dataType)
                            .plusParameter(viewHolder))

            adapterConstructor
                    .addParameter(ParameterSpec.builder(Names.ELEMENTS,
                            mutableList.parameterizedBy(dataType))
                            .defaultValue("ArrayList()")
                            .build())

            abstractFunctions
                    .add(generateAbstractBindFunction(viewType))

            setupFunctions.add(generateCreateViewHolderFunc(viewType))
            setupFunctions.add(generateBindViewHolderFunc(viewType))
        }

        return adapterFileBuilder
                .addType(adapterClassBuilder
                        .addModifiers(KModifier.ABSTRACT)
                        .primaryConstructor(
                                adapterConstructor
                                        .addParameter(ParameterSpec.builder(Names.UPDATE_UI, BOOLEAN)
                                                .defaultValue("true")
                                                .build())
                                        .build())
                        .addSuperclassConstructorParameter("%L, %L, %L",
                                Names.CONTEXT,
                                Names.ELEMENTS,
                                Names.UPDATE_UI)
                        .addFunctions(setupFunctions)
                        .addFunctions(abstractFunctions)
                        .build())
                .build()
    }


    private fun generateCreateViewHolderFunc(viewType: ViewType? = null,
                                             codeBlockBuilder: CodeBlock.Builder? = null,
                                             supportViewTypesNames: List<String> = emptyList()): FunSpec {


        val createViewHolderFunctionBuilder = FunSpec.builder(Methods.ON_CREATE_VIEW_HOLDER)
                .addParameter("parent", viewHolderClassName)
                .addParameter("viewType", INT)
                .addModifiers(KModifier.OVERRIDE)

        return if (viewType != null) {
            createViewHolderFunctionBuilder
                    .returns(viewType.viewHolderType)
                    .addStatement("return %T(inflate(R.layout.${viewType.layoutName}, parent))",
                            viewType.viewHolderType)
                    .build()
        } else {
            if (codeBlockBuilder == null) {
                throw IllegalArgumentException("At least one parameter must be not null")
            }
            createViewHolderFunctionBuilder
                    .returns(Parameters.VIEW_HOLDER_SUPER_CLASS)
                    .addStatement("val viewHolderType = %T.valueOf(viewType)",
                            Parameters.VIEW_HOLDER_TYPE_ENUM_CLASS)
                    .addStatement("")
                    .beginControlFlow("return when (viewHolderType)")
                    .addCode(codeBlockBuilder
                            .insertExceptionThrowCase(false, supportViewTypesNames)
                            .build())
                    .endControlFlow()
                    .build()

        }
    }

    private fun generateBindViewHolderFunc(viewType: ViewType? = null,
                                           codeBlockBuilder: CodeBlock.Builder? = null): FunSpec {

        val bindViewHolderFunctionBuilder = FunSpec.builder(Methods.ON_BIND_VIEW_HOLDER)
                .addModifiers(KModifier.OVERRIDE)

        return if (viewType != null) {
            bindViewHolderFunctionBuilder
                    .addParameter("holder", viewType.viewHolderType)
                    .addParameter(Parameters.POSITION_PARAMETER_SPEC)
                    .addStatement("%L%L(holder, elements[position], position)",
                            Methods.ABSTRACT_BIND_CUSTOM,
                            viewType.viewHolderType.simpleName)
                    .build()

        } else {
            if (codeBlockBuilder == null) {
                throw IllegalArgumentException("At least one parameter must be not null")
            }
            bindViewHolderFunctionBuilder
                    .addParameter("holder", Parameters.VIEW_HOLDER_SUPER_CLASS)
                    .addParameter(Parameters.POSITION_PARAMETER_SPEC)
                    .addStatement("")
                    .beginControlFlow("when (holder)")
                    .addCode(codeBlockBuilder.build())
                    .endControlFlow()
                    .build()
        }
    }

    private fun generateItemViewTypeFunc(codeBlockBuilder: CodeBlock.Builder,
                                         supportViewTypesNames: List<String>): FunSpec {
        return FunSpec.builder(Methods.GET_VIEW_TYPE)
                .addModifiers(KModifier.OVERRIDE)
                .addParameter(Parameters.POSITION_PARAMETER_SPEC)
                .returns(INT)
                .addStatement("val element = elements[position]")
                .addStatement("")
                .beginControlFlow("return when (element)")
                .addCode(codeBlockBuilder
                        .insertExceptionThrowCase(true, supportViewTypesNames)
                        .build())
                .endControlFlow()
                .build()
    }

    private fun generateAbstractBindFunction(viewType: ViewType): FunSpec {
        val viewHolder = viewType.viewHolderType
        val dataType = viewType.dataContainerType

        val viewHolderParam = ParameterSpec
                .builder(viewHolder.simpleParameterName, viewHolder)
                .build()

        val dataTypeParam = ParameterSpec
                .builder(dataType.simpleParameterName, dataType)
                .build()

        return FunSpec.builder("${Methods.ABSTRACT_BIND_CUSTOM}${viewHolder.simpleName}")
                .addKdoc(abstractBindFuncKDoc, viewHolder,
                        viewHolderParam.name, dataTypeParam.name, viewHolder, dataType)
                .addModifiers(KModifier.ABSTRACT)
                .addParameter(viewHolderParam)
                .addParameter(dataTypeParam)
                .addParameter(Parameters.POSITION_PARAMETER_SPEC)
                .build()
    }


    private fun CodeBlock.Builder.insertCreateViewHolderCase(viewType: ViewType,
                                                             viewHolderEnumName: String): CodeBlock.Builder {

        return addStatement("%T.%L ->", Parameters.VIEW_HOLDER_TYPE_ENUM_CLASS, viewHolderEnumName)
                .indent()
                .addStatement("%T(inflate(viewHolderType.layout, parent))", viewType.viewHolderType)
                .unindent()
                .addStatement("")
    }

    private fun CodeBlock.Builder.insertBindViewHolderCase(viewType: ViewType,
                                                           viewHolderSimpleName: String): CodeBlock.Builder {

        return addStatement("is %T ->", viewType.viewHolderType)
                .indent()
                .addStatement("%L%L(holder, elements[position] as %T, position)",
                        Methods.ABSTRACT_BIND_CUSTOM, viewHolderSimpleName, viewType.dataContainerType)
                .unindent()
                .addStatement("")
    }

    private fun CodeBlock.Builder.insertItemViewTypeCase(viewType: ViewType,
                                                         viewHolderEnumName: String): CodeBlock.Builder {
        return addStatement("is %T ->", viewType.dataContainerType)
                .indent()
                .addStatement("%T.%L.id",
                        Parameters.VIEW_HOLDER_TYPE_ENUM_CLASS, viewHolderEnumName)
                .unindent()
                .addStatement("")
    }


    private fun CodeBlock.Builder.insertExceptionThrowCase(showPositionLine: Boolean = false,
                                                           viewTypesSimpleNames: List<String>? = null): CodeBlock.Builder {
        val builder = addStatement("else ->")
                .indent()
                .addStatement("throw %T(%L",
                        Parameters.ILLEGAL_ARGUMENT_EXCEPTION_CLASS, Names.MULTI_LINE_ESCAPED)
                .indent()

        if (showPositionLine) {
            builder.addStatement("Unsupported type received for position: \$%L", Names.POSITION)
        } else {
            builder.addStatement("Unsupported type received")
        }

        if (viewTypesSimpleNames != null) {
            builder.addStatement("Only %L are supported", viewTypesSimpleNames)
        }

        builder
                .unindent()
                .addStatement("%L.trimIndent())", Names.MULTI_LINE_ESCAPED)
                .unindent()

        return builder
    }

}
