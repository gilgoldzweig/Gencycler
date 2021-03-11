package com.gillongname.core

import androidx.recyclerview.widget.RecyclerView
import com.gillongname.core.adadpter.TestAdapter
import com.gillongname.core.adadpter.TestGencyclerModel
import com.gillongname.core.adadpter.TestRecyclerViewObserver
import com.gillongname.core.adadpter.TestViewHolder
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class GencyclerListAdapterTest {

    var elements = ArrayList<TestGencyclerModel>()
    lateinit var adapter: GencyclerListAdapter<TestGencyclerModel, TestViewHolder>

    @Before
    fun before() {
        elements = ArrayList()
        adapter = spyk(TestAdapter(elements), recordPrivateCalls = true)
    }

    @Test
    fun checkAddingElementsActuallyUpdatesList() {
        val model = TestGencyclerModel()

        adapter.add(element = model, notifyChanges = false)
        assert(model in elements)

        (0..10).forEach {
            adapter.add(element = TestGencyclerModel(it), notifyChanges = false)
        }
        assertEquals(elements.size, 12)
        val indexedModel = TestGencyclerModel(90)
        adapter.add(4, indexedModel, false)

        assertEquals(elements[4], indexedModel)

        val rangeTest = listOf(
            TestGencyclerModel(),
            TestGencyclerModel(),
            TestGencyclerModel(),
            TestGencyclerModel(),
            TestGencyclerModel()
        )

        val size = adapter.size
        adapter.addAll(elements = rangeTest, notifyChanges = false)
        assertEquals(elements.size, size + rangeTest.size)
    }

    @Test
    fun checkAddingElementsNotifies() {
        val model = TestGencyclerModel()
        justRun { adapter.notifyItemInserted(0) }
        adapter.add(model)


        verify(exactly = 1) {
            adapter.notifyItemInserted(0)
        }


        val rangeTest = listOf(
            TestGencyclerModel(),
            TestGencyclerModel(),
            TestGencyclerModel(),
            TestGencyclerModel(),
            TestGencyclerModel()
        )

        justRun { adapter.notifyItemRangeInserted(1, rangeTest.size) }
        adapter.addAll(rangeTest)

        verify(exactly = 1) {
            adapter.notifyItemRangeInserted(1, rangeTest.size)
        }
    }

}