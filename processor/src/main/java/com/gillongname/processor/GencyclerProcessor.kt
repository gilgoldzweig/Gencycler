package com.gillongname.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated

class GencyclerProcessor : SymbolProcessor {

    override fun init(
        options: Map<String, String>,
        kotlinVersion: KotlinVersion,
        codeGenerator: CodeGenerator,
        logger: KSPLogger
    ) {
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getAllFiles().map { it }
    }

    override fun finish() {

    }
}