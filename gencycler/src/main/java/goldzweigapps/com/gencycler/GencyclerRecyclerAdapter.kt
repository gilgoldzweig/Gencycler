package goldzweigapps.com.gencycler

import android.content.Context
import android.os.Build
import android.os.Looper
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import goldzweigapps.com.annotations.annotations.GencyclerDataContainer
import java.util.ArrayList

/**
 * Helper class that helps the generated code be cleaner and provides us with utility methods
 *
 * @property T The type of the data container, anything that extends [GencyclerDataContainer]
 *
 * @property VH The type of our ViewHolder[GencyclerHolder]
   makes it easier for our generated adapter to handle both single & multi view types
 *
 * @param context Used to inflate the layout of the ViewHolder
 *
 * @param elements Our generic elements used to populate our adapter
 *
 * @param updateUi Flag to know if we want to run a Ui thread test and call the appropriate notify method
 *
 */
abstract class GencyclerRecyclerAdapter<T : GencyclerDataContainer, VH : RecyclerView.ViewHolder>(
        context: Context,
        var elements: MutableList<T> = ArrayList(),
        var updateUi: Boolean = true) : RecyclerView.Adapter<VH>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private val uiThreadException =
            IllegalThreadStateException("Trying to update the RecyclerView not on main thread")

    /**
     * overrides the getItemCount which is constant that way we don't have to genrate it
     * It can be overridden if the user want's to
     *
     */
    override fun getItemCount(): Int = size

    /**
     * Helper function to inflate the view holder layout,
     * Can be used by any adapter extending this class
     *
     */
    protected fun inflate(@LayoutRes layoutResId: Int, parent: ViewGroup): View =
            inflater.inflate(layoutResId, parent, false)


    /**
     * Adds a new element to the adapter
     *
     * @param element Element to insert
     * @param position The position we want to add the element,
    if not provided the item will be inserted at the end
     *
     * If updateUi is true then we will call [notifyItemInserted]
     */
    open fun add(element: T, position: Int = elements.size) {
        elements.add(position, element)

        if (!updateUi) return
        if (isUiThread()) notifyItemInserted(position) else throw uiThreadException
    }

    /**
     * Adds a new list of elements to the adapter
     *
     * @param elements Elements to insert
     * @param position The position we want to add the element,
    if not provided the item will be inserted at the end
     *
     * If updateUi is true then we will call [notifyItemRangeInserted]
     */
    open fun add(elements: List<T>, position: Int = elements.size) {
        this.elements.addAll(position, elements)

        if (!updateUi) return
        if (isUiThread()) notifyItemRangeInserted(position, elements.size) else throw uiThreadException
    }

    /**
     * Custom '+' operator
     *
     * @see add(element, position)
     */
    open operator fun plus(element: T): Unit = add(element)

    /**
     * Custom '+' operator
     *
     * @see add(elements, position)
     */
    open operator fun plus(elements: List<T>): Unit = add(elements)

    /**
     * Removes an element from the adapter
     *
     * @param position position to remove
     *
     * If updateUi is true then we will call [notifyItemRemoved]
     *
     * @return true if the item was removed and false otherwise
     */
    open fun remove(position: Int): Boolean {
        if (position < 0 || position > elements.size) return false

        elements.removeAt(position)

        if (!updateUi) return true
        if (isUiThread()) notifyItemRemoved(position) else throw uiThreadException

        return true
    }

    /**
     * Removes an element from the adapter
     *
     * @param element The element to remove
     *
     * If updateUi is true then we will call [notifyItemRemoved]
     *
     * @see remove(position)
     * @return true if the item was removed and false otherwise
     */
    open fun remove(element: T) = remove(elements.indexOf(element))

    /**
     * Removes a list of elements from the adapter
     *
     * @param elements list of elements we want to remove
     *
     * If updateUi is true then we will call [notifyItemRangeRemoved]
     *
     * @return true if the list was removed and false otherwise
     */
    open fun remove(elements: List<T>): Boolean {
        val startPosition = this.elements.indexOf(elements.first())

        val removeStatus = this.elements.removeAll(elements)

        if (!updateUi || !removeStatus) return removeStatus
        if (isUiThread()) notifyItemRangeRemoved(startPosition, elements.size) else throw uiThreadException

        return true
    }

    /**
     * Removes all items in given range
     *
     * @param startIndex The removal range start index
     *
     * @param lastIndex The removal range end index
     *
     * If updateUi is true then we will call [notifyItemRangeRemoved]
     *
     * @return true if the range was removed and false otherwise
     */
    open fun remove(startIndex: Int, lastIndex: Int): Boolean {
        if (startIndex < 0 || lastIndex > elements.size) return false
        return remove(elements.subList(startIndex, lastIndex))
    }

    /**
     * Removes all items in given range
     *
     * @param range The range of the items to remove
     *
     * If updateUi is true then we will call [notifyItemRangeRemoved]
     *
     * @return true if the range was removed and false otherwise
     */
    open fun remove(range: IntRange): Boolean = remove(range.start, range.last)

    /**
     * Custom '-' operator
     *
     * @see remove(range)
     */
    open operator fun minus(range: IntRange): Boolean = remove(range)

    /**
     * Custom '-' operator
     *
     * @see remove(elements)
     */
    open operator fun minus(elements: List<T>): Boolean = remove(elements)

    /**
     * Custom '-' operator
     *
     * @see remove(element)
     */
    open operator fun minus(element: T): Boolean = remove(element)

    /**
     * Custom '-' operator
     *
     * @see remove(position)
     */
    open operator fun minus(position: Int): Boolean = remove(position)

    /**
     * Replaces a single element from the adapter
     *
     * @param element The element that will we inserted instead
     *
     * @param position The element position that will be replaced
     *
     * If updateUi is true then we will call [notifyItemChanged]
     *
     */
    open fun replace(element: T, position: Int) {
        elements[position] = element

        if (!updateUi) return
        if (isUiThread()) notifyItemChanged(position) else throw uiThreadException
    }

    /**
     * Replaces all the existing elements
     *
     * @param elements The replacement list
     *
     * If updateUi is true then we will call [notifyDataSetChanged]
     *
     */
    open fun replace(elements: List<T>) {
        this.elements.clear()
        this.elements.addAll(elements)

        if (!updateUi) return
        if (isUiThread()) notifyDataSetChanged() else throw uiThreadException
    }

    /**
     * Remove all items from the adapter
     *
     * If updateUi is true then we will call [notifyDataSetChanged]
     *
     */
    open fun clear() {
        elements.clear()

        if (!updateUi) return
        if (isUiThread()) notifyDataSetChanged() else throw uiThreadException
    }

    /**
     * The amount of elements currently in the adapter
     */
    val size: Int
        get() = elements.size

    /**
     * @return true if the adapter is empty
     */
    open fun isEmpty(): Boolean = elements.isEmpty()

    /**
     * @return true if the adapter is not empty
     */
    open fun isNotEmpty(): Boolean = !isEmpty()

    /**
     * Custom '[]' operator
     *
     * allows us the get an item by position
     *
     * @sample yourAdapter[1]
     * @return will return the element at first position
     */
    open operator fun get(position: Int): T = elements[position]

    /**
     * Custom '[]' operator
     *
     * allows us the get an item by position
     *
     * @sample yourAdapter[element]
     * @return will return the position of the element or -1
     */
    open operator fun get(element: T): Int = elements.indexOf(element)

    /**
     * Custom 'in' operator
     *
     * allows us to check if an item exist in the adapter
     *
     * @sample element in yourAdapter
     * @return will return if the item exist in the adapter
     */
    open operator fun contains(element: T): Boolean = element in elements

    /**
     * Checks if the current thread is Ui thread
     *
     * @return true if the current thread is Ui thread
     */
    private fun isUiThread(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Looper.getMainLooper().isCurrentThread
        } else {
            Thread.currentThread() == Looper.getMainLooper().thread
        }
    }

}
