package goldzweigapps.com.gencycler.extensions

import androidx.recyclerview.widget.ItemTouchHelper
import goldzweigapps.com.annotations.annotations.GencyclerModel
import goldzweigapps.com.gencycler.GencyclerHolder
import goldzweigapps.com.gencycler.GencyclerRecyclerAdapter
import java.lang.UnsupportedOperationException

class SimpleGesturesHelper<in T: GencyclerRecyclerAdapter<out GencyclerModel, out GencyclerHolder>>(
		private val adapter: T) : ItemTouchHelper.Callback() {

	private val itemTouchHelper = ItemTouchHelper(this)
	private var swipeEnabled: Boolean = false
	private var dragEnabled: Boolean = false
	private var longPressDragEnabled: Boolean = false
	private var direction = -1
	private val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN



	fun setSwipeEnabled(enabled: Boolean, vararg directions: Int) {
		swipeEnabled = enabled
		if (!swipeEnabled) {
			direction = -1
			return
		}

		direction = when {
			directions.isEmpty() ->
				throw UnsupportedOperationException("no directions we're provided for setSwipeEnabled")

			directions.size > 1 ->
				ItemTouchHelper.START or ItemTouchHelper.END

			else ->
				directions[0]
		}
	}

	@JvmOverloads
	fun setDragAndDropEnabled(enabled: Boolean, longPressOnly: Boolean = false) {
		dragEnabled = enabled
		longPressDragEnabled = longPressOnly
	}

	fun attachToRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView) {
		itemTouchHelper.attachToRecyclerView(recyclerView)
	}

	override fun onMove(recyclerView: androidx.recyclerview.widget.RecyclerView,
						oldHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
						newHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder): Boolean {

		return dragEnabled &&
				adapter.swap(oldHolder.adapterPosition, newHolder.adapterPosition)
	}

	override fun getMovementFlags(recyclerView: androidx.recyclerview.widget.RecyclerView,
								  holder: androidx.recyclerview.widget.RecyclerView.ViewHolder): Int =
			makeMovementFlags(dragFlags, direction)

	override fun onSwiped(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, direction: Int) {
		if (swipeEnabled) adapter.remove(holder.adapterPosition)
	}

	companion object {
		const val START = ItemTouchHelper.START
		const val END = ItemTouchHelper.END
	}
}