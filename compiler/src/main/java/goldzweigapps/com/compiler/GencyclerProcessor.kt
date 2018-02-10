package goldzweigapps.com.compiler

import com.squareup.kotlinpoet.*
import goldzweigapps.com.annotations.annotations.*
import goldzweigapps.com.annotations.annotations.Holder
import goldzweigapps.com.compiler.generators.Generators
import goldzweigapps.com.compiler.generators.generateExtensionClass
import goldzweigapps.com.compiler.models.ViewHolder
import goldzweigapps.com.compiler.parser.XMLParser
import goldzweigapps.com.compiler.utils.*
import org.w3c.dom.Node
import java.io.File
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
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

    var holdersMap = HashMap<String, List<ViewHolder>>()
    lateinit var rClass: String
    var initialized = false
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

    override fun init(p0: ProcessingEnvironment?) {
        super.init(p0)
        if (p0 == null) return

    }


    override fun process(annotations: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        if (roundEnvironment == null) return true
        if (annotations == null || annotations.isEmpty()) return true

        if (!initialized) EnvironmentUtil.init(processingEnv)
        initialized = true

        generateExtensionClass()
                .writeTo(File(EnvironmentUtil.savePath()).toPath())

        val recyclerAdapterAnnotation = roundEnvironment.getElementsAnnotatedWith(RecyclerAdapter::class.java)
                .first()
                .getAnnotation(RecyclerAdapter::class.java)

        rClass = try {
            rClass
        } catch (e: UninitializedPropertyAccessException) {
            try {
                recyclerAdapterAnnotation.parseRClass()
            } catch (e: Exception) {
                EnvironmentUtil.logError("R class not found are you sure you have a @RecyclerAdapter?")
                return true
            }
        }

        for (holderElement in roundEnvironment.getElementsAnnotatedWith(Holder::class.java)) {

            val xmlParser = XMLParser(layoutFile, classType = holderElement.asType().toString())
            val holder = holderElement.getAnnotation(Holder::class.java)

            holder
                    .parseClasss()
                    .forEach {
                        val holders = holdersMap[it]
                        if (holders == null) {
                            holdersMap[it] = listOf(
                                    try {
                                        xmlParser.parse(holder.layoutName)
                                    } catch (e: Exception) {
                                        e.message?.let(EnvironmentUtil::logError)
                                                ?: e.printStackTrace()
                                        return true
                                    })
                        } else {
                            holdersMap[it] = ArrayList(holders +
                                    try {
                                        xmlParser.parse(holder.layoutName)
                                    } catch (e: Exception) {
                                        e.message?.let(EnvironmentUtil::logError)
                                                ?: e.printStackTrace()
                                        return true
                                    })
                        }
                    }
        }
        holdersMap.forEach {
            startXMLClassConstriction(rClass, if (recyclerAdapterAnnotation.customName.isEmpty())
                "Generated${ClassName.bestGuess(it.key).simpleName()}" else recyclerAdapterAnnotation.customName, it.value)
        }

        return true
    }

    private fun startXMLClassConstriction(rClass: String, name: String, viewHolders: List<ViewHolder>) {
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
            setOf(RecyclerAdapter::class.java.canonicalName, Holder::class.java.canonicalName)


    private fun RecyclerAdapter.parseRClass(): String = try {
        rClass.java.canonicalName
    } catch (mte: MirroredTypeException) {
        mte.typeMirror.toString()
    }

    private fun NamingAdapter.parseNamingAdapterClass(): String = try {
        adapter.java.canonicalName
    } catch (mte: MirroredTypeException) {
        mte.typeMirror.toString()
    }

    private fun Holder.parseClasss(): List<String> =
            try {
                recyclerAdapters.map { it.java.canonicalName }
            } catch (mre: MirroredTypesException) {
                mre.typeMirrors.map { it.toString() }
            }

}

