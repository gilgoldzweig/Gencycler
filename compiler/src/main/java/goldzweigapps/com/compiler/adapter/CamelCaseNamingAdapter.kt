package goldzweigapps.com.compiler.adapter

/**
 * Created by gilgoldzweig on 08/02/2018.
 */
class CamelCaseNamingAdapter : NamingAdapter {

    private val wordSeparators = arrayOf('_', '-', '.')

    override fun buildNameForId(id: String): String {
        var shadowId = id
        var name = ""
        if (shadowId.isEmpty()) return shadowId
        if (shadowId[0].isUpperCase()) {
            shadowId = shadowId.replace(shadowId[0], shadowId[0].toLowerCase())
        }
        var capitalNext = false
        for (char in shadowId) {
            if (char in wordSeparators) {
                capitalNext = true
            } else {
                name += if (capitalNext) char.toUpperCase() else char
                capitalNext = false
            }
        }
        return name
    }
}