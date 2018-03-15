package goldzweigapps.com.compiler.parser

import goldzweigapps.com.compiler.adapter.NamingAdapter
import goldzweigapps.com.compiler.models.ViewHolder
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import kotlin.reflect.KClass

/**
 * Created by gilgoldzweig on 08/02/2018.
 */
interface Parser {
    val namingAdapter: NamingAdapter
    val classType: String

    @Throws(Exception::class)
    fun parse(layoutName: String,
              isUniqueAnnotationPresent: Boolean = false,
              uniqueName: String? = null,
              uniqueValue: String? = null): ViewHolder
}