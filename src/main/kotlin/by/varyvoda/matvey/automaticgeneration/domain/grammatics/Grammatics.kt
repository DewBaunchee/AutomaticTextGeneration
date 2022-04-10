package by.varyvoda.matvey.automaticgeneration.domain.grammatics

fun grammaticsFromInitial(initial: NonTerminal): Grammatics {
    val terminals = hashSetOf<Terminal>()
    val nonTerminals = hashSetOf<NonTerminal>()
    val duplicates = hashMapOf<NonTerminal, List<NonTerminal>>()
    nonTerminals.add(initial)
    fillSymbols(initial, terminals, nonTerminals, duplicates)

    duplicates.entries.forEach {
        it.value.forEach { target ->
            initial.replace(target, it.key)
        }
    }
    return Grammatics(terminals, nonTerminals, initial)
}

private fun fillSymbols(
    nonTerminal: NonTerminal,
    terminals: MutableSet<Terminal>,
    nonTerminals: HashSet<NonTerminal>,
    duplicates: HashMap<NonTerminal, List<NonTerminal>>
) {
    nonTerminal.getOptions()
        .forEach { option ->
            option.forEach { symbol ->
                if (symbol.isTerminal()) {
                    symbol as Terminal
                    terminals.add(symbol)
                } else {
                    symbol as NonTerminal
                    val duplicated = nonTerminals.findByHashCode(symbol)
                    if (duplicated != null) {
                        duplicates.compute(duplicated) { _, value -> value?.plus(symbol) ?: listOf(symbol) }
                    } else {
                        nonTerminals.add(symbol)
                        fillSymbols(symbol, terminals, nonTerminals, duplicates)
                    }
                }
            }
        }
}

private fun <E> HashSet<E>.findByHashCode(other: E): E? = firstOrNull { it.hashCode() == other.hashCode() }

private fun <E> List<E>.endsWith(other: List<E>): Boolean {
    if (other.size > size) return false
    for (i in other.indices) {
        if (get(size - 1 - i) != other[other.size - 1 - i]) return false
    }
    return true
}

class Grammatics(val terminals: Set<Terminal>, val nonTerminals: Set<NonTerminal>, val initial: NonTerminal) {

    fun secondStep(): Grammatics {
        val remains = nonTerminals
            .filter { it.getOptions().size == 2 && it.isRemain() }
            .filter {
                val options = it.getOptions().sortedByDescending { option -> option.size }
                return@filter options[0].endsWith(options[1])
            }
        remains.forEach { remain ->
            initial.replaceRecursively(remain)
        }
        return Grammatics(terminals, nonTerminals.filter { !remains.contains(it) }.toSet(), initial)
    }

    fun thirdStep(): Grammatics {
        return this
    }
}