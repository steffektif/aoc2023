package de.steffektif.aoc2023.day01

import java.io.File


fun main(args: Array<String>) {
//    println("Solution a = ${solveA()}")
    println("Solution b = ${solveB()}")
}

fun solveA(): Int {
    var sum = 0
    loadInput().forEachLine { string ->
        val digits = string.filter { it.isDigit() }
        sum += (digits.first().toString() + digits.last().toString()).toInt()
    }
    return sum
}

fun solveB(): Int {
    var sum = 0
    val numberWords = mapOf(
        Pair("one", "1"), Pair("two", "2"), Pair("three", "3"), Pair("four", "4"), Pair("five", "5"),
        Pair("six", "6"), Pair("seven", "7"), Pair("eight", "8"), Pair("nine", "9")
    )
    loadInput().forEachLine { string ->
        var replacedString = string
        numberWords.forEach { numberWord -> replacedString = replacedString.replace(numberWord.key, numberWord.value ) }
        val digits = replacedString.filter { it.isDigit() }
        sum += (digits.first().toString() + digits.last().toString()).toInt()
        println(replacedString)
        println(sum)
    }
    return sum
}

fun loadInput(): File {
    return File("src/main/kotlin/de/steffektif/aoc2023/day01/input")
}