package by.varyvoda.matvey.automaticgeneration.domain.grammatics

class GrammaticsErrorException(
    val errorSymbol: Symbol,
    val substring: String,
    val steps: List<NonTerminal>,
    val errorNonTerminal: NonTerminal?
) :
    Exception() {

    fun withErrorNonTerminal(errorNonTerminal: NonTerminal): GrammaticsErrorException {
        return GrammaticsErrorException(errorSymbol, substring, steps, errorNonTerminal)
    }
}