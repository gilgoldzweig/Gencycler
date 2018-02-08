package goldzweigapps.com.compiler

import com.squareup.kotlinpoet.*
import goldzweigapps.com.annotations.annotations.*
import goldzweigapps.com.annotations.annotations.experimental.GencyclerHolder
import goldzweigapps.com.annotations.annotations.experimental.GencyclerHolderImpl
import goldzweigapps.com.compiler.generators.Generators
import goldzweigapps.com.compiler.generators.generateExtensionClass
import goldzweigapps.com.compiler.utils.*
import java.io.File
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.MirroredTypesException
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * Created by gilgoldzweig on 14/10/2017.
 */

class GencyclerProcessor : AbstractProcessor() {
    var holdersMap = HashMap<String, List<GencyclerHolderImpl>>()
    lateinit var rClass: String
    var round = -1
    override fun process(annotations: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        if (roundEnvironment == null) return true
        if (annotations == null) return true
        round++
        if (round == 0) EnvironmentUtil.init(processingEnv)
        generateExtensionClass()
                .writeTo(File(EnvironmentUtil.savePath()).toPath())

        rClass = try {
            rClass
        } catch (e: UninitializedPropertyAccessException) {
            try {
               roundEnvironment.getElementsAnnotatedWith(GencyclerAdapter::class.java)
                        .first()
                        .getAnnotation(GencyclerAdapter::class.java)
                        .parseRClass()
            } catch (e: Exception) {
                EnvironmentUtil.logError(e.message ?: "")
                throw Throwable("R class not found")
            }
        }

        for (holderElement in roundEnvironment.getElementsAnnotatedWith(GencyclerHolder::class.java)) {
            val holder = holderElement.getAnnotation(GencyclerHolder::class.java)
            holder.parseClasss().forEach {
                val holders = holdersMap[it]
                if (holders == null) {
                    holdersMap.put(it, listOf(GencyclerHolderImpl(holder.layoutName, holderElement.asType().toString(), rClass)))
                } else {
                    holdersMap.put(it, ArrayList(holders + GencyclerHolderImpl(holder.layoutName, holderElement.asType().toString(), rClass)))
                }
            }
        }
        holdersMap.forEach {
            startXMLClassConstriction(ClassName.bestGuess(it.key).simpleName(), it.value.map {
                        GencyclerHolderImpl(it.layoutName,
                                parseLayoutFileHack(it.layoutName),
                                it.dataType,
                                it.rClass)
            })
        }
        for (adapter in holdersMap.keys) {
            holdersMap[adapter]?.let {

            }
        }
        return false
    }

    private fun startXMLClassConstriction(name: String, viewHolders: List<goldzweigapps.com.annotations.annotations.GencyclerHolderImpl>) {
        val generator = Generators(viewHolders)
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
            setOf(GencyclerAdapter::class.java.canonicalName, GencyclerHolder::class.java.canonicalName)



    private fun GencyclerAdapter.parseRClass(): String = try {
        rClass.java.canonicalName
    } catch (mte: MirroredTypeException) {
        mte.typeMirror.toString()
    }

    private fun GencyclerHolder.parseClasss(): List<String> =
            try {
                EnvironmentUtil.logWarning(Arrays.toString(adapters))
                adapters.map { it.java.canonicalName }
            } catch (mre: MirroredTypesException) {
                mre.typeMirrors.map { it.toString() }
            }


    @Throws(Exception::class)
    private fun findLayouts(): File {
        return lazy {
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
        }.value
    }

    private fun parseLayoutFileHack(fileName: String): List<GencyclerViewImpl> {
        val layouts = findLayouts().listFiles()
        val layoutFile = try {
            layouts.firstOrNull { it.nameWithoutExtension == fileName }
        } catch (e: Exception) {
            layouts.firstOrNull { it.name == fileName }
        } ?: throw Throwable("file was not found: $fileName")
        val views = ArrayList<GencyclerViewImpl>()

        with(layoutFile) {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = builder.parse(this)
            val element = doc.documentElement
            val nodes = element.childNodes
            for (i in 0 until nodes.length) {
                val node = nodes.item(i)
                if (node.hasAttributes() && node.attributes.getNamedItem("android:id") != null) {
                    with(node.attributes) {
                        val id = getNamedItem("android:id").nodeValue
                        val viewType = findViewType(node.nodeName)
                        val fieldName = id.convertIdStringToFieldName()
                        val resId = id.removeIdStringPrefix()
                        val view = GencyclerViewImpl(fieldName,
                                resId,
                                viewType)
                        views += view
                    }
                }
            }
        }
        return views
    }

    private fun findViewType(nodeName: String): String {
        if (nodeName.contains('.')) return nodeName
        return try {
            ClassName.bestGuess("android.widget.$nodeName").canonicalName
        } catch (e: Exception) {
            "android.view.View"
        }
    }

    private fun String.convertIdStringToFieldName(): String {
        var id = removeIdStringPrefix()
        var name = ""
        if (id.isNotEmpty()) {
            if (id[0].isUpperCase()) {
                id = id.replace(id[0], id[0].toLowerCase())
            }
            var capitalNext = false
            for (char in id) {
                if (char == '-' || char == '_') {
                    capitalNext = true
                } else {
                    name += if (capitalNext) char.toUpperCase() else char
                    capitalNext = false
                }
            }
        }
        return name
    }

    private fun String.removeIdStringPrefix() = removePrefix("@+id/")
    fun Any.isInitialized() = try {
        true
    } catch (e: UninitializedPropertyAccessException) {
        false
    }
}

