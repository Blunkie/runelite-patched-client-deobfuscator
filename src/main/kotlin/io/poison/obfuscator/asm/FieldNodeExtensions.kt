package io.poison.obfuscator.asm

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.FieldNode

fun FieldNode.isStatic() = this.access and Opcodes.ACC_STATIC != 0