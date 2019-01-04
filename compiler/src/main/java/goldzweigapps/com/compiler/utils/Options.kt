package goldzweigapps.com.compiler.utils

import goldzweigapps.com.compiler.models.Option

import javax.annotation.processing.ProcessingEnvironment
import java.util.HashMap

class Options(processingEnvironment: ProcessingEnvironment) {

    private val supportedOptions = HashMap<String, Option>()
    private var options: Map<String, String> = HashMap()

    init {
        options = processingEnvironment.options
        addSupportedOption(OPTION_MANIFEST)
        addSupportedOption(OPTION_USE_R2)
        addSupportedOption(OPTION_RESOURCE_FOLDER)
        addSupportedOption(OPTION_R_CLASS)
    }

    fun addAllSupportedOptions(options: List<Option>) {
        options.forEach(::addSupportedOption)
    }

    private fun addSupportedOption(option: Option) {
        supportedOptions[option.name] = option
    }

    operator fun get(option: Option): String? =
            options[option.name] ?: option.defaultValue

    operator fun get(optionKey: String): String? =
            supportedOptions[optionKey]
                    ?.let(::get) ?: options[optionKey]

    fun getBoolean(option: Option): Boolean =
            get(option)?.toBoolean() ?: false

    fun getBoolean(optionKey: String): Boolean =
            get(optionKey)?.toBoolean() ?: false

    fun getSupportedOptions(): Set<String> =
            supportedOptions.keys

    companion object {
        val OPTION_MANIFEST = Option("androidManifestFile")
        val OPTION_USE_R2 = Option("androidUseR2")
        val OPTION_RESOURCE_FOLDER = Option("resourcesFolder", "main")
        val OPTION_R_CLASS= Option("rClassPackage")
    }
}