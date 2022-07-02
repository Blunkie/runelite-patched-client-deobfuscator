package io.poison.obfuscator.asm

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodNode

fun MethodNode.isMainMethod() = this.name.equals("main") && this.desc.equals("([Ljava/lang/String;)V")

fun MethodNode.isStatic() = this.access and Opcodes.ACC_STATIC != 0