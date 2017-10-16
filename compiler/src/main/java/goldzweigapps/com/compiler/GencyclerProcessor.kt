package goldzweigapps.com.compiler

import com.squareup.kotlinpoet.*
import goldzweigapps.com.annotations.annotations.GencyclerAdapter
import goldzweigapps.com.annotations.annotations.GencyclerHolder
import goldzweigapps.com.compiler.generators.Generators
import goldzweigapps.com.compiler.utils.*
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement


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
        GenerateRecyclerAdapterImpl()
                .generate()
                .writeTo(File(EnvironmentUtil.savePath()).toPath())
        for (element in roundEnvironment.getElementsAnnotatedWith(GencyclerAdapter::class.java)) {

//           val holders = element.annotationMirrors
//                   .map { it.elementValues }
//                   .map { it. .map { it.value as GencyclerHolderType } }

//           val holderType =  findAnnotationValue(element,
//                    GencyclerHolder::class.java.canonicalName,
//                    GencyclerHolder::class.java.simpleName,
//                    GencyclerHolderType::class.java)
//            EnvironmentUtil.logWarning(holders.toString())
            val gencyclerAdapterAnnotation = element.getAnnotation(GencyclerAdapter::class.java);
            val holders = gencyclerAdapterAnnotation.holders.toList()
            val name = if (gencyclerAdapterAnnotation.customName.isEmpty()) {
                "$FILE_NAME_ADDON${element.simpleName}"
            } else {
                gencyclerAdapterAnnotation.customName
            }
//
            for (holder in holders) {
                EnvironmentUtil.logWarning(holder.classType)
            }
            startClassConstriction(name, holders)
        }
        return false
    }
    private fun startClassConstriction(name: String, holders: List<GencyclerHolder>) {
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
                .addFunction(Generators.ItemCountGenerator())
                if (holders.isNotEmpty()) {
                    classBuilder.addFunction(Generators.ItemViewTypeGenerator(holders))
                    classBuilder.addTypes(Generators.ViewHoldersGenerator(holders))
                    classBuilder.addFunction(Generators.OnCreateViewHolderGenerator(holders))
                    classBuilder.addFunctions(Generators.OnBindAbstractViewHolderGenerator(holders))
                    classBuilder.addFunction(Generators.OnBindViewHolderGenerator(holders))
                }

        FileSpec.builder(PACKAGE_NAME, name)
                .addType(classBuilder.build())
                .build()
//                .writeTo(System.out)
                .writeTo(File(EnvironmentUtil.savePath()).toPath())
    }
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun getSupportedAnnotationTypes(): Set<String> =
            setOf(GencyclerAdapter::class.java.canonicalName)

}