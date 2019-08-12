package goldzweigapps.com.sample.filter

import android.widget.Filter
import goldzweigapps.com.gencycler.filter.GencyclerFilter
import org.junit.After
import org.junit.Before

import org.junit.Test

class GencyclerFilterTest {

    private lateinit var testData: List<FilterableTestModel>
    private lateinit var filter: Filter
    private var doOnResultAction: (List<FilterableTestModel>) -> Unit = {}
    private val testRange = 1..5
    private val reverseTestRange = 5 downTo 1

    @Before
    fun setUp() {
        testData = (testRange).map {
            FilterableTestModel("1".repeat(it))
        }
        filter = GencyclerFilter(testData, { query, model ->
            model.charSequence.contains(query)
        }, doOnResultAction)
    }

    @Test
    fun typingText_shouldRemoveItemsFromList() {
//        (testRange).forEach {
//            filter.filter("1".repeat(it)) {
//                assertEquals(it, reverseTestRange.step(1).step)
//            }
//        }
    }

    @After
    fun tearDown() {
    }
}