package io.poison.obfuscator.asm

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode

fun ClassNode.toByteArray(): ByteArray {
    val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
    this.accept(writer)
    return writer.toByteArray()
}