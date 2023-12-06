package de.steffektif.aoc2023.day04

import de.steffektif.aoc2023.loadFile
import kotlin.math.pow

fun main() {
    val loadedString = loadFile("src/main/kotlin/de/steffektif/aoc2023/day04/input").useLines { it.toList() }
    println("Solution a = ${solveA(loadedString.joinToString("\n"))}")
    println("Solution b = ${solveB(loadedString.joinToString { "\n" })}")
}

fun solveA(input: String): Int {
    var sum = 0
    input.split("\n").map { line ->
        val split = line.split(":")[1].split("|")
        val winningNumbers = split[0].trim().split(" ").filter { it != "" }
        val yourNumbers = split[1].trim().split(" ").filter { it != "" }
        var hits = 0
        for (winningNumber in winningNumbers) {
            if (yourNumbers.contains(winningNumber)) hits++
        }
        if (hits == 1) sum++
        if (hits == 2) sum += 2
        if (hits > 2) sum += 2.0.pow((hits - 1).toDouble()).toInt()
    }
    return sum

}

fun solveB(input: String): Int {
    var sum = 0
    input.split("\n").map { line ->

    }
    return sum
}