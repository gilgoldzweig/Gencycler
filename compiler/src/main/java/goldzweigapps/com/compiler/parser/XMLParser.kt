package goldzweigapps.com.compiler.parser

import goldzweigapps.com.compiler.consts.Packages
import goldzweigapps.com.compiler.consts.Parameters
import goldzweigapps.com.compiler.consts.widgets
import goldzweigapps.com.compiler.models.ViewField
import goldzweigapps.com.compiler.utils.Logger
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

object XMLParser {
    private val documentFactory = DocumentBuilderFactory.newInstance()
    private val documentBuilder = documentFactory.newDocumentBuilder()

    /**
     * Retrieves an attribute value by a provided name
     *
     * @param xmlFile the source xml file to find the attribute from
     * @param attributeName the name of the attribute
     * @param nodePosition reduce the search to a specific node which is known to include the attribute
     *
     * @return the value of the argument or null if it does not exist
     */
    fun findAttributeByName(xmlFile: File, attributeName: String, nodePosition: Int? = null): String? {
        val attributes = xmlFile.asNode().attributes
        if (attributeName.isEmpty() || attributes.length == 0) return null

        if (nodePosition != null && nodePosition >= 0 && nodePosition < attributes.length) {
            return attributes.item(nodePosition)
                    .attributes
                    .getNamedItem(attributeName)?.nodeValue
        } else {

            for (i in 0 until attributes.length) {
                val node = attributes.item(i)
                val name = node.nodeName
                val value = node.nodeValue
                if (name == attributeName) {
                    return value
                }
            }
            throw Throwable("R class was not found")
        }
    }

    /**
     * Parses the xml file and generate a ViewField for every node that contains a 'android:id' attribute
     *
     * @param xmlFile the source android layout xml file
     * @return a set of ViewField
     * @see ViewField
     */
    fun parseViewFields(xmlFile: File): List<ViewField> =
            xmlFile.asNode().getViewsFromNode()


    private fun Node.getViewsFromNode(existingViews: MutableList<ViewField> = ArrayList()): List<ViewField> {
        toViewField()?.let {
            existingViews.add(it)
        }
        if (hasChildNodes()) {
            for (index in 0 until childNodes.length) {
                childNodes.item(index).getViewsFromNode(existingViews)
            }
        }
        return existingViews
    }

    private fun Node.toViewField(): ViewField? {
        val fieldIdAttribute = attributes
                ?.getNamedItem("android:id")
                ?.nodeValue ?: return null

        return ViewField(fieldIdAttribute.removePrefix("@+id/"), analyzeNodeViewType())
    }


    /**
     * Parses the xml node name
     *
     */
    private fun Node.analyzeNodeViewType(): String {
        return when {
            '.' in nodeName -> //Node has a full package name so we'll use that
                nodeName

            nodeName in widgets -> //Node is part of android widgets we fill in the missing package name
                "${Packages.ANDROID_WIDGET}$nodeName"

            else -> //Node has unknown package we just say it's a View
                Parameters.VIEW_CLASS_NAME.canonicalName

        }
    }

    /**
     * Converts a Xml file to a Xml node
     * @param receiver a Xml file
     *
     * @return the parsed file root node
     */
    private fun File.asNode(): Node {
        val xmlDocument = documentBuilder.parse(this)
        val element = xmlDocument.documentElement
        return element as Node
    }

}