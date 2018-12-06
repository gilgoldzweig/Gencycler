package goldzweigapps.com.compiler.utils

import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.FileObject
import javax.tools.StandardLocation
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException

object FileHelper {

    @Throws(FileNotFoundException::class)
    fun findRootProject(processingEnv: ProcessingEnvironment): File {
        val rootProjectHolder = findRootProjectHolder(processingEnv)
        return rootProjectHolder.projectRoot
    }

    /**
     * We use a dirty trick to find the AndroidManifest.xml file, since it's not
     * available in the classpath. The idea is quite simple : create a fake
     * class file, retrieve its URI, and start going up in parent folders to
     * find the AndroidManifest.xml file. Any better solution will be
     * appreciated.
     */
    @Throws(FileNotFoundException::class)
    fun findRootProjectHolder(processingEnv: ProcessingEnvironment): FileHolder {
        val filer = processingEnv.filer

        val dummySourceFile: FileObject
        try {
            dummySourceFile = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", "dummy" + System.currentTimeMillis())
        } catch (ignored: IOException) {
            throw FileNotFoundException()
        }

        var dummySourceFilePath = dummySourceFile.toUri().toString()

        if (dummySourceFilePath.startsWith("file:")) {
            if (!dummySourceFilePath.startsWith("file://")) {
                dummySourceFilePath = "file://" + dummySourceFilePath.substring("file:".length)
            }
        } else {
            dummySourceFilePath = "file://$dummySourceFilePath"
        }

        val cleanURI: URI
        try {
            cleanURI = URI(dummySourceFilePath)
        } catch (e: URISyntaxException) {
            throw FileNotFoundException()
        }

        val dummyFile = File(cleanURI)
        val sourcesGenerationFolder = dummyFile.parentFile
        val projectRoot = sourcesGenerationFolder.parentFile

        return FileHolder(dummySourceFilePath, sourcesGenerationFolder, projectRoot)
    }

    @Throws(FileNotFoundException::class)
    fun findModuleResourcesFolder(processingEnv: ProcessingEnvironment): File {
        val buildFolderRoot = findRootProjectHolder(processingEnv).projectRoot

        val appRootFolder = buildFolderRoot
                .parentFile //source
                .parentFile //generated
                .parentFile //build
                .parentFile //app
        val resourcesOption =
                EnvironmentUtil.getOptionValue(Options.OPTION_RESOURCE_FOLDER)
        val resourceFolder = File("$appRootFolder/src/$resourcesOption/res")

        if (resourceFolder.exists()) {
            return resourceFolder
        } else {
            val fileNotFoundException =
                    FileNotFoundException("Project resources we're not found at $resourceFolder")
            Logger.e(exception = fileNotFoundException)
            throw fileNotFoundException
        }

    }

    @Throws(FileNotFoundException::class)
    fun findModuleLayoutFolder(processingEnv: ProcessingEnvironment): File =
            File("${findModuleResourcesFolder(processingEnv)}/layout")

    @Throws(FileNotFoundException::class)
    fun resolveOutputDirectory(processingEnv: ProcessingEnvironment): File {
        val rootProject = FileHelper.findRootProject(processingEnv)

        // Target folder - Maven
        val targetFolder = File(rootProject, "target")
        if (targetFolder.isDirectory && targetFolder.canWrite()) {
            return targetFolder
        }

        // Build folder - Gradle
        val buildFolder = File(rootProject, "build")
        if (buildFolder.isDirectory && buildFolder.canWrite()) {
            return buildFolder
        }

        // Bin folder - Eclipse
        val binFolder = File(rootProject, "bin")
        return if (binFolder.isDirectory && binFolder.canWrite()) {
            binFolder
        } else rootProject

        // Fallback to projet root folder
    }
}

data class FileHolder(var dummySourceFilePath: String,
                      var sourcesGenerationFolder: File,
                      var projectRoot: File)