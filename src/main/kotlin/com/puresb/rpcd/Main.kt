package com.puresb.rpcd

import io.poison.obfuscator.asm.ClassPool
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    logger.info { "Program arguments: ${args.joinToString()}" }
    logger.info { "Starting..." }

    val patchedClientFileName = args[0]
    val classes = ClassPool.fromJar(patchedClientFileName)

    val transformers = listOf(
        RemoveNamedAnnotations(),
        RemoveDependencies(),
        SortMembers(),
    )

    transformers.forEach {
        it.apply(classes)
    }

    val outputFileName = patchedClientFileName.replace(".jar", "-out.jar")
    classes.writeJar(outputFileName)

    logger.info { "Wrote deobbed patched client." }

    if (args.size > 1) {
        val vanillaClient = args[1]
        logger.info { "Comparing to vanilla client..." }

        val vanillaClasses = ClassPool.fromJar(vanillaClient)
        val diff = BasicClassPoolDiff()
        diff.compute(vanillaClasses, classes)
        diff.logResults()
        diff.logSummary()
    }

    logger.info { "Done!" }
}