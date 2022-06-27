package com.puresb.rpcd

import io.poison.obfuscator.asm.ClassPool
import io.poison.obfuscator.transformers.Transformer

class RemoveNamedAnnotations : Transformer {
    val targetAnnotationDesc = "Ljavax/inject/Named;"

    override fun apply(classes: ClassPool) {
        classes.forEach {
            it.fields.forEach { field ->
                field?.invisibleAnnotations?.removeIf { anno -> anno?.desc == targetAnnotationDesc }
            }

            it.methods.forEach { method ->
                method?.invisibleAnnotations?.removeIf { anno -> anno?.desc == targetAnnotationDesc }
            }
        }
    }
}