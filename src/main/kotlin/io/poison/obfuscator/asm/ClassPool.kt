package io.poison.obfuscator.asm

import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode
import java.nio.file.Files
import java.nio.file.Paths
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import kotlin.io.path.outputStream

class ClassPool private constructor() : Iterable<ClassNode> {
    private val classesByName = mutableMapOf<String, ClassNode>()
    private val resources = mutableMapOf<String, ByteArray>()

    fun addClass(node: ClassNode) {
        if (classesByName.containsKey(node.name)) {
            throw IllegalArgumentException("ClassNode with name ${node.name} already exists!")
        }

        classesByName[node.name] = node
    }

    fun classes() = classesByName.values.toList()

    fun classNames() = classesByName.keys

    fun writeJar(fileName: String) {
        val f = Paths.get(fileName)
        Files.deleteIfExists(f)

        val writer = JarWriter(f)
        writer.dontWriteEmptyFolders = true

        classes().forEach { clazz ->
            val entryFileName = clazz.name.replace(".", "\\") + ".class"
            writer.add(entryFileName, clazz.toByteArray())
        }

        resources.forEach { (name, bytes) ->
            writer.add(name, bytes)
        }

        writer.write()
    }

    companion object {
        fun fromJar(fileName: String): ClassPool {
            val pool = ClassPool()

            JarFile(fileName)
                .use { jar -> jar.entries().asSequence()
                    .forEach {
                        if (it.name.endsWith(".class")) {
                            val reader = ClassReader(jar.getInputStream(it))
                            val node = ClassNode()
                            reader.accept(node, ClassReader.SKIP_FRAMES)
                            pool.addClass(node)
                        } else {
                            pool.resources[it.name] = jar.getInputStream(it).readAllBytes()
                        }
                    }
                }

            return pool
        }
    }

    override fun iterator(): Iterator<ClassNode> {
        return classes().iterator()
    }
}