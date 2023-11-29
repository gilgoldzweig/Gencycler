package com.gillongname.core

interface MutableListOperators<E> {

    operator fun plus(element: E): Boolean
    operator fun plus(elements: Collection<E>): Boolean

    operator fun minus(element: E): Boolean
    operator fun minus(index: Int): E
    operator fun minus(elements: Collection<E>): Boolean
    operator fun minus(range: IntRange): Boolean

    operator fun contains(element: E): Boolean

    operator fun get(element: E): Int
    operator fun get(index: Int): E
    operator fun get(range: IntRange): Collection<E>
}