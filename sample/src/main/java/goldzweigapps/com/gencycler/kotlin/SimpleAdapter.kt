package goldzweigapps.com.gencycler.kotlin

import android.content.Context
import goldzweigapps.com.annotations.annotations.GencyclerAdapter
import goldzweigapps.com.gencycler.SimpleModelViewHolder

import goldzweigapps.com.gencycler.kotlin.simple.AnotherSimpleModel
import goldzweigapps.com.gencycler.kotlin.simple.SimpleModel

@GencyclerAdapter(SimpleModel::class)
class SimpleAdapter(context: Context) : GeneratedSimpleAdapter(context) {

	override fun onBindSimpleModelViewHolder(simpleModelViewHolder: SimpleModelViewHolder,
											 simpleModel: SimpleModel,
											 position: Int) {

	}

}