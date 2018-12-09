package goldzweigapps.com.gencycler.extensions

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import goldzweigapps.com.annotations.annotations.GencyclerModel
import goldzweigapps.com.gencycler.GencyclerHolder
import goldzweigapps.com.gencycler.GencyclerRecyclerAdapter

class SimpleGesturesHelper(private val adapter:
						   GencyclerRecyclerAdapter<GencyclerModel, GencyclerHolder>) :
		ItemTouchHelper.Callback() {

	private val itemTouchHelper = ItemTouchHelper(this)
	private var swipeEnabled: Boolean = false
	private var dragEnabled: Boolean = false
	private var longPressDragEnabled: Boolean = false
	private val directions: HashSet<Int> = HashSet()


	fun setSwipeEnabled(enabled: Boolean, vararg directions: Int) {
		swipeEnabled = enabled
		this.directions.addAll(directions.toTypedArray())
	}

	@JvmOverloads
	fun setDragAndDropEnabled(enabled: Boolean, longPressOnly: Boolean = false) {
		dragEnabled = enabled
		longPressDragEnabled = longPressOnly
	}

	fun attachToRecyclerView(recyclerView: RecyclerView) {
		itemTouchHelper.attachToRecyclerView(recyclerView)
	}

	override fun onMove(recyclerView: RecyclerView,
						oldHolder: RecyclerView.ViewHolder,
						newHolder: RecyclerView.ViewHolder): Boolean {

		return dragEnabled &&
				adapter.swap(oldHolder.adapterPosition, newHolder.adapterPosition)
	}

	override fun getMovementFlags(recyclerView: RecyclerView,
								  holder: RecyclerView.ViewHolder): Int {

		val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
		val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END

		return makeMovementFlags(dragFlags, swipeFlags)
	}

	override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
		if (!swipeEnabled || direction !in directions) return
		adapter.remove(holder.adapterPosition)
	}
}