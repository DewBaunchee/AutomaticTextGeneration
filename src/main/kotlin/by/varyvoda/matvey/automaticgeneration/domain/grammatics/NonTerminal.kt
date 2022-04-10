package by.varyvoda.matvey.automaticgeneration.domain.grammatics

import java.util.*

class NonTerminal(vararg initialOptions: List<Symbol>) : Symbol() {

    private var options: MutableList<List<Symbol>> = initialOptions.toMutableList()

    fun getOptions(): List<List<Symbol>> {
        return options.toList()
    }

    fun merge(nonTerminal: NonTerminal) {
        options.addAll(nonTerminal.options)
    }

    fun isRecursive(): Boolean {
        return options.any { option -> option.any { symbol -> symbol === this } }
    }

    fun isRemain(): Boolean {
        return options.all { option -> option.all { symbol -> symbol.isTerminal() } }
    }

    fun replace(target: NonTerminal, replacement: NonTerminal) {
        options = options.map { option ->
            option.map symbolMap@{ symbol ->
                if (symbol === target) return@symbolMap replacement
                if (symbol is NonTerminal) symbol.replace(target, replacement)
                return@symbolMap symbol
            }
        }.toMutableList()
    }

    fun replaceRecursively(target: NonTerminal) {
        replaceRecursively(target, emptyList())
    }

    fun replaceRecursively(target: NonTerminal, current: List<NonTerminal>) {
        if(current.contains(this)) throw RecursiveCall(this)
        options = options.map { option ->
            option.map symbolMap@{ symbol ->
                if (symbol === target) return@symbolMap this
                if (symbol is NonTerminal) {
                    try {
                        symbol.replaceRecursively(target, current.plus(this))
                    } catch (_: RecursiveCall) {}
                }
                return@symbolMap symbol
            }
        }.toMutableList()
    }

    override fun tryString(string: String) {
        internalTry(string, mutableListOf())
    }

    override fun isTerminal(): Boolean {
        return false
    }

    override fun internalTry(string: String, steps: List<NonTerminal>): String {
        var finalString = string
        val errors = mutableListOf<GrammaticsErrorException>()
        options.forEach option@{ option ->
            option.forEach { symbol ->
                try {
                    finalString = symbol.internalTry(finalString, steps.plus(this))
                } catch (e: GrammaticsErrorException) {
                    var completedError: GrammaticsErrorException = e
                    if (completedError.errorNonTerminal == null)
                        completedError = completedError.withErrorNonTerminal(this)
                    errors.add(completedError)
                    return@option
                }
            }
            return finalString
        }
        errors.stream()
            .sorted(Comparator.comparing { error -> error.steps.size * -1 })
            .findFirst()
            .ifPresent { error ->
                throw error
            }

        throw GrammaticsErrorException(this, string, steps, this)
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NonTerminal

        return options == other.options
    }

    override fun toString(): String {
        return internalToString(Stack())
    }

    override fun internalToString(current: List<NonTerminal>): String {
        if (current.any { it === this }) throw RecursiveCall(this)
        return options.joinToString("|") { option ->
            val string = optionToString(option, current)
            return@joinToString "(${string})"
        }
    }

    private fun optionToString(option: List<Symbol>, current: List<NonTerminal>): String {
        val sb = StringBuilder()
        option.forEach { symbol ->
            if (symbol.isTerminal())
                sb.append(symbol.internalToString(current.plus(this)))
            else
                try {
                    sb.append(symbol.internalToString(current.plus(this)))
                } catch (e: RecursiveCall) {
                    if (e.nonTerminal === this) return "($sb)+"
                    throw e
                }
        }
        return sb.toString()
    }

    private class RecursiveCall(val nonTerminal: NonTerminal) : RuntimeException()
}
