package goldzweigapps.com.compiler.models

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import goldzweigapps.com.compiler.consts.Packages
import javax.lang.model.element.TypeParameterElement
import javax.lang.model.type.TypeMirror

/**
 * Created by gilgoldzweig on 08/02/2018.
 */
data class ViewType(val layoutName: String = "",
                    val dataContainerType: TypeName,
                    val viewHolderType: ClassName)

