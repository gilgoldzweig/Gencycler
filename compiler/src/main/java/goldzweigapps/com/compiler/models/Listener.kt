package goldzweigapps.com.compiler.models

import com.squareup.kotlinpoet.TypeName
import goldzweigapps.com.annotations.annotations.Actions

data class Listener(val name: String,
                    val valueDataType: TypeName,
                    val actions: Array<Actions>)

