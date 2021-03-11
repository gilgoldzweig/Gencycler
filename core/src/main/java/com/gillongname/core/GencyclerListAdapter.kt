package com.gillongname.core

import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

abstract class GencyclerListAdapter<E, VH : RecyclerView.ViewHolder>(
    private val elements: MutableList<E> = ArrayList()
) : RecyclerView.Adapter<VH>(), MutableListOperators<E>, MutableList<E> by elements {

    override val size: Int
        get() = elements.size

    /**
     * overrides the getItemCount which is constant that way we don't have to generate it
     * It can be overridden if the user want's to
     */
    override fun getItemCount(): Int = size

    override fun add(element: E): Boolean =
        add(element = element, notifyChanges = true)

    override fun add(index: Int, element: E) =
        add(index, element, true).ignore()

    fun add(index: Int = size, element: E, notifyChanges: Boolean): Boolean {
        elements.add(index, element)
        if (notifyChanges) {
            notifyItemInserted(index)
        }
        return true
    }


    override fun addAll(elements: Collection<E>): Boolean =
        addAll(size, elements)

    override fun addAll(index: Int, elements: Collection<E>): Boolean =
        addAll(index, elements, true)

    fun addAll(index: Int = size, elements: Collection<E>, notifyChanges: Boolean): Boolean {
        val result = this.elements.addAll(index, elements)
        if (result && notifyChanges) {
            notifyItemRangeInserted(index, elements.size)
        }
        return result
    }


    override fun plus(element: E): Boolean =
        add(element)

    override fun plus(elements: Collection<E>): Boolean =
        addAll(elements)


    override fun remove(element: E): Boolean =
        remove(element, true)

    fun remove(element: E, notifyChanges: Boolean): Boolean {
        val index = elements.indexOf(element)
        val result = elements.remove(element)

        if (result && notifyChanges) notifyItemRemoved(index)
        return result
    }

    override fun removeAt(index: Int): E =
        removeAt(index, true)

    fun removeAt(index: Int, notifyChanges: Boolean): E {
        if (index !in 0 until size)
            throw IndexOutOfBoundsException("Failed removing element at $index, size is $size")

        val result = elements.removeAt(index)
        if (notifyChanges) {
            notifyItemRemoved(index)
        }
        return result
    }

    override fun removeAll(elements: Collection<E>): Boolean =
        removeAll(elements, true)

    open fun removeRange(range: IntRange): Boolean {
        var result = false
        if (range in 0 until size) {
            val subList = subList(range.first, range.last)
            result = removeAll(subList, true)
        }
        return result
    }

    fun removeAll(elements: Collection<E>, notifyChanges: Boolean): Boolean {
        if (elements.isEmpty()) return false

        val startIndex = this.elements.indexOf(elements.first())

        val result = this.elements.removeAll(elements)

        if (result && notifyChanges) notifyItemRangeRemoved(startIndex, elements.size)

        return result
    }


    override fun minus(element: E): Boolean =
        remove(element)

    override fun minus(index: Int): E =
        removeAt(index)

    override fun minus(elements: Collection<E>): Boolean =
        removeAll(elements)

    override fun minus(range: IntRange): Boolean =
        removeRange(range)


    override operator fun set(index: Int, element: E): E =
        set(index, element, true)

    fun set(index: Int, element: E, notifyChanges: Boolean): E {
        if (index !in 0 until size)
            throw IndexOutOfBoundsException("Failed setting element at $index, size is $size")

        val lastElement = elements.set(index, element)
        if (notifyChanges) {
            notifyItemChanged(index)
        }
        return lastElement
    }

    open fun swap(from: Int, to: Int, notifyChanges: Boolean = true): Boolean {
        if (from < 0 || from > size || to < 0 || to > size) return false

        val temp = elements[from]
        elements[from] = elements[to]
        elements[to] = temp

        if (notifyChanges) {
            notifyItemMoved(from, to)
        }

        return true
    }


    override operator fun get(index: Int): E = elements[index]

    override operator fun get(element: E): Int = elements.indexOf(element)

    override operator fun get(range: IntRange): Collection<E> =
        elements.subList(range.first, range.last)


    override operator fun contains(element: E): Boolean =
        element in elements

    open fun replace(collection: Collection<E>, notifyChanges: Boolean = true) {
        elements.replace(collection)
        if (notifyChanges) {
            notifyDataSetChanged()
        }
    }

    override fun clear() =
        clear(true)

    fun clear(notifyChanges: Boolean) {
        elements.clear()
        if (notifyChanges) {
            notifyDataSetChanged()
        }
    }

    override fun isEmpty(): Boolean = elements.isEmpty()


    private fun MutableCollection<E>.replace(collection: Collection<E>) {
        if (this is ArrayList) {
            this.ensureCapacity(collection.size)
        }
        collection.forEachIndexed { index, e ->
            if (index < size) {
                elements[index] = e
            } else {
                elements.add(e)
            }
        }

        var modifiedListSize = size
        while (modifiedListSize > collection.size) {
            modifiedListSize--
            elements.removeAt(modifiedListSize)
        }
    }

    private operator fun IntRange.contains(range: IntRange): Boolean =
        start in range && last in range

    /**
     * Used to ignore whatever value is provided in the
     * @receiver
     *
     */
    private fun Any?.ignore(): Unit = Unit

//    /**
////     * Because I can't mock the original notifyDataSetChanged calls I use internal once just for unit testing.
////     * You should use [RecyclerView.Adapter.notifyDataSetChanged]
////     */
////    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
////    internal fun internalNotifyDataSetChanged() = notifyDataSetChanged()
////
////    /**
////     * Because I can't mock the original notifyItemChanged calls I use internal once just for unit testing.
////     * You should use [RecyclerView.Adapter.notifyItemChanged]
////     */
////    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
////    internal fun internalNotifyItemChanged(position: Int) = notifyItemChanged(position)
////
////    /**
////     * Because I can't mock the original notifyItemInserted calls I use internal once just for unit testing.
////     * You should use [RecyclerView.Adapter.notifyItemInserted]
////     */
////    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
////    internal fun internalNotifyItemInserted(position: Int) = notifyItemInserted(position)
////
////    /**
////     * Because I can't mock the original notifyItemRemoved calls I use internal once just for unit testing.
////     * You should use [RecyclerView.Adapter.notifyItemRemoved]
////     */
////    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
////    internal fun internalNotifyItemRemoved(position: Int) = notifyItemRemoved(position)
////
////    /**
////     * Because I can't mock the original notifyItemRangeChanged calls I use internal once just for unit testing.
////     * You should use [RecyclerView.Adapter.notifyItemRangeChanged]
////     */
////    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
////    internal fun internalNotifyItemRangeChanged(positionStart: Int, count: Int) =
////        notifyItemRangeChanged(positionStart, count)
////
////    /**
////     * Because I can't mock the original notifyItemRangeInserted calls I use internal once just for unit testing.
////     * You should use [RecyclerView.Adapter.notifyItemRangeInserted]
////     */
////    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
////    internal fun internalNotifyItemRangeInserted(positionStart: Int, count: Int) =
////        notifyItemRangeInserted(positionStart, count)
////
////    /**
////     * Because I can't mock the original notifyItemRangeRemoved calls I use internal once just for unit testing.
////     * You should use [RecyclerView.Adapter.notifyItemRangeRemoved]
////     */
////    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
////    internal fun internalNotifyItemRangeRemoved(positionStart: Int, count: Int) =
////        notifyItemRangeRemoved(positionStart, count)
////
////    /**
////     * Because I can't mock the original notifyItemMoved calls I use internal once just for unit testing.
////     * You should use [RecyclerView.Adapter.notifyItemMoved]
////     */
////    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
////    internal fun internalNotifyItemMoved(fromPosition: Int, toPosition: Int) =
////        notifyItemMoved(fromPosition, toPosition)
////
}