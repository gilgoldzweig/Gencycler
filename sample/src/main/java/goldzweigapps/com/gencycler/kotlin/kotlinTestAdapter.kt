package goldzweigapps.com.gencycler.kotlin

import android.content.Context
import goldzweigapps.com.annotations.annotations.GencyclerAdapter
import goldzweigapps.com.gencycler.GeneratedSimpleAdapter
import goldzweigapps.com.gencycler.SimpleModelViewHolder
import goldzweigapps.com.gencycler.extensions.SimpleGesturesHelper
import goldzweigapps.com.gencycler.kotlin.simple.AnotherSimpleModel
import goldzweigapps.com.gencycler.kotlin.simple.SimpleModel

@GencyclerAdapter(SimpleModel::class)
class SimpleAdapter(context: Context,
					elements: ArrayList<SimpleModel> = ArrayList()) : GeneratedSimpleAdapter(context, elements) {

	override fun onBindSimpleModelViewHolder(simpleModelViewHolder: SimpleModelViewHolder, simpleModel: SimpleModel, position: Int) {

	}
}
private lateinit var context: Context
private val simpleGesturesHelper = SimpleGesturesHelper(SimpleAdapter(context))
fun init() {
}