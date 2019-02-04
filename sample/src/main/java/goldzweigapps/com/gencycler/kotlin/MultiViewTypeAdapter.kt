package goldzweigapps.com.gencycler.kotlin

import android.content.Context
import android.view.MotionEvent
import android.view.View
import goldzweigapps.com.annotations.annotations.Actions
import goldzweigapps.com.annotations.annotations.GencyclerActions
import goldzweigapps.com.annotations.annotations.GencyclerAdapter
import goldzweigapps.com.annotations.annotations.GencyclerModel
import goldzweigapps.com.gencycler.AnotherSimpleModelViewHolder
import goldzweigapps.com.gencycler.SimpleModelViewHolder
import goldzweigapps.com.gencycler.kotlin.simple.AnotherSimpleModel
import goldzweigapps.com.gencycler.kotlin.simple.SimpleModel

@GencyclerAdapter(SimpleModel::class, AnotherSimpleModel::class)
@GencyclerActions([Actions.TOUCH, Actions.FILTER])
class MultiViewTypeAdapter(context: Context, actionsListener: ActionsListener) : GeneratedMultiViewTypeAdapter(context, actionsListener) {

	override fun onBindSimpleModelViewHolder(simpleModelViewHolder: SimpleModelViewHolder, simpleModel: SimpleModel, position: Int) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun onBindAnotherSimpleModelViewHolder(anotherSimpleModelViewHolder: AnotherSimpleModelViewHolder, ANOTHER_SIMPLE_MODEL: AnotherSimpleModel<*, *>, position: Int) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun performFilter(constraint: CharSequence, gencyclerModel: GencyclerModel): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}


lateinit var context: Context
fun nit() {

	val adapter = MultiViewTypeAdapter(context, object : ActionsListener {

		override fun onSimpleModelViewHolderTouched(view: View, event: MotionEvent): Boolean {
			TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
		}

		override fun onAnotherSimpleModelViewHolderClicked(position: Int, ANOTHER_SIMPLE_MODEL: AnotherSimpleModel<*, *>) {
			TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
		}

		override fun onAnotherSimpleModelViewHolderLongClicked(position: Int, ANOTHER_SIMPLE_MODEL: AnotherSimpleModel<*, *>): Boolean {
			TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
		}

		override fun onAnotherSimpleModelViewHolderTouched(view: View, event: MotionEvent): Boolean {
			TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
		}

	})
}