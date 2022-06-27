package com.puresb.rpcd

import io.poison.obfuscator.asm.ClassPool
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    logger.info { "Program arguments: ${args.joinToString()}" }
    logger.info { "Starting..." }

    val fileName = args.joinToString("")
    val classes = ClassPool.fromJar(fileName)

    val transformers = listOf(
        RemoveNamedAnnotations()
    )

    transformers.forEach {
        it.apply(classes)
    }

    val outputFileName = fileName.replace(".jar", "-out.jar")
    classes.writeJar(outputFileName)

    logger.info { "Done" }
}