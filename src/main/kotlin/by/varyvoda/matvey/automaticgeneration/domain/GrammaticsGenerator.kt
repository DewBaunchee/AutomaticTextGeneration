package by.varyvoda.matvey.automaticgeneration.domain

import by.varyvoda.matvey.automaticgeneration.domain.grammatics.*

class GrammaticsGenerator {

    fun generate(samples: List<String>): Grammatics {
        return buildBasic(samples).secondStep().thirdStep()
    }

    private fun buildBasic(samples: List<String>): Grammatics {
        val initial = NonTerminal()
        samples
            .sortedByDescending { it.length }
            .filter { it.isNotBlank() }
            .forEach { sample ->
                try {
                    initial.tryString(sample)
                } catch (e: GrammaticsErrorException) {
                    val root = getRoot(e.substring)
                    e.errorNonTerminal!!.merge(root)
                }
            }
        return grammaticsFromInitial(initial)
    }

    private fun getRoot(sample: String): NonTerminal {
        val currentSymbol = Terminal(sample[0].toString())
        if (sample.length == 1)
            return NonTerminal(listOf(currentSymbol))

        val nextSymbol = if (sample.length == 2)
            Terminal(sample[1].toString())
        else
            getRoot(sample.substring(1))

        return NonTerminal(listOf(currentSymbol, nextSymbol))
    }
}
