package io.poison.obfuscator.asm

import java.nio.file.Path
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream
import kotlin.io.path.outputStream

/**
 * A JAR writer which will not write empty folders.
 */
class JarWriter(private val f: Path) {
    private var entries = mutableListOf<Entry>()
    var dontWriteEmptyFolders = false

    fun add(name: String, bytes: ByteArray) {
        entries.add(Entry(name, bytes))
    }

    fun write() {
        JarOutputStream(f.outputStream()).use {
            filterEmptyEntries().forEach { (name, bytes) ->
                it.putNextEntry(JarEntry(name))
                it.write(bytes)
                it.closeEntry()
            }
        }
    }

    private fun filterEmptyEntries() = entries
        .filter { !dontWriteEmptyFolders || !it.name.endsWith("/") || entries.any { other -> other.name.startsWith(it.name) && !other.name.endsWith("/") } }

    private data class Entry(val name: String, val bytes: ByteArray)
}