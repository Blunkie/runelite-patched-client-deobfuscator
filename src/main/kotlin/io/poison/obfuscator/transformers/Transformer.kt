package io.poison.obfuscator.transformers

import io.poison.obfuscator.asm.ClassPool

interface Transformer {
    fun apply(classes: ClassPool)
}