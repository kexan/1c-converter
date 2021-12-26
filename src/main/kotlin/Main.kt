package ru.kexan

import java.awt.Toolkit

import java.awt.datatransfer.DataFlavor.stringFlavor
import java.awt.datatransfer.StringSelection
import java.util.*


fun main() {
    while (true) {
        val scanner = Scanner(System.`in`)
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard

        println("Выберите режим:\n1. Для клиентов\n2. Прайс\n3. Тест")
        val mode = scanner.nextInt()
        println("Прогу можно свернуть, просто копируйте текст")

        while (scanner.nextLine() != "end") {
            var fixmepls = true
            clipboard.addFlavorListener { e ->
                //адский костыль
                fixmepls = !fixmepls
                if (!fixmepls) {
                    clipboard.setContents(
                        StringSelection(convert(clipboard.getData(stringFlavor) as String, mode)), null
                    )
                }
            }
        }

    }
}

fun convert(text: String, mode: Int): String {

    if (!text.contains("НФ-")) {
        return text
    }

    val convertedText: List<String>
    when (mode) {
        1 -> {
            convertedText = text.replace("(1)", "")
                .replace("(2)", "")
                .replace("(3)", "")
                .replace("(4)", "")
                .replace("(5)", "")
                .replace("(термопакет)", "")
                .replace("(коробочка)", "")
                .replace(",00", "")
                .replace("шт", "-")
                .split("\n")
                .distinct()
        }
        2 -> {
            convertedText = text
                .replace("(1)", "")
                .replace("(2)", "")
                .replace("(3)", "")
                .replace("(4)", "")
                .replace("(5)", "")
                .replace(",00", "")
                .replace("шт", "-")
                .split("\n")
                .distinct()
        }
        else -> {
            convertedText = text
                .replace(",00", "")
                .replace("шт", "-")
                .replace(",0", " шт")
                .split("\n")
                .filter {
                    it.contains("(1)") ||
                            it.contains("(2)") ||
                            it.contains("(3)") ||
                            it.contains("(4)")
                }
                .filterNot { it.contains("шт") }
        }
    }
    var formattedText = ""
    for (string in convertedText) {
        val formattedString = string.substring(12)
        formattedText += if (mode != 3) "$formattedString р.\n" else "$formattedString \n"
    }
    return formattedText
}