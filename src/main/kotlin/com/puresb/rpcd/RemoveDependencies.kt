package com.puresb.rpcd

import io.poison.obfuscator.asm.ClassPool
import io.poison.obfuscator.transformers.Transformer

class RemoveDependencies : Transformer {
    override fun apply(classes: ClassPool) {
        classes.classNames().forEach {
            if (it.contains("/")) {
                classes.remove(it)
            }
        }
    }
}