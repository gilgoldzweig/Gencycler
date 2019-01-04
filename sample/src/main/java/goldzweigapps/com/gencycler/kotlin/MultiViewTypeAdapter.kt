package goldzweigapps.com.gencycler.kotlin

import android.content.Context
import goldzweigapps.com.annotations.annotations.GencyclerAdapter
import goldzweigapps.com.gencycler.AnotherSimpleModelViewHolder
import goldzweigapps.com.gencycler.SimpleModelViewHolder
import goldzweigapps.com.gencycler.kotlin.simple.AnotherSimpleModel
import goldzweigapps.com.gencycler.kotlin.simple.SimpleModel

@GencyclerAdapter(SimpleModel::class, AnotherSimpleModel::class)
class MultiViewTypeAdapter(context: Context) : GeneratedMultiViewTypeAdapter(context) {

	override fun onBindSimpleModelViewHolder(simpleModelViewHolder: SimpleModelViewHolder, simpleModel: SimpleModel, position: Int) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun onBindAnotherSimpleModelViewHolder(anotherSimpleModelViewHolder: AnotherSimpleModelViewHolder, ANOTHER_SIMPLE_MODEL: AnotherSimpleModel<*, *>, position: Int) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}