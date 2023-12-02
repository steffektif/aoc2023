package de.steffektif.aoc2023.day01

import de.steffektif.aoc2023.loadFile
import java.io.File
import java.util.*


val numberWords = mapOf(
    Pair("one", "1"), Pair("two", "2"), Pair("three", "3"), Pair("four", "4"), Pair("five", "5"),
    Pair("six", "6"), Pair("seven", "7"), Pair("eight", "8"), Pair("nine", "9")
)
fun main(args: Array<String>) {
//    println("Solution a = ${solveA()}")
    println("Solution b = ${solveB()}")
}

fun solveA(): Int {
    var sum = 0
    loadFile("src/main/kotlin/de/steffektif/aoc2023/day01/input").forEachLine { string ->
        val digits = string.filter { it.isDigit() }
        sum += (digits.first().toString() + digits.last().toString()).toInt()
    }
    return sum
}

fun solveB(): Int {
    var sum = 0
    loadFile("src/main/kotlin/de/steffektif/aoc2023/day01/input").forEachLine { string ->

        val lastDigitIndex = string.indexOfLast { it.isDigit() }
        val firstDigitIndex = string.indexOfFirst { it.isDigit() }

        var first = string[firstDigitIndex].toString()
        var last = string[lastDigitIndex].toString()

        val beforeFirst = string.substring(0, firstDigitIndex)
        val afterLast = string.substring(lastDigitIndex, string.length)

        val numberWordHitsBefore = checkForNumberWord(beforeFirst)
        val numberWordHitsAfter = checkForNumberWord(afterLast)
        if(numberWordHitsBefore.isNotEmpty()) {
            first = numberWords[numberWordHitsBefore.first.second]!!
        }

        if(numberWordHitsAfter.isNotEmpty()) {
            last = numberWords[numberWordHitsAfter.last.second]!!
        }

        sum += (first + last).toInt()
        println(sum)
    }
    return sum
}


// returns a sorted set of pairs holding the start-index of the finding and the finding as word
fun checkForNumberWord(string: String): SortedSet<Pair<Int, String>> {
     return numberWords.map { numberWord ->
        val pairs = mutableListOf<Pair<Int, String>>()
        if (string.contains(numberWord.key)) {
            numberWord.key.toRegex().findAll(string).toList().map {
                pairs.add(Pair(it.range.first, numberWord.key))
            }
        }
        pairs
    }.filter { it.isNotEmpty() }.flatten().toSortedSet(compareBy { it.first })
}