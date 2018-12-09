package goldzweigapps.com.gencycler.listeners

import goldzweigapps.com.annotations.annotations.GencyclerModel

interface OnItemClickedListener<T: GencyclerModel> : OnItemChangedListener<T> {
	fun onItemClicked(item: T, position: Int)
}