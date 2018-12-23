package goldzweigapps.com.compiler

import com.google.common.graph.Graph
import com.google.common.graph.GraphBuilder
import com.squareup.kotlinpoet.*
import goldzweigapps.com.annotations.annotations.*
import goldzweigapps.com.compiler.models.Adapter
import goldzweigapps.com.compiler.models.ViewHolder
import goldzweigapps.com.compiler.adapter.getNamingAdapter
import goldzweigapps.com.compiler.finder.AndroidManifestFinder
import goldzweigapps.com.compiler.generators.RecyclerAdapterGenerator
import goldzweigapps.com.compiler.generators.ViewHolderGenerator
import goldzweigapps.com.compiler.models.ViewType
import goldzweigapps.com.compiler.models.asClassName
import goldzweigapps.com.compiler.parser.XMLParser
import goldzweigapps.com.compiler.utils.*
import java.io.File
import java.lang.IllegalArgumentException
import java.lang.annotation.AnnotationTypeMismatchException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.MirroredTypesException
import javax.lang.model.type.TypeMirror
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy


/**
 * Created by gilgoldzweig on 14/10/2017.
 */

class GencyclerProcessor : AbstractProcessor() {

	private lateinit var layoutFolder: File
	private lateinit var manifestFinder: AndroidManifestFinder
	private lateinit var rClass: ClassName
	private lateinit var valueNameLayoutMap: Map<Int, String>

	private lateinit var viewHolderGenerator: ViewHolderGenerator
	private lateinit var recyclerAdapterGenerator: RecyclerAdapterGenerator


	override fun init(p0: ProcessingEnvironment?) {
		super.init(p0)
		if (p0 == null) return
		EnvironmentUtil.init(processingEnv)
		Logger.init(processingEnv)
		manifestFinder = AndroidManifestFinder(processingEnv)
		rClass = manifestFinder.findRClass()
		layoutFolder = FileHelper.findModuleLayoutFolder(processingEnv)
		valueNameLayoutMap = manifestFinder.generateLayoutValueMap(rClass)
		viewHolderGenerator = ViewHolderGenerator(rClass)
		recyclerAdapterGenerator = RecyclerAdapterGenerator(rClass)
	}


	override fun process(annotations: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
		if (roundEnvironment == null) return true
		if (annotations == null || annotations.isEmpty()) return true

		val viewHolders = ArrayList<ViewHolder>()
		val viewTypes = HashMap<String, ViewType>()

		roundEnvironment
				.getElementsAnnotatedWith(GencyclerViewHolder::class.java)
				.forEach {

					val typeElement = it as TypeElement

					val holder = it.getAnnotation(GencyclerViewHolder::class.java)

					val layoutName = valueNameLayoutMap[holder.value]

					var dataTypeCanonicalName = ""

					val dataTypeContainer =
							if (typeElement.typeParameters.isEmpty()) {
								val dataType = typeElement.asClassName()
								dataTypeCanonicalName = dataType.canonicalName
								dataType
							} else {
								val dataType = typeElement.asClassName()
										.parameterizedBy(*typeElement.typeParameters
												.map { STAR }
												.toTypedArray())

								dataTypeCanonicalName = dataType.rawType.canonicalName
								dataType
							}


					val layoutFile = File("$layoutFolder/$layoutName.xml")

					if (!layoutFile.exists()) {
						Logger.e("Layout not found $layoutFile")
						return true
					}

					val viewHolder = ViewHolder("${it.simpleName}ViewHolder",
							layoutName!!,
							getNamingAdapter(holder.namingCase),
							XMLParser.parseViewFields(layoutFile))

					viewHolders.add(viewHolder)

					viewTypes[dataTypeCanonicalName] =
							ViewType(layoutName, dataTypeContainer, viewHolder.asClassName())
				}

		viewHolderGenerator.generate(viewHolders)
				.writeTo(EnvironmentUtil.generateOutputFile(ViewHolderGenerator.FILE_NAME))

		roundEnvironment
				.getElementsAnnotatedWith(GencyclerAdapter::class.java)
				.forEach {

					val adapter = it.getAnnotation(GencyclerAdapter::class.java)

					val clickable = it.getAnnotation(Clickable::class.java) != null
					val longClickable = it.getAnnotation(LongClickable::class.java) != null
					val filterable = it.getAnnotation(Filterable::class.java) != null

					val adapterViewTypes = adapter.holders.map { holderName ->
						viewTypes[holderName] ?: throw IllegalArgumentException("""
                                No generated ViewHolder found for $holderName.
                                Are you sure you it was annotated with GencyclerViewHolder?
                            """.trimIndent())
					}

					val adapterName = if (adapter.customName.isEmpty()) {
						"Generated${it.simpleName}"
					} else {
						adapter.customName
					}

					val generatedAdapter = Adapter(adapterName, adapterViewTypes,
							clickable, longClickable, filterable)

					recyclerAdapterGenerator.generate(generatedAdapter)
							.writeTo(EnvironmentUtil.generateOutputFile(generatedAdapter.name))

				}

		return false
	}

	override fun getSupportedSourceVersion(): SourceVersion =
			SourceVersion.latestSupported()

	override fun getSupportedAnnotationTypes(): Set<String> =
			setOf(GencyclerViewHolder::class.java.canonicalName,
					GencyclerAdapter::class.java.canonicalName,
					Clickable::class.java.canonicalName,
					LongClickable::class.java.canonicalName,
					Filterable::class.java.canonicalName)


	private inline val GencyclerAdapter.holders: List<String>
		get() {
			return try {
				value.map { it.qualifiedName ?: "" }
			} catch (e: MirroredTypesException) {
				e.typeMirrors.map(TypeMirror::toString)
			}
		}
}

