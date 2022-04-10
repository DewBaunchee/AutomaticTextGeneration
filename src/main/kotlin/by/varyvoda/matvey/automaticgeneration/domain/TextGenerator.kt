package by.varyvoda.matvey.automaticgeneration.domain

import by.varyvoda.matvey.automaticgeneration.domain.grammatics.Grammatics
import by.varyvoda.matvey.automaticgeneration.domain.grammatics.NonTerminal
import by.varyvoda.matvey.automaticgeneration.domain.grammatics.Symbol
import java.time.Instant
import kotlin.random.Random

class TextGenerator(private val grammatics: Grammatics) {

    private val random: Random = Random(Instant.now().toEpochMilli())

    fun generate(): String {
        return from(grammatics.initial)
    }

    private fun from(currentSymbol: Symbol, current: List<NonTerminal> = emptyList()): String {
        return if (currentSymbol is NonTerminal)
            randomOption(currentSymbol).joinToString("") { from(it, current.plus(currentSymbol)) }
        else
            currentSymbol.toString()
    }

    private fun randomOption(nonTerminal: NonTerminal): List<Symbol> {
        return nonTerminal.getOptions()[random.nextInt(0, nonTerminal.getOptions().size)]
    }
}