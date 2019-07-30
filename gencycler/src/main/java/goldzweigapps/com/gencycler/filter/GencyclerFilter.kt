package goldzweigapps.com.gencycler.filter

import android.widget.Filter
import goldzweigapps.com.annotations.annotations.GencyclerModel

open class GencyclerFilter<T : GencyclerModel>(
        var elements: List<T> = emptyList(),
        var filter: (CharSequence, T) -> Boolean,
        var doOnResults: (List<T>) -> Unit
) : Filter() {

    private val sourceCopy = ArrayList(elements)
    private val filterResults = FilterResults()

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var filteredElements: MutableList<T> = ArrayList()
        if (constraint.isNullOrEmpty()) {
            filteredElements = ArrayList(sourceCopy)
        } else {
            sourceCopy.filterTo(filteredElements) { filter(constraint, it) }
        }

        filterResults.values = filteredElements
        filterResults.count = filteredElements.size

        return filterResults
    }


    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        if (results == null) return
        doOnResults.invoke(results.values as List<T>)
    }
}