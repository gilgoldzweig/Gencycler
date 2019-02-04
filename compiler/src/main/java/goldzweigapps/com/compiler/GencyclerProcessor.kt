package goldzweigapps.com.compiler

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.asClassName
import goldzweigapps.com.annotations.annotations.*
import goldzweigapps.com.compiler.adapter.getNamingAdapter
import goldzweigapps.com.compiler.finder.AndroidManifestFinder
import goldzweigapps.com.compiler.generators.ListenerGenerator
import goldzweigapps.com.compiler.generators.RecyclerAdapterGenerator
import goldzweigapps.com.compiler.generators.ViewHolderGenerator
import goldzweigapps.com.compiler.models.Adapter
import goldzweigapps.com.compiler.models.ViewHolder
import goldzweigapps.com.compiler.models.ViewType
import goldzweigapps.com.compiler.models.asClassName
import goldzweigapps.com.compiler.parser.XMLParser
import goldzweigapps.com.compiler.utils.EnvironmentUtil
import goldzweigapps.com.compiler.utils.FileHelper
import goldzweigapps.com.compiler.utils.Logger
import java.io.File
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypesException
import javax.lang.model.type.TypeMirror


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
    private val listenerGenerator: ListenerGenerator = ListenerGenerator()


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

        val globalActions = HashSet<Actions>()

        roundEnvironment
                .getElementsAnnotatedWith(GencyclerViewHolder::class.java)
                .forEach {

                    val typeElement = it as TypeElement

                    val holder = it.getAnnotation(GencyclerViewHolder::class.java)

                    val layoutName = valueNameLayoutMap[holder.value]

                    val dataTypeCanonicalName: String

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

                    val actions = it.getAnnotation(GencyclerActions::class.java)?.value ?: emptyArray()

                    viewHolders.add(viewHolder)

                    viewTypes[dataTypeCanonicalName] =
                            ViewType(layoutName, dataTypeContainer, viewHolder.asClassName(), actions)
                }


        viewHolderGenerator.generate(viewHolders)
                .writeTo(EnvironmentUtil.generateOutputFile(ViewHolderGenerator.FILE_NAME))

        roundEnvironment
                .getElementsAnnotatedWith(GencyclerAdapter::class.java)
                .forEach {

                    val adapter = it.getAnnotation(GencyclerAdapter::class.java)

                    val actionAnnotation = it.getAnnotation(GencyclerActions::class.java)

                    val actions = actionAnnotation?.value ?: emptyArray()


                    var actionsFoundInTypes = false
                    val adapterViewTypes = adapter.holders.map { holderName ->
                        val viewType = viewTypes[holderName] ?: throw IllegalArgumentException("""
                                No generated ViewHolder found for $holderName.
                                Are you sure you it was annotated with GencyclerViewHolder?
                            """.trimIndent())

                        actionsFoundInTypes = actionsFoundInTypes || viewType.actions.isNotEmpty()

                        viewType.copy(
                                layoutName = viewType.layoutName,
                                viewHolderType = viewType.viewHolderType,
                                dataContainerType = viewType.dataContainerType,
                                actions = viewType.actions + actions)
                    }


                    val adapterName = if (adapter.customName.isEmpty()) {
                        "Generated${it.simpleName}"
                    } else {
                        adapter.customName
                    }

                    val packageName = EnvironmentUtil.getPackgeName(it)

                    val generatedAdapter = Adapter(adapterName,
                            packageName,
                            adapterViewTypes, actions)

                    val adapterFile = EnvironmentUtil.generateOutputFile(generatedAdapter.name)

                    recyclerAdapterGenerator.generate(generatedAdapter)
                            .writeTo(adapterFile)

                    Logger.w(adapterViewTypes)
                    if (actionsFoundInTypes || actionAnnotation != null) {
                        listenerGenerator.generate(packageName, adapterViewTypes)
                                .writeTo(adapterFile)
                    }

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
                    Filterable::class.java.canonicalName,
                    GencyclerActions::class.java.canonicalName)


    private inline val GencyclerAdapter.holders: List<String>
        get() {
            return try {
                value.map { it.qualifiedName ?: "" }
            } catch (e: MirroredTypesException) {
                e.typeMirrors.map(TypeMirror::toString)
            }
        }
}

