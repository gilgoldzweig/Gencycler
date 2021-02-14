package com.gillongname.core

import androidx.recyclerview.widget.RecyclerView
import com.gillongname.annotations.GencyclerModel
import java.util.*
import kotlin.collections.ArrayList


abstract class GencyclerListAdapter<E : GencyclerModel, VH : RecyclerView.ViewHolder>(
    val elements: MutableList<E> = ArrayList()
) : RecyclerView.Adapter<VH>(), MutableListOperators<E>, MutableList<E> by elements {

    override val size: Int
        get() = elements.size

    override fun add(element: E): Boolean {
        val result = elements.add(element)
        if (result) {
            notifyItemInserted(size)
        }
        return result
    }

    override fun add(index: Int, element: E) {
        elements.add(index, element)
        notifyItemInserted(index)
    }

    override fun addAll(elements: Collection<E>): Boolean =
        addAll(size, elements)

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        val result = this.elements.addAll(index, elements)
        if (result) {
            notifyItemRangeInserted(index, elements.size)
        }
        return result
    }

    override fun plus(element: E): Boolean =
        add(element)

    override fun plus(elements: Collection<E>): Boolean =
        addAll(elements)


    override fun remove(element: E): Boolean {
        val index = elements.indexOf(element)
        val result = elements.remove(element)

        if (result) notifyItemRemoved(index)
        return result
    }


    override fun removeAt(index: Int): E {
        if (index !in 0 until size)
            throw IndexOutOfBoundsException("Failed removing element at $index, size is $size")

        val result = elements.removeAt(index)
        notifyItemRemoved(index)
        return result
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        if (elements.isEmpty()) return false

        val startIndex = this.elements.indexOf(elements.first())

        val result = this.elements.removeAll(elements)

        if (result) notifyItemRangeRemoved(startIndex, elements.size)

        return result
    }

    open fun removeRange(range: IntRange): Boolean {
        var result = false
        if (range in 0 until size) {
            val subList = subList(range.first, range.last)
            result = removeAll(subList)
            if (result) {
                notifyItemRangeRemoved(range.first, subList.size)
            }
        }
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


    override operator fun set(index: Int, element: E): E {
        if (index !in 0 until size)
            throw IndexOutOfBoundsException("Failed setting element at $index, size is $size")

        val lastElement = elements.set(index, element)
        notifyItemChanged(index)
        return lastElement
    }

    open fun swap(from: Int, to: Int): Boolean {
        if (from < 0 || from > size || to < 0 || to > size
        ) return false

        val temp = elements[from]
        elements[from] = elements[to]
        elements[to] = temp

        notifyItemMoved(from, to)
        return true
    }

    override operator fun get(index: Int): E = elements[index]

    override operator fun get(element: E): Int = elements.indexOf(element)

    override operator fun get(range: IntRange): Collection<E> =
        elements.subList(range.first, range.last)

    override operator fun contains(element: E): Boolean =
        element in elements

    open fun replace(collection: Collection<E>) {
        elements.replace(collection)
        notifyDataSetChanged()
    }

    override fun clear() {
        elements.clear()
        notifyDataSetChanged()
    }

    override fun isEmpty(): Boolean = elements.isEmpty()


    /**
     * overrides the getItemCount which is constant that way we don't have to generate it
     * It can be overridden if the user want's to
     *
     */
    override fun getItemCount(): Int = size


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
}