package goldzweigapps.com.compiler.utils

import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element
import javax.tools.Diagnostic.Kind

object Logger {

    private lateinit var processingEnv: ProcessingEnvironment
    private lateinit var messager: Messager
    private var initialized: Boolean = false

    fun init(processingEnvironment: ProcessingEnvironment) {
        synchronized(initialized) {
            if (initialized) return
            synchronized(processingEnvironment) {
                processingEnv = processingEnvironment
                messager = processingEnvironment.messager
                initialized = true
            }
        }
    }

    fun e(message: Any? = null, exception: Throwable? = null,
          element: Element? = null,
          annotationMirror: AnnotationMirror? = null,
          annotationValue: AnnotationValue? = null) {

        messager.printMessage(Kind.ERROR, """
            $message
            ${exception?.message ?: ""}
            """.trimIndent(), element, annotationMirror, annotationValue)
    }

    fun d(message: Any?,
          element: Element? = null,
          annotationMirror: AnnotationMirror? = null,
          annotationValue: AnnotationValue? = null) {

        messager.printMessage(Kind.NOTE, message?.toString() ?: "null",
                element,
                annotationMirror,
                annotationValue)
    }

    fun w(message: Any?,
          element: Element? = null,
          annotationMirror: AnnotationMirror? = null,
          annotationValue: AnnotationValue? = null) {

        messager.printMessage(Kind.WARNING, message?.toString() ?: "null",
                element,
                annotationMirror,
                annotationValue)
    }
}