package com.puresb.rpcd

import io.poison.obfuscator.asm.ClassPool
import io.poison.obfuscator.transformers.Transformer

class SortMembers : Transformer {
    override fun apply(classes: ClassPool) {
        classes.forEach {
            it.fields.sortBy { f -> f.name }
            it.methods.sortBy { m -> m.name }
        }
    }
}