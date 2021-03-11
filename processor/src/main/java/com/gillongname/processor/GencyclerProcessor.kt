package com.gillongname.processor

import com.gillongname.annotations.ViewHolder
import com.gillongname.annotations.adapter.Adapter
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import java.io.OutputStream
import kotlin.math.log

class GencyclerProcessor : SymbolProcessor {
    lateinit var logger: KSPLogger
    lateinit var codeGenerator: CodeGenerator
    lateinit var log: OutputStream

    override fun init(
        options: Map<String, String>,
        kotlinVersion: KotlinVersion,
        codeGenerator: CodeGenerator,
        logger: KSPLogger
    ) {
        this.codeGenerator = codeGenerator
        this.logger = logger
        log = codeGenerator.createNewFile(Dependencies(false), "", "GencyclerProcessor", "log")
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getAllFiles().forEach {
//            log.appendText(it.fileName)
        }
        resolver.getSymbolsWithAnnotation(Adapter::class.qualifiedName!!).forEach {
//            log.appendText(it.toString())
        }

        resolver.getSymbolsWithAnnotation(ViewHolder::class.qualifiedName!!).forEach {
//            log.appendText(it.toString())
        }
        return emptyList()
    }

    override fun finish() {
        log.close()
    }
}

fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}