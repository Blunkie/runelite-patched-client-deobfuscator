package com.puresb.rpcd

import io.poison.obfuscator.asm.ClassPool
import io.poison.obfuscator.asm.isStatic
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * Does not perform bytecode-level diffs to the original in the updated.
 * Only considers fields and methods. Not inner classes, etc.
 * The contents of new classes are not examined!
 */
class BasicClassPoolDiff {
    val ignoredNamespaces = listOf("org/bouncycastle", "org/json")
    val addedClasses = mutableListOf<String>()
    val removedClasses = mutableListOf<String>()
    val addedMethods = mutableListOf<String>()
    val removedMethods = mutableListOf<String>()
    val addedFields = mutableListOf<String>()
    val removedFields = mutableListOf<String>()

    fun compute(original: ClassPool, updated: ClassPool) {
        original.forEach { thiz ->
            if (ignoredNamespaces.any { thiz.name.startsWith(it) }) {
                return@forEach
            }

            val other = updated.get(thiz.name)

            if (other == null) {
                removedClasses.add(thiz.name)
                return@forEach
            }

            thiz.methods.forEach {
                val om = other.methods.firstOrNull { o -> o.desc == it.desc && o.name == it.name }

                if (om == null) {
                    val staticMarker = if (it.isStatic()) " [static]" else ""
                    removedMethods.add("${it.desc} ${it.name}" + staticMarker)
                }
            }

            thiz.fields.forEach {
                val of = other.fields.firstOrNull { o -> o.desc == it.desc && o.name == it.name }

                if (of == null) {
                    val staticMarker = if (it.isStatic()) " [static]" else ""
                    removedFields.add("${it.desc} ${it.name}" + staticMarker)
                }
            }

            other.methods.forEach {
                val om = thiz.methods.firstOrNull { o -> o.desc == it.desc && o.name == it.name }

                if (om == null) {
                    val staticMarker = if (it.isStatic()) " [static]" else ""
                    addedMethods.add("${it.desc} ${it.name}" + staticMarker)
                }
            }

            other.fields.forEach {
                val of = thiz.fields.firstOrNull { o -> o.desc == it.desc && o.name == it.name }

                if (of == null) {
                    val staticMarker = if (it.isStatic()) " [static]" else ""
                    addedFields.add("${it.desc} ${it.name}" + staticMarker)
                }
            }
        }

        updated.filter { original.get(it.name) == null }.forEach {
            addedClasses.add(it.name)
        }
    }

    fun logResults() {
        addedClasses.sorted().forEach {
            logger.debug { "add class: $it" }
        }

        removedClasses.sorted().forEach {
            logger.debug { "rm class: $it" }
        }

        addedMethods.sorted().forEach {
            logger.debug { "add method: $it" }
        }

        removedMethods.sorted().forEach {
            logger.debug { "rm method: $it" }
        }

        addedFields.sorted().forEach {
            logger.debug { "add field: $it" }
        }

        removedFields.sorted().forEach {
            logger.debug { "rm field: $it" }
        }
    }

    fun logSummary() {
        val netClasses = addedClasses.size - removedClasses.size
        logger.info { "Classes: +${addedClasses.size} / -${removedClasses.size} (net=$netClasses)" }

        val netMethods = addedMethods.size - removedMethods.size
        logger.info { "Methods: +${addedMethods.size} / -${removedMethods.size} (net=$netMethods)" }

        val netFields = addedFields.size - removedFields.size
        logger.info { "Fields: +${addedFields.size} / -${removedFields.size} (net=$netFields)" }
    }
}