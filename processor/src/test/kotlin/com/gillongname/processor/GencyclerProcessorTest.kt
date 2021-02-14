package com.gillongname.processor

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessors
import org.junit.After
import org.junit.Before
import org.junit.Test

class GencyclerProcessorTest {

    val processor = GencyclerProcessor()
    val kotlinSource = SourceFile.kotlin(
        "KClass.kt", """
        import com.gillongname.annotations.GencyclerModel
        import com.gillongname.annotations.ViewHolder

        @ViewHolder(555)
        data class Sample(val text: String) : GencyclerModel
    """
    )

    @Before
    fun setUp() {
    }

    @Test
    fun testViewHolderGeneration() {
        val compilation = KotlinCompilation().apply {
            sources = listOf(kotlinSource)
            symbolProcessors = listOf(processor)
            messageOutputStream = System.out
        }
        val test = compilation.compile()
    }

    @After
    fun tearDown() {
    }
}