package de.steffektif.aoc2023.day03

import de.steffektif.aoc2023.loadFile

val schematic = mutableMapOf<String, String>()
var xBoundary = 0
var yBoundary = 0

val isNumber: (String) -> Boolean = { str: String -> "[0-9]".toRegex().matches(str) }
val isDot: (String) -> Boolean = { str: String -> str == "." }
val isSymbol: (String) -> Boolean = { str: String -> !(str == "." || isNumber(str)) }

val isBottomSymbol: (Pair<Int, Int>) -> Boolean =
    { coordinates ->
        val target = schematic["${coordinates.first}-${coordinates.second + 1}"]
        target != null && isSymbol(target)
    }

val isTopSymbol: (Pair<Int, Int>) -> Boolean =
    { coordinates ->
        val target = schematic["${coordinates.first}-${coordinates.second - 1}"]
        target != null && isSymbol(target)
    }

val isRightSymbol: (Pair<Int, Int>) -> Boolean =
    { coordinates ->
        val target = schematic["${coordinates.first + 1}-${coordinates.second}"]
        target != null && isSymbol(target)
    }

val isLeftSymbol: (Pair<Int, Int>) -> Boolean =
    { coordinates ->
        val target = schematic["${coordinates.first - 1}-${coordinates.second}"]
        target != null && isSymbol(target)
    }

val isTopLeftSymbol: (Pair<Int, Int>) -> Boolean =
    { coordinates ->
        val target = schematic["${coordinates.first - 1}-${coordinates.second - 1}"]
        target != null && isSymbol(target)
    }

val isTopRightSymbol: (Pair<Int, Int>) -> Boolean =
    { coordinates ->
        val target = schematic["${coordinates.first + 1}-${coordinates.second - 1}"]
        target != null && isSymbol(target)
    }

val isBottomLeftSymbol: (Pair<Int, Int>) -> Boolean =
    { coordinates ->
        val target = schematic["${coordinates.first - 1}-${coordinates.second + 1}"]
        target != null && isSymbol(target)
    }

val isBottomRightSymbol: (Pair<Int, Int>) -> Boolean =
    { coordinates ->
        val target = schematic["${coordinates.first + 1}-${coordinates.second + 1}"]
        target != null && isSymbol(target)
    }

val checks = listOf(
    isTopLeftSymbol,
    isTopSymbol,
    isTopRightSymbol,
    isLeftSymbol,
    isRightSymbol,
    isBottomLeftSymbol,
    isBottomSymbol,
    isBottomRightSymbol
)

var numbers = mutableListOf<Number>()

data class Number(val number: String, val start: Pair<Int, Int>, val end: Pair<Int, Int>)

fun main() {
    buildEngineSchematic()
    println("result day 3 a: ${solveA()}")
    println("result day 3 b: ${solveB()}")
}


fun solveA(): Int {
    var sum = 0
    val latest = mutableListOf<String>()
    val latestIndecies = mutableListOf<Pair<Int, Int>>()
    for (x in 0..xBoundary) {
        for (y in 0..yBoundary) {
            val content = schematic["$y-$x"]
            if (isNumber(content!!)) {
                latest.add(content)
                latestIndecies.add(Pair(y, x))
            }
            if (isDot(content) || isSymbol(content)) {
                if (latest.isNotEmpty() && latestIndecies.isNotEmpty()) {
                    numbers.add(
                        Number(
                            latest.joinToString(""),
                            latestIndecies[0],
                            latestIndecies[latestIndecies.lastIndex]
                        )
                    )
                }
                latest.clear()
                latestIndecies.clear()
            }
        }
    }

    numbers.forEach {
        val valid = isValid(it)
        if (valid) {
            sum += it.number.toInt()
        } else {
            println(it.number.toInt())
        }
    }
    return sum
}

// at least one surrounding is a symbol
private fun isValid(number: Number): Boolean {
    val start = number.start.first
    val end = number.end.first
    val line = number.start.second

    for (i in start..end) {
        if (checks.map { it(Pair(i, line)) }.any { it }) {
            return true
        }
    }
    return false
}


private fun debugContent(y: Int, x: Int, content: String?) {
    println(
        "$y-$x = ${content}, isNumber: ${isNumber(content!!)}, isDot: ${isDot(content)}, isSymbol: ${
            isSymbol(
                content
            )
        }"
    )
}

fun solveB(): Int {
    var sum = 0
    return sum
}

fun buildEngineSchematic() {
    var linenumber = 0
    loadFile("src/main/kotlin/de/steffektif/aoc2023/day03/input").forEachLine { line ->
        val split = line.split("").filter { it != "" }
        for (index in split.indices) {
            schematic["${index}-${linenumber}"] = split[index]
            xBoundary = index
        }
        yBoundary = linenumber
        linenumber++
    }
}

