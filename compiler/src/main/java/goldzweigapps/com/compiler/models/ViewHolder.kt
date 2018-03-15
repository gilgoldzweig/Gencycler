package goldzweigapps.com.compiler.models


/**
 * Created by gilgoldzweig on 08/02/2018.
 */
data class ViewHolder(val layoutName: String,
                      val viewFields: List<View>,
                      val classType: String,
                      val unique: Boolean = false,
                      val uniqueName: String? = null,
                      val uniqueValue: String? = null)