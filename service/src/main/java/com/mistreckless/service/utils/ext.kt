package com.mistreckless.service.utils

import org.json.JSONArray


fun Array<String>.toJsonArray(): JSONArray {
    val array = JSONArray()
    this.forEach {
        array.put(it)
    }
    return array
}

fun Array<String>.toQueryLine(): String {
    val line = StringBuilder()
    this.forEachIndexed { index, item ->
        line.append(item)
        if (index < this.size - 1) {
            line.append("+")
        }
    }
    return line.toString()
}