package by.varyvoda.matvey.automaticgeneration.domain.grammatics

class Terminal(private val value: String) : Symbol() {

    override fun tryString(string: String) {
        throwIfNotSuitable(string, emptyList())
    }

    override fun isTerminal(): Boolean {
        return true
    }

    override fun internalTry(string: String, steps: List<NonTerminal>): String {
        throwIfNotSuitable(string, steps)
        return string.substring(value.length)
    }

    override fun internalToString(current: List<NonTerminal>): String {
        return toString()
    }

    private fun throwIfNotSuitable(string: String, steps: List<NonTerminal>) {
        if (!isSuitable(string))
            throw GrammaticsErrorException(this, string, steps, null)
    }

    private fun isSuitable(string: String): Boolean {
        return string.startsWith(value)
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Terminal

        return value == other.value
    }

    override fun toString(): String {
        return value
    }
}