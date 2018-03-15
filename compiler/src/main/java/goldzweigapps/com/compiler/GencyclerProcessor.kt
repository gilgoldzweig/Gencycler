package goldzweigapps.com.compiler

import com.squareup.kotlinpoet.*
import goldzweigapps.com.annotations.annotations.Holder
import goldzweigapps.com.annotations.annotations.RecyclerAdapter
import goldzweigapps.com.annotations.annotations.UniqueString
import goldzweigapps.com.compiler.finder.ManifestFinder
import goldzweigapps.com.compiler.generators.Generators
import goldzweigapps.com.compiler.generators.generateExtensionClass
import goldzweigapps.com.compiler.models.ViewHolder
import goldzweigapps.com.compiler.parser.XMLParser
import goldzweigapps.com.compiler.utils.*
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypesException
import kotlin.collections.ArrayList


/**
 * Created by gilgoldzweig on 14/10/2017.
 */

class GencyclerProcessor : AbstractProcessor() {
    var holdersMap = HashMap<String, List<ViewHolder>>()
    val layoutFile: File by lazy {
        val filer = processingEnv.filer
        val dummySourceFile = filer.createSourceFile("dummy${System.currentTimeMillis()}")
        var dummySourceFilePath = dummySourceFile.toUri().toString()
        if (dummySourceFilePath.startsWith("file:")) {
            if (!dummySourceFilePath.startsWith("file://")) {
                dummySourceFilePath = dummySourceFilePath.substring("file:".length)
            }
        }
        val dummyFile = File(dummySourceFilePath)
        val projectRoot = dummyFile.parentFile.parentFile.parentFile.parentFile.parentFile.parentFile
        File("${projectRoot.absoluteFile}/src/main/res/layout")

    }

    private lateinit var manifestFinder: ManifestFinder
    private lateinit var rClass: String
    private lateinit var valueNameLayoutMap: Map<Int, String>

    override fun init(p0: ProcessingEnvironment?) {
        super.init(p0)
        if (p0 == null) return
        EnvironmentUtil.init(processingEnv)
        manifestFinder = ManifestFinder(processingEnv)
        rClass = manifestFinder.findRClass()
        valueNameLayoutMap = manifestFinder.buildLayoutValueNameMap(rClass)
    }


    override fun process(annotations: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        if (roundEnvironment == null) return true
        if (annotations == null || annotations.isEmpty()) return true

        generateExtensionClass()
                .writeTo(File(EnvironmentUtil.savePath()).toPath())
        val recyclerAdapterAnnotationsNames = HashMap<String, RecyclerAdapter>()

        roundEnvironment.getElementsAnnotatedWith(RecyclerAdapter::class.java)
                .forEach {
                    recyclerAdapterAnnotationsNames[it.simpleName.toString()] =
                            it.getAnnotation(RecyclerAdapter::class.java)
                }

        for (holderElement in roundEnvironment.getElementsAnnotatedWith(Holder::class.java)) {
            val xmlParser = XMLParser(layoutFile, classType = holderElement.asType().toString())
            val holder = holderElement.getAnnotation(Holder::class.java)
            val uniqueDeclaredElements = holderElement.enclosedElements
                    .firstOrNull {
                        it.kind.isField && it.getAnnotation(UniqueString::class.java) != null
                    }

            val isUniqueAnnotationDeclared = uniqueDeclaredElements != null
            val uniqueFieldName = uniqueDeclaredElements?.simpleName?.toString()

            holder
                    .parseClasss()
                    .forEach {
                        val holders = holdersMap[it]
                        if (holders == null) {
                            holdersMap[it] = listOf(
                                    try {
                                        xmlParser.parse("${valueNameLayoutMap[holder.layoutRes]}",
                                                isUniqueAnnotationDeclared, uniqueFieldName, holder.uniqueString)
                                    } catch (e: Exception) {
                                        e.message?.let(EnvironmentUtil::logError)
                                                ?: e.printStackTrace()
                                        return true
                                    })
                        } else {
                            holdersMap[it] = ArrayList(holders +
                                    try {
                                        xmlParser.parse("${valueNameLayoutMap[holder.layoutRes]}",
                                                isUniqueAnnotationDeclared, uniqueFieldName, holder.uniqueString)
                                    } catch (e: Exception) {
                                        e.message?.let(EnvironmentUtil::logError)
                                                ?: e.printStackTrace()
                                        return true
                                    })
                        }
                    }
        }
        holdersMap.forEach {
            val className = ClassName.bestGuess(it.key).simpleName()
            val recyclerAdapter = recyclerAdapterAnnotationsNames[className]
            val customName = recyclerAdapter?.customName ?: ""

            startXMLClassConstriction(if (customName.isEmpty()) "Generated$className" else customName, it.value)
        }

        return true
    }

    private fun startXMLClassConstriction(name: String, viewHolders: List<ViewHolder>) {
        val generator = Generators(ClassName.bestGuess(rClass), viewHolders)

        val classBuilder = TypeSpec.classBuilder(name)
                .addModifiers(KModifier.ABSTRACT)
                .primaryConstructor(FunSpec.constructorBuilder()
                        .addParameter(ParameterSpec.builder("val context", context).build())
                        .addParameter(ParameterSpec.builder("elements", elements)
                                .defaultValue("ArrayList()").build())
                        .build())
                .superclass(recyclerAdapterExtensionImpl)
                .addSuperclassConstructorParameter("elements")
                .addProperty(LayoutInflaterProperty)

        if (viewHolders.isNotEmpty()) {
            with(generator) {
                classBuilder.addFunction(generateOnCreateViewHolder())
                classBuilder.addFunction(generateOnBindViewHolder())
                classBuilder.addFunction(generateItemCount())
                classBuilder.addFunction(generateItemViewType())
                classBuilder.addFunctions(generateOnBindAbstractViewHolder())
                classBuilder.addTypes(generateViewHolders())
            }
        }

        FileSpec.builder(PACKAGE_NAME, name)
                .addType(classBuilder.build())
                .indent("   ")
                .build()
                .writeTo(File(EnvironmentUtil.savePath()).toPath())
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun getSupportedAnnotationTypes(): Set<String> =
            setOf(RecyclerAdapter::class.java.canonicalName,
                    Holder::class.java.canonicalName,
                    UniqueString::class.java.canonicalName)


    private fun Holder.parseClasss(): List<String> =
            try {
                recyclerAdapters.map { it.java.canonicalName }
            } catch (mre: MirroredTypesException) {
                mre.typeMirrors.map { it.toString() }
            }

}

