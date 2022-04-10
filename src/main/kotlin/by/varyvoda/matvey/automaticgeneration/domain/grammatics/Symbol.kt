package by.varyvoda.matvey.automaticgeneration.domain.grammatics

abstract class Symbol {

    abstract fun tryString(string: String)

    abstract fun isTerminal(): Boolean

    fun isNonTerminal(): Boolean {
        return !isTerminal()
    }

    internal abstract fun internalTry(string: String, steps: List<NonTerminal>): String

    internal abstract fun internalToString(current: List<NonTerminal>): String
}