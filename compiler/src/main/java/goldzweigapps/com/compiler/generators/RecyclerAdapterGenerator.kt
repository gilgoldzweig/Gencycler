package goldzweigapps.com.compiler.generators

import com.squareup.kotlinpoet.*
import goldzweigapps.com.compiler.models.Adapter

import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
import com.squareup.kotlinpoet.jvm.jvmOverloads
import goldzweigapps.com.compiler.consts.*
import goldzweigapps.com.compiler.models.ViewType
import goldzweigapps.com.compiler.utils.simpleEnumName
import goldzweigapps.com.compiler.utils.simpleParameterName


class RecyclerAdapterGenerator(private val rClass: ClassName) {

	private val mutableList = ClassName(Packages.KOTLIN_COLLECTIONS, "MutableList")
	private val contextClassName = ClassName(Packages.ANDROID_CONTENT, "Context")

	private val recyclerAdapterSuperClass = ClassName(Packages.GENCYCLER,
			"GencyclerRecyclerAdapter")

	private val viewHolderClassName = ClassName(Packages.ANDROID_VIEW, "ViewGroup")


	private val filterableClassName = ClassName(Packages.ANDROID_WIDGET,
			"Filterable")

	private var androidFilterClassName = ClassName(Packages.ANDROID_WIDGET,
			"Filter")

	private val itemClickListenerClassName = ClassName(Packages.GENCYCLER_LISTENERS,
			"OnItemClickedListener")

	private val itemLongClickListenerClassName = ClassName(Packages.GENCYCLER_LISTENERS,
			"OnItemLongClickedListener")

	fun generate(adapter: Adapter): FileSpec {

		val adapterFileBuilder = FileSpec.builder(Packages.GENCYCLER, adapter.name)
				.addAnnotation(AnnotationSpec.builder(JvmName::class)
						.useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
						.addMember("\"${adapter.name}\"")
						.build())

		val adapterClassBuilder = TypeSpec.classBuilder(adapter.name)
				.addKdoc(KDocs.ADAPTER_CLASS)

		val adapterConstructor = FunSpec.constructorBuilder()
				.jvmOverloads()
				.addParameter(ParameterSpec
						.builder(Names.CONTEXT, contextClassName)
						.build())


		val setupFunctions = ArrayList<FunSpec>()
		val abstractFunctions = ArrayList<FunSpec>()

		var parametrizedType: TypeName = Parameters.GENERIC_DATA_MODEL
		var parametrizedHolderType = Parameters.VIEW_HOLDER_SUPER_CLASS

		when {

			//When only a single ViewHolder is provided
			adapter.viewTypes.size == 1 -> {

				val viewType = adapter.viewTypes.first()

				val viewHolder = viewType.viewHolderType

				val dataType = viewType.dataContainerType

				parametrizedType = dataType



				parametrizedHolderType = viewHolder


				abstractFunctions
						.addAll(listOf(
								generateAbstractBindFunc(viewType),
								generateAbstractRecycleFunc(viewHolder)))

				setupFunctions
						.addAll(listOf(
								generateCreateViewHolderFunc(viewType),
								generateBindViewHolderFunc(viewType,
										adapter.clickable, adapter.longClickable),
								generateRecycleViewHolderFunc(viewHolder)))
			}

			//When multiple ViewHolders are provided
			adapter.viewTypes.size > 1 -> {
				parametrizedType = Parameters.GENERIC_DATA_MODEL
				parametrizedHolderType = Parameters.VIEW_HOLDER_SUPER_CLASS

				val supportedViewTypesSimpleName = ArrayList<String>()
				val itemViewTypeCodeBuilder = CodeBlock.builder()
				val createViewHolderCodeBuilder = CodeBlock.builder()
				val bindViewHolderCodeBuilder = CodeBlock.builder()
				val recycleViewHolderCodeBuilder = CodeBlock.builder()

				for (viewType in adapter.viewTypes) {
					val viewHolder = viewType.viewHolderType

					val viewHolderEnumName = viewHolder.simpleEnumName
					val viewHolderSimpleName = viewHolder.simpleName

					createViewHolderCodeBuilder
							.insertCreateViewHolderCase(viewType, viewHolderEnumName)

					bindViewHolderCodeBuilder
							.insertBindViewHolderCase(viewType, viewHolderSimpleName)

					itemViewTypeCodeBuilder
							.insertItemViewTypeCase(viewType, viewHolderEnumName)

					recycleViewHolderCodeBuilder
							.insertRecycleViewHolderCase(viewType, viewHolderSimpleName)

					val dataType = viewType.dataContainerType

					supportedViewTypesSimpleName.add(
							when (dataType) {
								is ClassName ->
									dataType.simpleName

								is ParameterizedTypeName ->
									dataType.rawType.simpleName

								else ->
									throw IllegalArgumentException("Unexpected type was found")

							})

					abstractFunctions.addAll(listOf(
							generateAbstractBindFunc(viewType),
							generateAbstractRecycleFunc(viewHolder)))

				}

				setupFunctions.addAll(listOf(
						generateCreateViewHolderFunc(
								codeBlockBuilder = createViewHolderCodeBuilder,
								supportViewTypesNames = supportedViewTypesSimpleName),

						generateBindViewHolderFunc(
								codeBlockBuilder = bindViewHolderCodeBuilder,
								clickable = adapter.clickable,
								longClickable = adapter.longClickable),

						generateItemViewTypeFunc(
								codeBlockBuilder = itemViewTypeCodeBuilder,
								supportViewTypesNames = supportedViewTypesSimpleName),

						generateRecycleViewHolderFunc(
								codeBlockBuilder = recycleViewHolderCodeBuilder)))
			}
		}


		adapterClassBuilder
				.superclass(recyclerAdapterSuperClass
						.plusParameter(parametrizedType)
						.plusParameter(parametrizedHolderType))



		if (adapter.filterable) {
			adapterClassBuilder
					.addSuperinterface(filterableClassName)
					.addProperty(PropertySpec.builder(Names.FILTER,
							Parameters.GENCYCLER_FILTER
									.parameterizedBy(parametrizedType), KModifier.PRIVATE)
							.initializer("""

						%T(elements, { filter, item -> performFilter(filter, item) }, ::replace)
						""".trimIndent(), Parameters.GENCYCLER_FILTER)
							.build())

			setupFunctions.addAll(listOf(
					generateSuperFilterFunc(),
					generateFilterFunc()))

			abstractFunctions.add(generateAbstractPerformFilterFunc(parametrizedType))
		}

		adapterConstructor
				.addParameter(ParameterSpec.builder(Names.ELEMENTS,
						mutableList.parameterizedBy(parametrizedType))
						.defaultValue("ArrayList()")
						.build())
				.addParameter(ParameterSpec.builder(Names.UPDATE_UI, BOOLEAN)
						.defaultValue("true")
						.build())

		if (adapter.clickable) {
			val onClickName = Names.ON_CLICK_LISTENER
			val onClickType = itemClickListenerClassName.parameterizedBy(parametrizedType)

			adapterConstructor
					.addParameter(ParameterSpec
							.builder(onClickName, onClickType)
							.build())

			adapterClassBuilder
					.addProperty(PropertySpec
							.builder(onClickName, onClickType)
							.initializer(onClickName)
							.mutable()
							.build())
		}

		if (adapter.longClickable) {
			val onClickName = Names.ON_LONG_CLICK_LISTENER
			val onClickType = itemLongClickListenerClassName.parameterizedBy(parametrizedType)

			adapterConstructor
					.addParameter(ParameterSpec
							.builder(onClickName, onClickType)
							.build())

			adapterClassBuilder
					.addProperty(PropertySpec.builder(onClickName, onClickType)
							.initializer(onClickName)
							.mutable()
							.build())
					.build()
		}

		return adapterFileBuilder
				.addType(adapterClassBuilder
						.addModifiers(KModifier.ABSTRACT)
						.primaryConstructor(adapterConstructor.build())
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
					.addStatement("return %T(inflate(%T.layout.%L, parent))",
							viewType.viewHolderType, rClass, viewType.layoutName)
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
										   clickable: Boolean = false,
										   longClickable: Boolean = false,
										   codeBlockBuilder: CodeBlock.Builder? = null): FunSpec {

		var elementDeceleration = "elements[position]"
		val bindViewHolderFunctionBuilder = FunSpec.builder(Methods.ON_BIND_VIEW_HOLDER)
				.addModifiers(KModifier.OVERRIDE)

		if (clickable || longClickable || viewType == null) {
			bindViewHolderFunctionBuilder
					.addStatement("val %L = $elementDeceleration", Names.ELEMENT)
					.addStatement("")
			elementDeceleration = Names.ELEMENT
		}

		if (clickable) {
			bindViewHolderFunctionBuilder
					.addStatement("holder.onClicked { %L.onItemClicked(%L, position) }",
							Names.ON_CLICK_LISTENER, Names.ELEMENT)
					.addStatement("")
		}

		if (longClickable) {
			bindViewHolderFunctionBuilder
					.addStatement("holder.onLongClicked { %L.onItemLongClicked(%L, position) }",
							Names.ON_LONG_CLICK_LISTENER, Names.ELEMENT)
					.addStatement("")
		}


		return if (viewType != null) {
			bindViewHolderFunctionBuilder
					.addParameter("holder", viewType.viewHolderType)
					.addParameter(Parameters.POSITION_PARAMETER_SPEC)
					.addStatement("%L%L(holder, %L, position)",
							Methods.ABSTRACT_BIND_CUSTOM,
							viewType.viewHolderType.simpleName,
							elementDeceleration)
					.build()

		} else {
			if (codeBlockBuilder == null) {
				throw IllegalArgumentException("At least one parameter must be not null")
			}
			bindViewHolderFunctionBuilder
					.addParameter("holder", Parameters.VIEW_HOLDER_SUPER_CLASS)
					.addParameter(Parameters.POSITION_PARAMETER_SPEC)
					.beginControlFlow("when (holder)")
					.addCode(codeBlockBuilder.build())
					.endControlFlow()
					.build()
		}
	}

	private fun generateRecycleViewHolderFunc(viewHolder: ClassName? = null,
											  codeBlockBuilder: CodeBlock.Builder? = null): FunSpec {

		val bindViewHolderFunctionBuilder = FunSpec.builder(Methods.ON_VIEW_RECYCLED)
				.addModifiers(KModifier.OVERRIDE)


		return if (viewHolder != null) {
			bindViewHolderFunctionBuilder
					.addParameter("holder", viewHolder)
					.addStatement("%L%L(holder, holder.adapterPosition)",
							Methods.ABSTRACT_RECYCLED_CUSTOM,
							viewHolder.simpleName)
					.build()

		} else {
			if (codeBlockBuilder == null) {
				throw IllegalArgumentException("At least one parameter must be not null")
			}
			bindViewHolderFunctionBuilder
					.addParameter("holder", Parameters.VIEW_HOLDER_SUPER_CLASS)
					.addStatement("val position = holder.adapterPosition")
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

	private fun generateSuperFilterFunc(): FunSpec {
		return FunSpec.builder(Methods.GET_FILTER)
				.addModifiers(KModifier.OVERRIDE)
				.returns(androidFilterClassName)
				.addStatement("return filter")
				.build()
	}

	private fun generateFilterFunc(): FunSpec {
		return FunSpec.builder(Methods.FILTER)
				.addParameter(Names.CONSTRAINT, CharSequence::class)
				.addStatement("filter.filter(%L)", Names.CONSTRAINT)
				.build()
	}

	private fun generateAbstractBindFunc(viewType: ViewType): FunSpec {
		val viewHolder = viewType.viewHolderType
		val dataType = viewType.dataContainerType

		val viewHolderParam = ParameterSpec
				.builder(viewHolder.simpleParameterName, viewHolder)
				.build()

		val dataTypeParam = ParameterSpec
				.builder(dataType.simpleParameterName, dataType)
				.build()

		return FunSpec.builder("${Methods.ABSTRACT_BIND_CUSTOM}${viewHolder.simpleName}")
				.addKdoc(KDocs.ABSTRACT_BIND_FUNC, viewHolder,
						viewHolderParam.name, dataTypeParam.name, viewHolder, dataType)
				.addModifiers(KModifier.ABSTRACT)
				.addParameter(viewHolderParam)
				.addParameter(dataTypeParam)
				.addParameter(Parameters.POSITION_PARAMETER_SPEC)
				.build()
	}

	private fun generateAbstractRecycleFunc(viewHolder: ClassName): FunSpec {

		val viewHolderParam = ParameterSpec
				.builder(viewHolder.simpleParameterName, viewHolder)
				.build()

		return FunSpec.builder("${Methods.ABSTRACT_RECYCLED_CUSTOM}${viewHolder.simpleName}")
				.addKdoc(KDocs.ABSTRACT_RECYCLE_FUNC,
						viewHolder, viewHolderParam.name, viewHolder.simpleName)
				.addModifiers(KModifier.OPEN)
				.addParameter(viewHolderParam)
				.addParameter(Parameters.POSITION_PARAMETER_SPEC)
				.addStatement("return super.onViewRecycled(%L)", viewHolderParam.name)
				.build()
	}

	private fun generateAbstractPerformFilterFunc(typeName: TypeName): FunSpec {
		val dataType = if (typeName is ParameterizedTypeName) {
			typeName.rawType
		} else {
			typeName as ClassName
		}

		val dataTypeParamName = dataType.simpleParameterName

		val dataTypeParam = ParameterSpec
				.builder(dataTypeParamName, dataType)
				.build()

		return FunSpec.builder(Methods.PERFORM_FILTER)
				.addKdoc(KDocs.ABSTRACT_PERFORM_FILTER_FUNC, dataTypeParamName,
						Parameters.GENCYCLER_FILTER, filterableClassName)
				.addModifiers(KModifier.ABSTRACT)
				.returns(BOOLEAN)
				.addParameter(Names.CONSTRAINT, CharSequence::class)
				.addParameter(dataTypeParam)
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
				.addStatement("%L%L(holder, %L as %T, position)",
						Methods.ABSTRACT_BIND_CUSTOM,
						viewHolderSimpleName,
						Names.ELEMENT,
						viewType.dataContainerType)
				.unindent()
				.addStatement("")
	}

	private fun CodeBlock.Builder.insertRecycleViewHolderCase(viewType: ViewType,
															  viewHolderSimpleName: String): CodeBlock.Builder {

		return addStatement("is %T ->", viewType.viewHolderType)
				.indent()
				.addStatement("%L%L(holder, position)",
						Methods.ABSTRACT_RECYCLED_CUSTOM,
						viewHolderSimpleName)
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
