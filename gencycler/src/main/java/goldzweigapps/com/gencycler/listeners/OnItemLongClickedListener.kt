package goldzweigapps.com.gencycler.listeners

import goldzweigapps.com.annotations.annotations.GencyclerModel

interface OnItemLongClickedListener<T: GencyclerModel> : OnItemChangedListener<T> {
	fun onItemLongClicked(item: T, position: Int): Boolean
}