package de.steffektif.aoc2023.day03

import de.steffektif.aoc2023.loadFile

val schematic = mutableMapOf<String, String>()
var xBoundary = 0
var yBoundary = 0

val isNumber: (String) -> Boolean = { str: String -> "[0-9]".toRegex().matches(str) }
val isDot: (String) -> Boolean = { str: String -> str == "." }
val isSymbol: (String) -> Boolean = { str: String -> !(str == "." || isNumber(str)) }
val isGear: (String) -> Boolean = { str: String -> str == "*" }


val checkForSymbol: (Pair<Int, Int>, Int, Int) -> Boolean = { coordinates, plusX, plusY ->
    val target = schematic["${coordinates.first + plusX}-${coordinates.second + plusY}"]
    target != null && isSymbol(target)
}

val checkForNumber: (Pair<Int, Int>, Int, Int) -> Pair<Int, Int>? = { coordinates, plusX, plusY ->
    val target = schematic["${coordinates.first + plusX}-${coordinates.second + plusY}"]
    if (target != null && isNumber(target)) Pair(coordinates.first + plusX, coordinates.second + plusY) else null
}

val isBottomNumber: (Pair<Int, Int>) -> Pair<Int, Int>? = { checkForNumber(it, 0, 1) }

val isTopNumber: (Pair<Int, Int>) -> Pair<Int, Int>? = { checkForNumber(it, 0, -1) }

val isRightNumber: (Pair<Int, Int>) -> Pair<Int, Int>? = { checkForNumber(it, 1, 0) }

val isLeftNumber: (Pair<Int, Int>) -> Pair<Int, Int>? = { checkForNumber(it, -1, 0) }

val isTopLeftNumber: (Pair<Int, Int>) -> Pair<Int, Int>? = { checkForNumber(it, -1, -1) }

val isTopRightNumber: (Pair<Int, Int>) -> Pair<Int, Int>? = { checkForNumber(it, 1, -1) }

val isBottomLeftNumber: (Pair<Int, Int>) -> Pair<Int, Int>? = { checkForNumber(it, -1, 1) }

val isBottomRightNumber: (Pair<Int, Int>) -> Pair<Int, Int>? = { checkForNumber(it, 1, 1) }


val isBottomSymbol: (Pair<Int, Int>) -> Boolean = { checkForSymbol(it, 0, 1) }

val isTopSymbol: (Pair<Int, Int>) -> Boolean = { checkForSymbol(it, 0, -1) }

val isRightSymbol: (Pair<Int, Int>) -> Boolean = { checkForSymbol(it, 1, 0) }

val isLeftSymbol: (Pair<Int, Int>) -> Boolean = { checkForSymbol(it, -1, 0) }

val isTopLeftSymbol: (Pair<Int, Int>) -> Boolean = { checkForSymbol(it, -1, -1) }

val isTopRightSymbol: (Pair<Int, Int>) -> Boolean = { checkForSymbol(it, 1, -1) }

val isBottomLeftSymbol: (Pair<Int, Int>) -> Boolean = { checkForSymbol(it, -1, 1) }

val isBottomRightSymbol: (Pair<Int, Int>) -> Boolean = { checkForSymbol(it, 1, 1) }

val numberChecks = listOf(
    isTopLeftNumber,
    isTopNumber,
    isTopRightNumber,
    isLeftNumber,
    isRightNumber,
    isBottomLeftNumber,
    isBottomNumber,
    isBottomRightNumber
)

val symbolChecks = listOf(
    isTopLeftSymbol,
    isTopSymbol,
    isTopRightSymbol,
    isLeftSymbol,
    isRightSymbol,
    isBottomLeftSymbol,
    isBottomSymbol,
    isBottomRightSymbol
)


data class Number(
    val number: String,
    val start: Pair<Int, Int>,
    val end: Pair<Int, Int>,
)


val coordsMap = mutableMapOf<String, Int>()

fun main() {
    buildEngineSchematic()
    println("result day 3 a: ${solveA()}")
    println("result day 3 b: ${solveB()}")
}

fun solveB(): Int {
    var sum = 0
    findNumbers()
    for (x in 0..xBoundary) {
        for (y in 0..yBoundary) {
            val content = schematic["$y-$x"]
            if (isGear(content!!)) {
                val neighbourNumbers = numberChecks.mapNotNull { it(Pair(x, y)) }
                if (neighbourNumbers.size == 2) {
                    // we found 2 numbers
                    val first = neighbourNumbers[0]
                    val second = neighbourNumbers[1]

                    val firstNumber = coordsMap["${first.second}-${first.first}"]
                    val secondNumber = coordsMap["${second.second}-${second.first}"]
                    sum += firstNumber!! * secondNumber!!
                }

            }
        }
    }
    return sum
}

fun solveA(): Int {
    var sum = 0
    val numbers = findNumbers()

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

private fun findNumbers(): MutableList<Number> {
    val numbers = mutableListOf<Number>()
    val latest = mutableListOf<String>()
    val latestIndices = mutableListOf<Pair<Int, Int>>()
    for (x in 0..xBoundary) {
        for (y in 0..yBoundary) {
            val content = schematic["$y-$x"]
            if (isNumber(content!!)) {
                latest.add(content)
                latestIndices.add(Pair(y, x))
            }
            if (isDot(content) || isSymbol(content)) {
                if (latest.isNotEmpty() && latestIndices.isNotEmpty()) {
                    numbers.add(
                        Number(
                            latest.joinToString(""),
                            latestIndices[0],
                            latestIndices[latestIndices.lastIndex],
                        )
                    )
                    for (index in latestIndices) {
                        coordsMap["$x-${index.first}"] = latest.joinToString("").toInt()
                    }
                }

                latest.clear()
                latestIndices.clear()
            }
        }
    }
    return numbers
}

// at least one surrounding is a symbol
private fun isValid(number: Number): Boolean {
    val start = number.start.first
    val end = number.end.first
    val line = number.start.second

    for (i in start..end) {
        if (symbolChecks.map { it(Pair(i, line)) }.any { it }) {
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

