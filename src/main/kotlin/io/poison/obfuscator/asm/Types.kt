package io.poison.obfuscator.asm

object Types {
    fun isObject(name: String) = name.startsWith("L") && name.endsWith(";")

    fun stripObjectBoilerplate(name: String) = if (!isObject(name)) name else name.substring(1, name.length - 1)

    fun addObjectBoilerplate(name: String) = if (isObject(name)) name else "L$name;"

    fun getClassName(name: String) = if (isObject(name)) {
        if (name.contains("/")) {
            name.substringAfterLast("/").substringBefore(";")
        } else {
            stripObjectBoilerplate(name)
        }
    } else {
        if (name.contains("/")) {
            name.substringAfterLast("/")
        } else {
            name
        }
    }
}