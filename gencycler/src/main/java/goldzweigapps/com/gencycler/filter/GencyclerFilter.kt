package goldzweigapps.com.gencycler.filter

import android.widget.Filter
import goldzweigapps.com.annotations.annotations.GencyclerModel

class GencyclerFilter<T : GencyclerModel>(var elements: List<T> = emptyList(),
										  var filter: (CharSequence, T) -> Boolean,
										  var doOnResults: (List<T>) -> Unit) : Filter() {
	private val filterResults = FilterResults()

	override fun performFiltering(constraint: CharSequence?): FilterResults {

		val result = if (constraint.isNullOrEmpty()) {
			elements
		} else {
			elements.filter { filter(constraint, it) }
		}

		filterResults.values = result
		filterResults.count = result.size

		return filterResults
	}

	
	override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
		if (results == null) return
		doOnResults.invoke(results.values as List<T>)
	}
}