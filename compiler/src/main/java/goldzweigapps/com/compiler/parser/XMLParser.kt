package goldzweigapps.com.compiler.parser

import com.squareup.kotlinpoet.ClassName
import goldzweigapps.com.compiler.adapter.CamelCaseNamingAdapter
import goldzweigapps.com.compiler.adapter.NamingAdapter
import goldzweigapps.com.compiler.models.View
import goldzweigapps.com.compiler.models.ViewHolder
import org.w3c.dom.Node
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Created by gilgoldzweig on 09/02/2018.
 */
class XMLParser(private val layoutFile: File,
                override val namingAdapter: NamingAdapter = CamelCaseNamingAdapter(),
                override val classType: String) : Parser {


    @Throws(Exception::class)
    override fun parse(layoutName: String): ViewHolder {
        val layouts = layoutFile.listFiles()
        val layoutFile = try {
            layouts.firstOrNull { it.nameWithoutExtension == layoutName }
        } catch (e: Exception) {
            layouts.firstOrNull { it.name == layoutName }
        } ?: throw Throwable("Layout was not found: $layoutName, are you sure you wrote it correctly?")
        val views = ArrayList<View>()

        with(layoutFile) {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = builder.parse(this)
            val element = doc.documentElement
            val nodes = element.childNodes
            for (i in 0 until nodes.length) {
                val node = nodes.item(i)
                buildViewFromNode(views, node)
            }
        }
        return ViewHolder(layoutName, views, classType)
    }

    private fun buildViewFromNode(views: ArrayList<View>, node: Node) {
        if (node.hasAttributes() && node.attributes.getNamedItem("android:id") != null) {
            with(node.attributes) {
                val id = getNamedItem("android:id").nodeValue.removePrefix("@+id/")
                val viewType = findViewType(node.nodeName)
                val fieldName = namingAdapter.buildNameForId(id)
                views += View(fieldName, id, viewType)
            }
        }
        if (node.hasChildNodes()) {
            val nodes = node.childNodes
            for (i in 0 until nodes.length) {
                buildViewFromNode(views, nodes.item(i))
            }
        }
    }

    private fun findViewType(nodeName: String): String {
        if (nodeName.contains('.')) return nodeName
        return try {
            ClassName.bestGuess("android.widget.$nodeName").canonicalName
        } catch (e: Exception) {
            "android.view.View"
        }
    }

}