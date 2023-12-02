package de.steffektif.aoc2023.day02

import de.steffektif.aoc2023.loadFile

const val allowedMaxRed = 12
const val allowedMaxGreen = 13
const val allowedMaxBlue = 14


fun main() {
    println("result day 2 a: ${solveA()}")
    println("result day 2 b: ${solveB()}")

}

fun solveA(): Int {
    var sum = 0
    loadFile("src/main/kotlin/de/steffektif/aoc2023/day02/input").forEachLine { line ->
        val id = line.split(":")[0].split(" ")[1].toInt()
        val sets = line.split(":")[1].split(";").map { it.trim() }
        val cubes = sets.map { set -> set.split(",").map { it.trim() }.map { cubes -> cubes.split(" ") } }.flatten()
        if (cubes.map { cube ->
                checkIfGreaterThanMax(cube[0].toInt(), cube[1])
            }.all { it }) {
            sum += id
        }
    }
    return sum
}

fun solveB(): Int {
    var sum = 0
    loadFile("src/main/kotlin/de/steffektif/aoc2023/day02/input").forEachLine { line ->
        val sets = line.split(":")[1].split(";").map { it.trim() }
        val cubes = sets.map { set -> set.split(",").map { it.trim() }.map { cubes -> cubes.split(" ") } }.flatten()
        sum += calculatePower(cubes)
    }
    return sum
}

// finds the highest number per color and multiplies all color counts together, returns the power of all 3 colors
fun calculatePower(cubes: List<List<String>>): Int {
    var maxGreen = 1
    var maxBlue = 1
    var maxRed = 1
    cubes.map { cube ->
        val count = cube[0].toInt()
        val color = cube[1]
        when (color) {
            "red" -> if (count > maxRed) {
                maxRed = count
            }

            "green" -> if (count > maxGreen) {
                maxGreen = count
            }

            "blue" -> if (count > maxBlue) {
                maxBlue = count
            }
        }
    }
    return maxGreen * maxRed * maxBlue
}

// return true if the count is below or the same as the maximum and therefore valid, returns false if the count is higher than the max => not valid
fun checkIfGreaterThanMax(count: Int, color: String): Boolean {
    return when (color) {
        "green" -> count <= allowedMaxGreen
        "red" -> count <= allowedMaxRed
        "blue" -> count <= allowedMaxBlue
        else -> false
    }
}