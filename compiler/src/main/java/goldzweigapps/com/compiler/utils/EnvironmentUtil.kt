package goldzweigapps.com.compiler.utils

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

object EnvironmentUtil {
    private lateinit var processingEnvironment: ProcessingEnvironment
    private var initialize = false


    fun init(environment: ProcessingEnvironment) {
        processingEnvironment = environment
        initialize = true
        logWarning("Test Test")
    }

    fun logError(message: String) {
        if (!initialize) return
        processingEnvironment.messager.printMessage(Diagnostic.Kind.ERROR, message)
    }

    fun logWarning(message: String) {
        if (!initialize) return
        processingEnvironment.messager.printMessage(Diagnostic.Kind.WARNING, message)
    }
    fun savePath() : String {
        if (!initialize) return ""
        val generatedPath = processingEnvironment.options["kapt.kotlin.generated"]
        return generatedPath
                ?.replace("(.*)tmp(/kapt/debug/)kotlinGenerated".toRegex(), "$1generated/source$2")!!
                .replace("kaptKotlin", "kapt")
    }

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