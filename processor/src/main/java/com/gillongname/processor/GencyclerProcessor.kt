package com.gillongname.processor

import com.gillongname.annotations.ViewHolder
import com.gillongname.annotations.adapter.Adapter
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import java.io.OutputStream

class GencyclerProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {
    val logger = environment.logger
    val generator = environment.codeGenerator

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(Adapter::class.qualifiedName!!).forEach {
            logger.info("Creating an Adapter for: ", it)
        }

        resolver.getSymbolsWithAnnotation(ViewHolder::class.qualifiedName!!).forEach {
            logger.info("Creating a ViewHolder for: ", it)
        }

        return emptyList()
    }

    override fun onError() {
        super.onError()

    }
    override fun finish() {
        logger.info("Gencycler finished processing")
    }
}

fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}