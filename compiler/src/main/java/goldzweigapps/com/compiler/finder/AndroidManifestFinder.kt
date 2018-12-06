/**
 * Copyright (C) 2010-2016 eBusiness Information, Excilys Group
 * Copyright (C) 2016-2018 the AndroidAnnotations project
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package goldzweigapps.com.compiler.finder

import com.squareup.kotlinpoet.ClassName
import goldzweigapps.com.compiler.parser.XMLParser
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.util.ArrayList
import java.util.Arrays
import java.util.Collections
import java.util.HashMap
import java.util.Properties
import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.lang.model.util.Elements
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

import goldzweigapps.com.compiler.models.Option
import goldzweigapps.com.compiler.utils.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.TypeKind
import javax.lang.model.util.ElementFilter

class AndroidManifestFinder(private val environment: ProcessingEnvironment) {


    @Throws(FileNotFoundException::class)
    fun findManifestFile(): File? {
        val androidManifestFile = EnvironmentUtil.getOptionValue(Options.OPTION_MANIFEST)
        return if (androidManifestFile != null) {
            findManifestInSpecifiedPath(androidManifestFile)
        } else {
            findManifestInKnownPaths()
        }
    }

    @Throws(FileNotFoundException::class)
    fun findRClass(): ClassName {
        val fileNotFoundException = FileNotFoundException("R class was not found")
        val manifestFile = findManifestFile() ?: throw fileNotFoundException

        val packageName =
                XMLParser.findAttributeByName(manifestFile,
                        "package") ?: throw fileNotFoundException

        return ClassName(packageName, "R")
    }

    fun generateLayoutValueMap(rClass: ClassName): Map<Int, String> {
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

    @Throws(FileNotFoundException::class)
    private fun findManifestInSpecifiedPath(androidManifestPath: String): File {
        val androidManifestFile = File(androidManifestPath)
        if (!androidManifestFile.exists()) {
            Logger.e("Could not find the AndroidManifest.xml file in specified path: $androidManifestPath")
            throw FileNotFoundException()
        } else {
            Logger.d("AndroidManifest.xml file found with specified path: $androidManifestFile")
        }
        return androidManifestFile
    }

    @Throws(FileNotFoundException::class)
    private fun findManifestInKnownPaths(): File? {
        val (_, sourcesGenerationFolder) = FileHelper.findRootProjectHolder(environment)
        return findManifestInKnownPathsStartingFromGenFolder(sourcesGenerationFolder.absolutePath)
    }

    @Throws(FileNotFoundException::class)
    internal fun findManifestInKnownPathsStartingFromGenFolder(sourcesGenerationFolder: String): File? {

        val strategies = listOf(GradleAndroidManifestFinderStrategy(environment, sourcesGenerationFolder),
                MavenAndroidManifestFinderStrategy(sourcesGenerationFolder),
                EclipseAndroidManifestFinderStrategy(sourcesGenerationFolder))

        var applyingStrategy: AndroidManifestFinderStrategy? = null

        for (strategy in strategies) {
            if (strategy.applies()) {
                applyingStrategy = strategy
                break
            }
        }

        var androidManifestFile: File? = null

        if (applyingStrategy != null) {
            androidManifestFile = applyingStrategy.findAndroidManifestFile()
        }

        if (androidManifestFile != null) {
            Logger.d("""
                ${applyingStrategy!!.name} AndroidManifest.xml file found
                using generation folder $sourcesGenerationFolder: $androidManifestFile
            """.trimIndent())
        } else {
            Logger.e("""
                Could not find the AndroidManifest.xml file,
                using generation folder $sourcesGenerationFolder
            """.trimIndent())
        }

        return androidManifestFile
    }

}

