package goldzweigapps.com.compiler.utils

import goldzweigapps.com.compiler.consts.Packages
import goldzweigapps.com.compiler.models.Option
import java.io.File
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic
import javax.tools.StandardLocation

object EnvironmentUtil {
    private lateinit var processingEnvironment: ProcessingEnvironment
    private lateinit var filer: Filer
    private lateinit var options: Options
    private var initialize = false


    fun init(environment: ProcessingEnvironment) {
        processingEnvironment = environment
        filer = processingEnvironment.filer
        options = Options(processingEnvironment)
        initialize = true
    }

    fun getOptionValue(option: Option): String? =
            options[option]

    fun getOptionBoolean(option: Option): Boolean =
            options.getBoolean(option)



    fun generateOutputFile(fileSimpleName: String): File =
        File(filer.getResource(StandardLocation.SOURCE_OUTPUT,"", "$fileSimpleName.kt")
                .toUri())

    fun getPackgeName(element: Element): String =
        elementUtils().getPackageOf(element).toString()


    fun isSerializable(typeMirror: TypeMirror): Boolean {
        if (!initialize) return false
        val serializable = processingEnvironment.elementUtils
                .getTypeElement("java.io.Serializable").asType()
        return processingEnvironment.typeUtils.isAssignable(typeMirror, serializable)
    }

    fun isParcelable(typeMirror: TypeMirror): Boolean {
        if (!initialize) return false
        val parcelable = processingEnvironment.elementUtils
                .getTypeElement("android.os.Parcelable").asType()
        return processingEnvironment.typeUtils.isAssignable(typeMirror, parcelable)
    }
    fun typeUtils(): Types? {
        if (!initialize) return null
        return processingEnvironment.typeUtils
    }
    fun elementUtils(): Elements = processingEnvironment.elementUtils

}