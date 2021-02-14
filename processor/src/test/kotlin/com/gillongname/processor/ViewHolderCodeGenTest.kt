//package com.gillongname.processor
//
//import com.tschuchort.compiletesting.KotlinCompilation
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//
//class ViewHolderCodeGenTest {
//
//    val processor = GencyclerProcessor()
//
//    @Before
//    fun setUp() {}
//
//    @Test
//    fun testViewHolderGeneration() {
//        val compilation = KotlinCompilation().apply {
//            sources = listOf(source)
//            symbolProcessors = listOf(MySymbolProcessor())
//        }
//    }
//
//    @After
//    fun tearDown() {}
//}