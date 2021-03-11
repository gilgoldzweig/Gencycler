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
    lateinit var adapter: SampleAdapter

    @Before
    fun before() {
        elements = ArrayList()
        adapter = TestAdapter(elements)
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
        val stub = spyk(adapter, recordPrivateCalls = true)
        val model = TestGencyclerModel()
        stub.verifyNotificationAction({ add(model) }, { notifyItemInserted(0) })

        val rangeTest = listOf(
            TestGencyclerModel(),
            TestGencyclerModel(),
            TestGencyclerModel(),
            TestGencyclerModel(),
            TestGencyclerModel()
        )

        stub.verifyNotificationAction(
            { addAll(rangeTest) },
            { notifyItemRangeInserted(1, rangeTest.size) })
    }


    @Test
    fun testRemovingElements() {
        val singleModel = TestGencyclerModel(0)
        val stub = spyk(adapter, recordPrivateCalls = true)
        stub.add(element = singleModel, notifyChanges = false)
        stub.remove(singleModel)
//        assert(adapter.isEmpty())
//        stub.verifyNotificationAction({ remove(singleModel) }, { notifyItemRemoved(0) })
    }

    @After
    fun clean() {
        clearAllMocks()
    }

    fun SampleAdapter.verifyNotificationAction(
        action: SampleAdapter.() -> Unit,
        notifyAction: SampleAdapter.() -> Unit
    ) {
        justRun { notifyAction() }
        action()
        verify { notifyAction() }
    }
}
typealias SampleAdapter = GencyclerListAdapter<TestGencyclerModel, TestViewHolder>