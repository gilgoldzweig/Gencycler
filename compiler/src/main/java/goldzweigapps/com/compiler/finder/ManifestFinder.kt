package goldzweigapps.com.compiler.finder

import goldzweigapps.com.compiler.utils.EnvironmentUtil
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.TypeKind
import javax.lang.model.util.ElementFilter
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Created by gilgoldzweig on 11/03/2018.
 */
class ManifestFinder(processingEnvironment: ProcessingEnvironment) {
    private val manifestFile: File by lazy {
        val filer = processingEnvironment.filer
        val dummySourceFile = filer.createSourceFile("dummy${System.currentTimeMillis()}")
        var dummySourceFilePath = dummySourceFile.toUri().toString()
        if (dummySourceFilePath.startsWith("file:")) {
            if (!dummySourceFilePath.startsWith("file://")) {
                dummySourceFilePath = dummySourceFilePath.substring("file:".length)
            }
        }
        val dummyFile = File(dummySourceFilePath)
        val projectRoot = dummyFile.parentFile.parentFile.parentFile.parentFile.parentFile.parentFile
        File("${projectRoot.absoluteFile}/src/main/AndroidManifest.xml")
    }

    private val factory = DocumentBuilderFactory.newInstance()
    private val builder = factory.newDocumentBuilder()
    private val doc = builder.parse(manifestFile)
    private val element = doc.documentElement
    private val attributes = element.attributes

    fun findRClass(): String {
        for (i in 0 until attributes.length) {
            val node = attributes.item(i)
            val name = node.nodeName
            val value = node.nodeValue
            if (name == "package") {
                return "$value.R"
            }
        }
        throw Throwable("R class was not found")
    }

    fun buildLayoutValueNameMap(rClass: String): Map<Int, String> {
        val valueNameMap = HashMap<Int, String>()
        val elements = EnvironmentUtil.elementUtils().getTypeElement("$rClass.layout").enclosedElements

        ElementFilter.fieldsIn(elements)?.forEach {
            val fieldType = it.asType().kind
            if (fieldType.isPrimitive && fieldType == TypeKind.INT) {
                valueNameMap[it.constantValue as Int] = it.simpleName.toString()
            }
        }
        return valueNameMap
    }
}

