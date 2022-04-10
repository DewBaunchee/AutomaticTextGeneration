package by.varyvoda.matvey.automaticgeneration

import by.varyvoda.matvey.automaticgeneration.domain.GrammaticsGenerator
import by.varyvoda.matvey.automaticgeneration.domain.TextGenerator
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import java.util.stream.Collectors
import java.util.stream.Stream

class HelloController {

    @FXML
    private lateinit var generate: Button

    @FXML
    private lateinit var outputText: TextArea

    @FXML
    private lateinit var sampleText: TextArea

    @FXML
    private lateinit var minWordLength: TextField

    @FXML
    private lateinit var wordCount: TextField

    @FXML
    fun initialize() {
        generate.setOnMouseClicked {
            val samples = getSamples()
            if(samples.isEmpty()) return@setOnMouseClicked

            val minWordLength = getMinWordLength()
            val wordCount = getWordCount()
            val textGenerator = TextGenerator(GrammaticsGenerator().generate(samples))

            // caaab bbaab caab bbab cab bbb cb
            val output = Stream.generate { textGenerator.generate() }
                .filter { string -> string.length >= minWordLength }
                .limit(wordCount.toLong())
                .collect(Collectors.joining(" "))
            outputText.text = output
        }
    }

    private fun getSamples(): List<String> {
        return sampleText.text
            .split(" ")
            .filter { it.isNotBlank() }
    }

    private fun getWordCount(): Int {
        return parseInt(wordCount.text, 20)
    }

    private fun getMinWordLength(): Int {
        return parseInt(minWordLength.text, 5)
    }

    private fun parseInt(string: String, or: Int): Int {
        return try {
            string.toInt()
        } catch (e: Exception) {
            or
        }
    }
}