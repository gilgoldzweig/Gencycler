package goldzweigapps.com.compiler

import com.squareup.kotlinpoet.*
import goldzweigapps.com.annotations.annotations.*
import goldzweigapps.com.compiler.generators.Generator
import goldzweigapps.com.compiler.generators.generateExtensionClass
import goldzweigapps.com.compiler.utils.*
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException


/**
 * Created by gilgoldzweig on 14/10/2017.
 */

/**
 * Created by gilgoldzweig on 12/10/2017.
 */
//@AutoService(Processor::class)
class GencyclerProcessor: AbstractProcessor() {
    var round = -1
    override fun process(annotations: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        if (roundEnvironment == null) return true
        if (annotations == null) return true
        round++
        if (round == 0) EnvironmentUtil.init(processingEnv)
        generateExtensionClass()
                .writeTo(File(EnvironmentUtil.savePath()).toPath())

        for (element in roundEnvironment.getElementsAnnotatedWith(GencyclerAdapter::class.java)) {

            val gencyclerAdapterAnnotation = element.getAnnotation(GencyclerAdapter::class.java)
            val name = if (gencyclerAdapterAnnotation.customName.isEmpty()) {
                "$FILE_NAME_ADDON${element.simpleName}"
            } else {
                gencyclerAdapterAnnotation.customName
            }

            startClassConstriction(name, gencyclerAdapterAnnotation.viewHolders.mapToImpl())
        }
        return false
    }

    private fun startClassConstriction(name: String, viewHolders: List<GencyclerViewHolderImpl>) {
        val generator = Generator(viewHolders)
        val classBuilder = TypeSpec.classBuilder(name)
                .addModifiers(KModifier.ABSTRACT)
                .primaryConstructor(FunSpec.constructorBuilder()
                        .addParameter(ParameterSpec.builder("val context", context).build())
                        .addParameter(ParameterSpec.builder("var elementList", elements)
                                .defaultValue("ArrayList()").build())
                        .build())
                .superclass(recyclerAdapterExtensionImpl)
                .addSuperclassConstructorParameter("elementList")
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
            setOf(GencyclerAdapter::class.java.canonicalName)

    private fun GencyclerViewHolder.parseClass(): String = try {
        classType.java.canonicalName
    } catch (mte: MirroredTypeException) {
        mte.typeMirror.toString()
    }
    private fun GencyclerViewField.parseClass(): String = try {
        viewType.java.canonicalName
    } catch (mte: MirroredTypeException) {
        mte.typeMirror.toString()
    }

    private fun Array<out GencyclerViewField>.mapToImpl() =
        map { GencyclerViewFieldImpl(it.name,
                it.resId,
                it.parseClass()) }
                .toTypedArray()

    private fun Array<out GencyclerViewHolder>.mapToImpl() =
           map {  GencyclerViewHolderImpl(it.layoutResId,
                   *it.viewFields.mapToImpl(),
                   classType = it.parseClass()) }
}