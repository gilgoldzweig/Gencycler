package goldzweigapps.com.compiler.finder

import goldzweigapps.com.compiler.utils.Logger
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.annotation.processing.ProcessingEnvironment

sealed class AndroidManifestFinderStrategy(internal val name: String, sourceFolderPattern: Pattern,
                                           sourceFolder: String) {

    internal val matcher: Matcher = sourceFolderPattern.matcher(sourceFolder)

    internal fun findAndroidManifestFile(): File? {
        for (location in possibleLocations()) {
            val manifestFile = File(matcher.group(1), "$location/AndroidManifest.xml")
            if (manifestFile.exists()) {
                return manifestFile
            }
        }
        return null
    }

    internal fun applies(): Boolean =
            matcher.matches()

    internal abstract fun possibleLocations(): Iterable<String>
}

class GradleAndroidManifestFinderStrategy(private val environment: ProcessingEnvironment,
                                          sourceFolder: String) :
        AndroidManifestFinderStrategy("Gradle", GRADLE_GEN_FOLDER, sourceFolder) {

    private val directories = listOf("build/intermediates/manifests/full",
            "build/intermediates/bundles",
            "build/intermediates/manifests/aapt")

    override fun possibleLocations(): Iterable<String> {
        val path = matcher.group(1)
        val mode = matcher.group(2)
        val gradleVariant = matcher.group(3)
        val variantPart = gradleVariant.substring(1)

        val possibleLocations = ArrayList<String>()
        findPossibleLocationsV32(path, variantPart, possibleLocations)

        for (directory in directories) {
            findPossibleLocations(path, directory, variantPart, possibleLocations)
        }

        return updateLocations(path, possibleLocations)
    }

    private fun updateLocations(path: String, possibleLocations: List<String>): List<String> {
        val knownLocations = ArrayList<String>()
        for (location in possibleLocations) {
            val expectedLocation = "$path/$location"
            val file = File("$expectedLocation/output.json")
            if (file.exists()) {
                val matcher = OUTPUT_JSON_PATTERN.matcher(readJsonFromFile(file))
                if (matcher.matches()) {
                    val relativeManifestPath = matcher.group(1)
                    val manifestFile = File("$expectedLocation/$relativeManifestPath")
                    val manifestDirectory = manifestFile.parentFile.absolutePath
                    knownLocations.add(manifestDirectory.substring(path.length))
                }
            }
        }

        if (knownLocations.isEmpty()) {
            knownLocations.addAll(possibleLocations)
        }

        return knownLocations
    }

    private fun readJsonFromFile(file: File): String {
        return try {
            BufferedReader(FileReader(file))
                    .use(BufferedReader::readLine)
        } catch (e: IOException) {
            Logger.e("Unable to read json file: $file", e)
            ""
        }

    }

    private fun findPossibleLocationsV32(basePath: String, variantPart: String, possibleLocations: MutableList<String>) {
        var variantPart = variantPart
        val directories = File(basePath + BUILD_TOOLS_V32_MANIFEST_PATH).list() ?: return

        if (variantPart.startsWith("/") || variantPart.startsWith("\\")) {
            variantPart = variantPart.substring(1)
        }


        val variantParts = variantPart.split("[/\\\\]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (variantParts.size > 1) {
            val sb = StringBuilder(variantParts[0])
            for (i in 1 until variantParts.size) {
                val part = variantParts[i]
                sb.append(upperCaseFirst(part))
            }
            variantPart = sb.toString()
        }

        var possibleLocation = "$BUILD_TOOLS_V32_MANIFEST_PATH/$variantPart"

        findPossibleLocations(basePath, possibleLocations, possibleLocation)
        findPossibleLocations(basePath, possibleLocations, possibleLocation + "/process" + upperCaseFirst(variantPart) + "Manifest/merged")
    }

    private fun findPossibleLocations(basePath: String, possibleLocations: MutableList<String>, possibleLocationWithProcessManifest: String) {
        if (File(basePath, possibleLocationWithProcessManifest).isDirectory) {
            possibleLocations.add(possibleLocationWithProcessManifest)
            addPossibleSplitLocations(basePath, possibleLocationWithProcessManifest, possibleLocations)
        }
    }

    private fun findPossibleLocations(basePath: String, targetPath: String, variantPart: String, possibleLocations: MutableList<String>) {
        var variantPart = variantPart
        val directories = File(basePath + targetPath).list() ?: return

        if (variantPart.startsWith("/") || variantPart.startsWith("\\")) {
            variantPart = variantPart.substring(1)
        }

        for (directory in directories) {
            val possibleLocation = "$targetPath/$directory"
            val variantDir = File(basePath + possibleLocation)
            if (variantDir.isDirectory && variantPart.toLowerCase().startsWith(directory.toLowerCase())) {
                val remainingPart = variantPart.substring(directory.length)
                if (remainingPart.length == 0) {
                    possibleLocations.add(possibleLocation)
                    addPossibleSplitLocations(basePath, possibleLocation, possibleLocations)
                } else {
                    findPossibleLocations(basePath, possibleLocation, remainingPart, possibleLocations)
                }
            }
        }
    }

    private fun addPossibleSplitLocations(basePath: String, possibleLocation: String, possibleLocations: MutableList<String>) {
        for (abiSplit in SUPPORTED_ABI_SPLITS) {
            val splitDir = File("$basePath$possibleLocation/$abiSplit")
            if (splitDir.isDirectory) {
                possibleLocations.add("$possibleLocation/$abiSplit")
                for (densitySplit in SUPPORTED_DENSITY_SPLITS) {
                    val splitSubDir = File("$basePath$possibleLocation/$abiSplit/$densitySplit")
                    if (splitSubDir.isDirectory) {
                        possibleLocations.add("$possibleLocation/$abiSplit/$densitySplit")
                    }
                }
            }
        }
        for (densitySplit in SUPPORTED_DENSITY_SPLITS) {
            val splitDir = File("$basePath$possibleLocation/$densitySplit")
            if (splitDir.isDirectory) {
                possibleLocations.add("$possibleLocation/$densitySplit")
            }
        }
    }

    companion object {

        internal val GRADLE_GEN_FOLDER = Pattern.compile("^(.*?)build[\\\\/]generated[\\\\/]source[\\\\/](k?apt)(.*)$")
        internal val OUTPUT_JSON_PATTERN = Pattern.compile(".*,\"path\":\"(.*?)\",.*")

        private val SUPPORTED_ABI_SPLITS = listOf("arm64-v8a", "armeabi", "armeabi-v7a", "mips", "mips64", "x86", "x86_64")
        private val SUPPORTED_DENSITY_SPLITS = listOf("hdpi", "ldpi", "mdpi", "xhdpi", "xxhdpi", "xxxhdpi")

        private const val BUILD_TOOLS_V32_MANIFEST_PATH = "build/intermediates/merged_manifests"
    }

    private fun lowerCaseFirst(string: String): String {
        if (string.length < 2)
            return string.toLowerCase()

        val first = string.substring(0, 1).toLowerCase()
        val end = string.substring(1, string.length)
        return first + end
    }

    private fun upperCaseFirst(string: String): String {
        if (string.length < 2)
            return string.toUpperCase()

        val first = string.substring(0, 1).toUpperCase()
        val end = string.substring(1, string.length)
        return first + end
    }
}

class MavenAndroidManifestFinderStrategy(sourceFolder: String) :
        AndroidManifestFinderStrategy("Maven", MAVEN_GEN_FOLDER, sourceFolder) {

    override fun possibleLocations(): Iterable<String> {
        return listOf("target", "src/main", "")
    }

    companion object {

        internal val MAVEN_GEN_FOLDER = Pattern.compile("^(.*?)target[\\\\/]generated-sources.*$")
    }
}

class EclipseAndroidManifestFinderStrategy(sourceFolder: String) :
        AndroidManifestFinderStrategy("Eclipse", ECLIPSE_GEN_FOLDER, sourceFolder) {

    override fun possibleLocations(): Iterable<String> =
            emptyList()

    companion object {

        internal val ECLIPSE_GEN_FOLDER = Pattern.compile("^(.*?)\\.apt_generated.*$")
    }

}
